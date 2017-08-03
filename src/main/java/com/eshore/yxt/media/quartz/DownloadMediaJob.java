package com.eshore.yxt.media.quartz;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.FtpDownloadUtil;
import com.eshore.yxt.media.core.util.HttpDownloadUtil;
import com.eshore.yxt.media.core.util.cache.MemcacheCaller;
import com.eshore.yxt.media.core.util.media.FileDigest;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import com.eshore.yxt.media.service.media.impl.MediaFileSerivceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DownloadMediaJob {

    public static final Logger logger = LoggerFactory.getLogger(DownloadMediaJob.class);

    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private MediaFileService mediaFileService;

    @Value("yxt.ftp.sever")
    private String ftpServer;
    @Value("yxt.ftp.port")
    private int ftpServerPort;
    @Value("yxt.ftp.user")
    private String ftpServerUser;
    @Value("yxt.ftp.password")
    private String ftpServerPwd;

    @Value("root.file.path")
    private String mediaFileRootPath;

    @Scheduled(cron = "1 0/30 *  * * ? ")//每隔30分钟隔行一次
    public void run(){
        //检测文件下载
        List<Long> taskIdList = taskMessageService.findIdListTaskMessageByStatus(Constants.TaskMessageStatus.NO_DULE);
        File file = null;
        for(Long taskId:taskIdList){
            TaskMessage taskMessage = taskMessageService.getByid(taskId);
            if(taskMessage!=null&&taskMessage.getStatus()== Constants.TaskMessageStatus.NO_DULE){
                Boolean isIn = MemcacheCaller.INSTANCE.add(Constants.DOWNLOADING_TASK_KEY+"_"+taskId,60*60,"2");
                if(isIn){
                    //进行下载
                    taskMessage.setStatus(Constants.TaskMessageStatus.DOWNLOADING);
                    taskMessageService.addOrUpdate(taskMessage);
                    String filePath = null;
                    String fileName = null;
                    Long fileSize = 0L;
                    String fileMd5 = null;
                    try{
                        String videoUrl = taskMessage.getVideoUrls();
                        String videoName = taskMessage.getVideoName();
                        Integer urlType = taskMessage.getUrlType();
                        String md5 = taskMessage.getVideoMd5();
                        Long size = taskMessage.getVideoSize();

                        if(StringUtils.isBlank(videoUrl)||!(urlType==Constants.UrlType.TYPE_FTL||urlType==Constants.UrlType.TYPE_HTTP)){
                            throw new Exception("文件下载地址不正确。taskId="+taskMessage.getTaskId()+"--URLType="+urlType+"----> videoUrl="+videoUrl);
                        }
                        String localPath = mediaFileRootPath+ File.separator+"tasktemp"+File.separator+taskMessage.getType();
                        String suffix = videoName.substring(videoName.lastIndexOf("."));
                        fileName = taskMessage.getTaskId()+suffix;
                        //下载后的文件
                        filePath = localPath+File.separator+fileName;
                        if(urlType==Constants.UrlType.TYPE_FTL){
                            //下载文件
                            FtpDownloadUtil.downloadFtpFile(ftpServer,ftpServerUser,ftpServerPwd,ftpServerPort,videoUrl,localPath,fileName);
                        }else if(urlType==Constants.UrlType.TYPE_HTTP){
                            HttpDownloadUtil.downLoadFromUrl(videoUrl,fileName,localPath);
                        }
                        //说明文件下载成功
                        // 检测对比文件是否正确，检测  文件的 大小和md5
                        File downfile = new File(filePath);
                        if(downfile==null||!downfile.exists()){
                            throw new Exception("文件下载失败，没有检测到文件存在。taskId"+taskMessage.getTaskId());
                        }
                        fileSize = downfile.length();
                        fileMd5 = FileDigest.getFileMD5(downfile);
                        if(!(fileSize==size&&md5.equals(fileMd5))){
                            throw new Exception("文件下载失败，文件已下载，但文件内容长度或md5 与接口获取的值不一致。taskId"+taskMessage.getTaskId());
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        if(filePath!=null){
                            File downfile = new File(filePath);
                            if(downfile.exists()){
                                downfile.delete();
                            }
                        }
                        //删除标记
                        MemcacheCaller.INSTANCE.delete(Constants.DOWNLOADING_TASK_KEY+"_"+taskId);

                        taskMessage.setStatus(Constants.TaskMessageStatus.DOWNLOAD_ERROR);
                        taskMessageService.addOrUpdate(taskMessage);
                        continue;
                    }

                    //下载完成
                    taskMessage.setStatus(Constants.TaskMessageStatus.DOWNLOADED);
                    taskMessageService.addOrUpdate(taskMessage);

                    //新增数据视频源文件记录
                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setSourceId(0L);
                    mediaFile.setDefType(0);
                    mediaFile.setFileName(fileName);
                    mediaFile.setMd5(fileMd5);
                    mediaFile.setSize(fileSize);
                    mediaFile.setType(taskMessage.getType());
                    mediaFile.setFileId(taskMessage.getFileId());
                    mediaFile.setCreateTime(new Date());
                    mediaFile.setUpdateTime(new Date());
                    mediaFileService.addOrUpdate(mediaFile);

                    //删除标记
                    MemcacheCaller.INSTANCE.delete(Constants.DOWNLOADING_TASK_KEY+"_"+taskId);
                }
            }
        }
    }
}
