package com.eshore.yxt.media.quartz;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.JsonHttpUtil;
import com.eshore.yxt.media.core.util.media.FileDigest;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FfmpegMediaJob {

    public static final Logger logger = LoggerFactory.getLogger(FfmpegMediaJob.class);
    private static AtomicInteger count = new AtomicInteger(0);//线程安全的计数变量
    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private MediaFileService mediaFileService;

    @Value("${root.file.path}")
    private String mediaFileRootPath;
    @Value("${yxt.ftp.ffmpegcount}")
    private Integer ffmpegCount;
    @Value("${voicerecord.ffmpeg.path.linux}")
    private String ffmpegPath_linux;
    @Value("${voicerecord.ffmpeg.path.window}")
    private String ffmpegPath_window;
    public static final String MAC_OS = "Mac OS X";
    public static final String WINDOW_OS = "Windows";

    @Scheduled(cron = "1 0/1 *  * * ? ")//每隔1秒隔行一次
    public synchronized void run(){
        if ((count.get()) > ffmpegCount) {
            logger.error("定时任务运行失败。当前服务的定时转码任务数已经超过了"+ffmpegCount+"个。暂停增加线程数。");
            return;
        }
        int countV2 = count.incrementAndGet();// 自增1,返回更新值
        logger.info("定时任务运行成功。当前服务的定时转码任务数递增到："+countV2+"个。");

        //获得已下载完成的列表
        List<Long> taskIdList = taskMessageService.findIdListTaskMessageByStatus(Constants.TaskMessageStatus.DOWNLOADED);
        File file = null;
        String sourceFile = null;
        MediaFile mediaFileV = null;
                //循环处理
        TaskMessage taskMessage = null;
        for(Long taskId:taskIdList){
            try{
                taskMessage = taskMessageService.getByid(taskId);
                if(taskMessage==null||taskMessage.getStatus().intValue()!= Constants.TaskMessageStatus.DOWNLOADED.intValue()){
                    continue;
                }
                //判断文件是否存在，进行转码处理
                MediaFile mediaFile = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_0");
                String localPath = String.format(mediaFileRootPath + File.separator + "task" + File.separator + taskMessage.getType()
                        + File.separator + DateFormatUtils.format(new Date(),"yyyyMMdd"))+File.separator +taskMessage.getTaskId();
                String fileName = mediaFile.getFileName();
                sourceFile = localPath+File.separator+mediaFile.getFileName();
                file = new File(sourceFile);
                if(!file.exists()){
                    throw new Exception("下载的文件不存在，无法进行转码。mediaFile="+mediaFile.getFileId());
                }
                //转码处理中
                //进行下载
                taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEGING);
                taskMessage.setDuleMessage("文件转码中");
                taskMessageService.addOrUpdate(taskMessage);
                mediaFileV = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_1");
                if(mediaFileV==null){
                    Boolean isSucc1 = ffmpegVideo(sourceFile,localPath,taskMessage.getTaskId()+"_1.mp4","1");
                    if(isSucc1){
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
                    }
                }

                mediaFileV = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_2");
                if(mediaFileV==null){
                    Boolean isSucc2 = ffmpegVideo(sourceFile,localPath,taskMessage.getTaskId()+"_2.mp4","2");
                    if(isSucc2){
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
                    }
                }

                //转码完成
                taskMessage.setStatus(Constants.TaskMessageStatus.CALLBACKING);
                taskMessage.setDuleMessage("文件完成转码,回调发送第三方结果");
                taskMessageService.addOrUpdate(taskMessage);
                ///转码完成。新增视频文件数据
                String result = JsonHttpUtil.doPost(taskMessage.getCallbackUrl(),"{}");
                if("ok".equals(result)){
                    //完成转码流程
                    taskMessage.setStatus(Constants.TaskMessageStatus.DULED);
                    taskMessage.setDuleMessage("转码流程处理完成");
                    taskMessageService.addOrUpdate(taskMessage);
                }else{
                    logger.error("FfmpegMediaJob error 回调第三方结果失败，信息为："+result);
                }
            }catch (Exception e){
                logger.error("FfmpegMediaJob error-->,{}",e);
                taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEGED);
                taskMessage.setDuleMessage(e.getMessage());
                taskMessageService.addOrUpdate(taskMessage);
                continue;
            }

        }
        int countV =count.decrementAndGet();// 自减1,返回更新值
        logger.info("定时任务运行结束。当前服务的定时转码任务数递减到："+countV+"个。");
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
