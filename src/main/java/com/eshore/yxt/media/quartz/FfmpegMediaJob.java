package com.eshore.yxt.media.quartz;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskMessageService;
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
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FfmpegMediaJob {

    public static final Logger logger = LoggerFactory.getLogger(FfmpegMediaJob.class);
    private static AtomicInteger count = new AtomicInteger(0);//线程安全的计数变量
    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private MediaFileService mediaFileService;

    @Value("root.file.path")
    private String mediaFileRootPath;
    @Value("yxt.ftp.ffmpegcount")
    private String ffmpegCount;

    //@Scheduled(cron = "1 0/30 *  * * ? ")//每隔1秒隔行一次
    public synchronized void run(){
        Integer ffmpegCountV = Integer.valueOf(ffmpegCount);
        if ((count.get()) > ffmpegCountV) {
            logger.error("定时任务运行失败。当前服务的定时转码任务数已经超过了"+ffmpegCountV+"个。暂停增加线程数。");
            return;
        }
        int countV2 = count.incrementAndGet();// 自增1,返回更新值
        logger.info("定时任务运行成功。当前服务的定时转码任务数递增到："+countV2+"个。");

        //获得已下载完成的列表
        List<Long> taskIdList = taskMessageService.findIdListTaskMessageByStatus(Constants.TaskMessageStatus.DOWNLOADED);
        File file = null;
        String filePath = null;
        //循环处理
        TaskMessage taskMessage = null;
        for(Long taskId:taskIdList){
            try{
                taskMessage = taskMessageService.getByid(taskId);
                if(taskMessage==null||taskMessage.getStatus()!= Constants.TaskMessageStatus.DOWNLOADED){
                    continue;
                }
                //判断文件是否存在，进行转码处理
                MediaFile mediaFile = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId());
                String localPath = mediaFileRootPath+ File.separator+"tasktemp"+File.separator+taskMessage.getType();
                String fileName = mediaFile.getFileName();
                filePath = localPath+File.separator+fileName;
                file = new File(filePath);
                if(!file.exists()){
                    throw new Exception("下载的文件不存在，无法进行转码。mediaFile="+mediaFile.getFileId());
                }
                //转码处理中
                //进行下载
                taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEGING);
                taskMessageService.addOrUpdate(taskMessage);

                //转码完成
                taskMessage.setStatus(Constants.TaskMessageStatus.FFMPEGED);
                taskMessageService.addOrUpdate(taskMessage);
                ///转码完成。新增视频文件数据

                //发送回调数据给第三方
                taskMessage.setStatus(Constants.TaskMessageStatus.CALLBACKING);
                taskMessageService.addOrUpdate(taskMessage);


                //完成转码流程
                taskMessage.setStatus(Constants.TaskMessageStatus.DULED);
                taskMessageService.addOrUpdate(taskMessage);

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
}
