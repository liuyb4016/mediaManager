package com.eshore.yxt.media.service.media.impl;

import cn.eshore.common.util.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.FtpDownloadUtil;
import com.eshore.yxt.media.core.util.HttpUtils;
import com.eshore.yxt.media.core.util.JsonHttpUtil;
import com.eshore.yxt.media.core.util.cache.MemcacheCaller;
import com.eshore.yxt.media.core.util.media.FileDigest;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.repository.media.MediaFileRepository;
import com.eshore.yxt.media.repository.media.TaskMessageRepository;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskLogService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;
import com.eshore.yxt.media.web.mdeia.resp.TaskMessageResq;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskMessageSerivceImpl implements TaskMessageService {

    public static final Logger logger = LoggerFactory.getLogger(TaskMessageSerivceImpl.class);

    @Autowired
    private TaskMessageRepository taskMessageRepository;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private MediaFileService mediaFileService;

    @Value("${yxt.ftp.sever}")
    private String ftpServer;
    @Value("${yxt.ftp.port}")
    private Integer ftpServerPort;
    @Value("${yxt.ftp.user}")
    private String ftpServerUser;
    @Value("${yxt.ftp.password}")
    private String ftpServerPwd;


    @Value("${http.res.url}")
    private String httpResUrl;
    @Value("${root.file.path}")
    private String mediaFileRootPath;
    @Value("${voicerecord.ffmpeg.path.linux}")
    private String ffmpegPath_linux;
    @Value("${voicerecord.ffmpeg.path.window}")
    private String ffmpegPath_window;
    public static final String MAC_OS = "Mac OS X";
    public static final String WINDOW_OS = "Windows";

    @Override
    public TaskMessage getTaskMessageByFileId(Integer type,String fileId) {
        return taskMessageRepository.getTaskMessageByFileId(type,fileId);
    }

    @Override
    public TaskMessage addOrUpdate(TaskMessage taskMessage) {
        taskMessage.setUpdateTime(new Date());
        return taskMessageRepository.save(taskMessage);
    }

    @Override
    public List<Long> findIdListTaskMessageByStatus(Integer status) {
        return taskMessageRepository.findIdListTaskMessageByStatus(status);
    }

    @Override
    public TaskMessage getByid(Long id){
        return taskMessageRepository.findOne(id);
    }

    @Override
    public Boolean duleDownload(Long taskId) {
        TaskMessage taskMessage = this.getByid(taskId);
        if(taskMessage==null||taskMessage.getStatus().intValue()!= Constants.TaskMessageStatus.NO_DULE.intValue()) {
            return false;
        }
        Boolean isIn = MemcacheCaller.INSTANCE.add(Constants.DOWNLOADING_TASK_KEY+"_"+taskMessage.getTaskId(),60*60,"2");
        if(isIn){
            //进行下载
            taskMessage.setStatus(Constants.TaskMessageStatus.DOWNLOADING);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),1,"正在下载");

            String filePath = null;
            String fileName = null;
            long fileSize = 0;
            String fileMd5 = null;
            try{
                String videoUrl = taskMessage.getVideoUrl();
                String videoName = taskMessage.getVideoName();
                String md5 = taskMessage.getVideoMd5();
                Long size = taskMessage.getVideoSize();

                if(StringUtils.isBlank(videoUrl)){
                    throw new Exception("文件下载地址不正确。taskId="+taskMessage.getTaskId()+"------> videoUrl="+videoUrl);
                }
                String localPath = String.format(mediaFileRootPath + File.separator + "task" + File.separator + taskMessage.getType()
                        + File.separator + DateFormatUtils.format(new Date(),"yyyyMMdd"))+File.separator +taskMessage.getTaskId();
                String suffix = videoName.substring(videoName.lastIndexOf("."));
                fileName = taskMessage.getTaskId()+"_0"+suffix;
                //下载后的文件
                filePath = localPath+File.separator+fileName;
                //下载文件
                FtpDownloadUtil.downloadFtpFile(ftpServer,ftpServerUser,ftpServerPwd,ftpServerPort,videoUrl,localPath,taskMessage.getVideoName(),fileName);

                //说明文件下载成功
                // 检测对比文件是否正确，检测  文件的 大小和md5
                File downfile = new File(filePath);
                if(downfile==null||!downfile.exists()){
                    throw new Exception("文件下载失败，没有检测到文件存在。taskId="+taskMessage.getTaskId());
                }
                fileSize = downfile.length();
                fileMd5 = FileDigest.getFileMD5(downfile);
                if(fileSize!=size.longValue()||!md5.equals(fileMd5)){
                    throw new Exception("文件下载失败，文件已下载，但文件内容长度或md5 与接口获取的值不一致。taskId="+taskMessage.getTaskId());
                }
            }catch(Exception e){
                logger.error("DownloadMediaJob error-->,{}",e);
                if(filePath!=null){
                    File downfile = new File(filePath);
                    if(downfile.exists()){
                        downfile.delete();
                    }
                }
                //删除标记
                MemcacheCaller.INSTANCE.delete(Constants.DOWNLOADING_TASK_KEY+"_"+taskMessage.getTaskId());
                taskMessage.setStatus(Constants.TaskMessageStatus.DOWNLOAD_ERROR);
                this.addOrUpdate(taskMessage);
                taskLogService.addLog(taskMessage.getTaskId(),0,"下载过程中出现错误："+e.getMessage());
                return false;
            }

            //下载完成
            taskMessage.setStatus(Constants.TaskMessageStatus.DOWNLOADED);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),1,"成功完成下载");
            //新增数据视频源文件记录
            MediaFile mediaFile = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId());
            if(mediaFile==null){
                mediaFile = new MediaFile();
            }
            mediaFile.setTaskId(taskMessage.getTaskId()+"_0");
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
            MemcacheCaller.INSTANCE.delete(Constants.DOWNLOADING_TASK_KEY+"_"+taskMessage.getTaskId());
            return true;
        }else{
            return false;
        }
    }

    public Boolean ffmpegVideo270P(Long taskId) {
        String sourceFile = null;
        File file = null;
        TaskMessage taskMessage = null;
        MediaFile mediaFileV = null;
        try{
            taskMessage = this.getByid(taskId);
            if(taskMessage==null||taskMessage.getStatus().intValue()!= Constants.TaskMessageStatus.DOWNLOADED.intValue()){
                return false;
            }
            //判断文件是否存在，进行转码处理
            MediaFile mediaFile = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_0");
            String localPath = mediaFileRootPath + File.separator + "task" + File.separator + taskMessage.getType()
                    + File.separator + DateFormatUtils.format(new Date(),"yyyyMMdd")+File.separator +taskMessage.getTaskId();
            sourceFile = localPath+File.separator+mediaFile.getFileName();
            file = new File(sourceFile);
            if(!file.exists()){
                throw new Exception("下载的文件不存在，无法进行转码。mediaFile="+mediaFile.getFileId());
            }
            //转码处理中
            //进行下载
            taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG270ING);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),1,"文件转码270P中");

            mediaFileV = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_1");
            if(mediaFileV==null){
                taskLogService.addLog(taskMessage.getTaskId(),1,"1.标清文件转码中");
                Boolean isSucc1 = ffmpegVideo(sourceFile,localPath,taskMessage.getTaskId()+"_1.mp4","1");
                if(isSucc1){
                    taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG270ED);
                    this.addOrUpdate(taskMessage);
                    taskLogService.addLog(taskMessage.getTaskId(),1,"1.标清文件转码完成");
                    File fileV =  new File(localPath+File.separator+taskMessage.getTaskId()+"_1.mp4");
                    if(fileV!=null&&fileV.exists()&& fileV.length()>0){
                        mediaFileV = new MediaFile();
                        mediaFileV.setTaskId(taskMessage.getTaskId()+"_1");
                        mediaFileV.setSourceId(mediaFile.getId());
                        mediaFileV.setDefType(1);
                        mediaFileV.setFileName(taskMessage.getTaskId()+"_1.mp4");
                        mediaFileV.setMd5(FileDigest.getFileMD5(fileV));
                        mediaFileV.setSize(fileV.length());
                        mediaFileV.setType(taskMessage.getType());
                        mediaFileV.setFileId(taskMessage.getFileId());
                        mediaFileV.setCreateTime(new Date());
                        mediaFileV.setUpdateTime(new Date());
                        mediaFileService.addOrUpdate(mediaFileV);
                    }
                }else{
                    taskLogService.addLog(taskMessage.getTaskId(),0,"1.标清文件转码失败");
                }
            }else{
                taskLogService.addLog(taskMessage.getTaskId(),0,"转码270 文件已被转码，不需要再次转码");
                taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG270_ERROR);
                this.addOrUpdate(taskMessage);
                return false;
            }
        }catch (Exception e){
            logger.error("FfmpegMediaJob error-->,{}",e);
            taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG270_ERROR);
            this.addOrUpdate(taskMessage);
            String log = "转码流程270中发生异常："+e.getMessage();
            if(log.length()>900){
                log = log.substring(0,900);
            }
            taskLogService.addLog(taskMessage.getTaskId(),0,log);
            return false;
        }
        return true;
    }

    public Boolean ffmpegVideo720P(Long taskId) {
        String sourceFile = null;
        File file = null;
        TaskMessage taskMessage = null;
        MediaFile mediaFileV = null;
        try{
            taskMessage = this.getByid(taskId);
            if(taskMessage==null||taskMessage.getStatus().intValue()!= Constants.TaskMessageStatus.FFMPEG270ED.intValue()){
                return false;
            }
            //判断文件是否存在，进行转码处理
            MediaFile mediaFile = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_0");
            String localPath = mediaFileRootPath + File.separator + "task" + File.separator + taskMessage.getType()
                    + File.separator + DateFormatUtils.format(new Date(),"yyyyMMdd")+File.separator +taskMessage.getTaskId();
            sourceFile = localPath+File.separator+mediaFile.getFileName();
            file = new File(sourceFile);
            if(!file.exists()){
                throw new Exception("下载的文件不存在，无法进行转码。mediaFile="+mediaFile.getFileId());
            }
            //转码处理中
            //进行下载
            taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG720ING);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),1,"文件720转码中");

            mediaFileV = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_2");
            if(mediaFileV==null){
                taskLogService.addLog(taskMessage.getTaskId(),1,"2.标清文件转码中");
                Boolean isSucc2 = ffmpegVideo(sourceFile,localPath,taskMessage.getTaskId()+"_2.mp4","2");
                if(isSucc2){
                    taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG720ED);
                    this.addOrUpdate(taskMessage);
                    taskLogService.addLog(taskMessage.getTaskId(),1,"2.标清文件转码完成");
                    File fileV =  new File(localPath+File.separator+taskMessage.getTaskId()+"_2.mp4");
                    if(fileV!=null&&fileV.exists()&& fileV.length()>0){
                        mediaFileV = new MediaFile();
                        mediaFileV.setTaskId(taskMessage.getTaskId()+"_2");
                        mediaFileV.setSourceId(mediaFile.getId());
                        mediaFileV.setDefType(2);
                        mediaFileV.setFileName(taskMessage.getTaskId()+"_2.mp4");
                        mediaFileV.setMd5(FileDigest.getFileMD5(fileV));
                        mediaFileV.setSize(fileV.length());
                        mediaFileV.setType(taskMessage.getType());
                        mediaFileV.setFileId(taskMessage.getFileId());
                        mediaFileV.setCreateTime(new Date());
                        mediaFileV.setUpdateTime(new Date());
                        mediaFileService.addOrUpdate(mediaFileV);
                    }
                }else{
                    taskLogService.addLog(taskMessage.getTaskId(),0,"2.标清文件转码失败");
                }
            }else{
                taskLogService.addLog(taskMessage.getTaskId(),0,"转码720 文件已被转码，不需要再次转码");
                taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG720_ERROR);
                this.addOrUpdate(taskMessage);
                return false;
            }
        }catch (Exception e){
            logger.error("FfmpegMediaJob error-->,{}",e);
            taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEG720_ERROR);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),0,"转码720流程中发生异常："+e.getMessage());
            return false;
        }
        return true;
    }


    public Boolean callBackUrl(Long taskId) {
        String sourceFile = null;
        File file = null;
        TaskMessage taskMessage = null;
        MediaFile mediaFileV = null;
        try{
            taskMessage = this.getByid(taskId);
            if(taskMessage==null||taskMessage.getStatus().intValue()!= Constants.TaskMessageStatus.FFMPEG720ED.intValue()){
                return false;
            }

            //转码完成
            taskMessage.setStatus(Constants.TaskMessageStatus.CALLBACKING);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),1,"文件完成转码,准备回调发送第三方结果");
            ///转码完成。新增视频文件数据
            MediaFile mediaFile1 = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_1");
            MediaFile mediaFile2 = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_2");
            //类型  1 标清  2 高清
            List<TaskMessageResq> listNew = new ArrayList<TaskMessageResq>();
            if(mediaFile1!=null) listNew.add(new TaskMessageResq(mediaFile1.getFileId(),mediaFile1.getDefType()+"",getMediaUrl(taskMessage,mediaFile1)));
            if(mediaFile2!=null) listNew.add(new TaskMessageResq(mediaFile2.getFileId(),mediaFile2.getDefType()+"",getMediaUrl(taskMessage,mediaFile2)));

            JSONArray jsonArray=new JSONArray();//1、创建JSONArray
            jsonArray.addAll(listNew);
            String jsonstr = jsonArray.toJSONString();
            Map<String,String> paramsMap = new HashMap<String, String>();
            paramsMap.put("result",jsonstr);
            String result = HttpUtils.requestByPostMethod(taskMessage.getCallbackUrl(),paramsMap);
            if("\"ok\"".equals(result)){
                //完成转码流程
                taskMessage.setStatus(Constants.TaskMessageStatus.DULED);
                this.addOrUpdate(taskMessage);
                taskLogService.addLog(taskMessage.getTaskId(),1,"成功回调第三方接口。转码流程处理完成");
            }else{
                taskMessage.setStatus(Constants.TaskMessageStatus.CALLBACK_ERROR);
                this.addOrUpdate(taskMessage);
                logger.error("FfmpegMediaJob error 回调第三方结果失败，信息为："+result);
                taskLogService.addLog(taskMessage.getTaskId(),0,"回调第三方接口失败。转码流程处理失败");
                return false;
            }
        }catch (Exception e){
            logger.error("FfmpegMediaJob error-->,{}",e);
            taskMessage.setStatus(Constants.TaskMessageStatus.CALLBACK_ERROR);
            this.addOrUpdate(taskMessage);
            taskLogService.addLog(taskMessage.getTaskId(),0,"回调第三方接口失败。转码流程处理异常："+e.getMessage());
            return false;
        }
        return true;
    }

    private String getMediaUrl(TaskMessage taskMessage,MediaFile mediaFile){
        String localPath = httpResUrl + "/task/" + taskMessage.getType()
                + "/"+ DateFormatUtils.format(new Date(),"yyyyMMdd")+"/" +taskMessage.getTaskId();
        return localPath+"/"+mediaFile.getFileName();
    }

    /**
     *
     * @Title: ffmpegVoice
     * @Description: 视频转码
     * @param @param sourceFile
     * @param @param targetFile
     * @param @param fileName
     * @param @param type    参数   1 标清   2；高清
     * @return void    返回类型
     * @throws
     */
    public Boolean ffmpegVideo(String sourceFile,String targetFile,String fileName,String type){
        Boolean isSuccess = true;
        File parentdir = new File(targetFile);
        if (!parentdir.exists()) {
            parentdir.mkdirs();
        }
        Properties prop = System.getProperties();
        String osName = prop.getProperty("os.name", "");
        String filename = "";
        if (osName.equals(MAC_OS)) { // mac osx
            filename = ffmpegPath_linux;
        } else if (osName.startsWith(WINDOW_OS)) { // windows
            filename = ffmpegPath_window;
        } else {
            filename = ffmpegPath_linux;
        }
        InputStream in = null;
        Process proc = null;
        try {

            //音频  -f mp3 -acodec libmp3lame -y
            //-acodec pcm_s16le

            //-i before-code.flv -vcodec h264 -s 480x270 -pix_fmt yuv420p -acodec aac 20170801_1.mp4
            //-i before-code.flv -vcodec h264 -s 960×720 -pix_fmt yuv420p -acodec aac 20170801_1.mp4
            String params = "";
            if("1".equals(type)){//480x270
                params = " -vcodec h264 -s 480x270 -pix_fmt yuv420p -acodec aac -y ";
            }else if("2".equals(type)){//960×720
                params = " -vcodec h264 -s 960x720 -pix_fmt yuv420p -acodec aac -y ";
            }
            String cmd = filename+" -loglevel quiet -i "+sourceFile+params+targetFile+File.separator+fileName;
            logger.info("ffmpeg video cmd = {}", cmd);
            proc = Runtime.getRuntime().exec(cmd);
            in = proc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.info("ffmpeg video {}", line);
            }

        } catch (Exception e) {
            logger.error("ffmpeg video error", e);
            e.printStackTrace();
            isSuccess = false;
        } finally {
            IOUtils.closeQuietly(in);
            if (proc != null)
                proc.destroy();
        }
        return isSuccess;
    }
}
