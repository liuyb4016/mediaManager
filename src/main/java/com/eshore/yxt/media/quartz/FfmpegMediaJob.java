package com.eshore.yxt.media.quartz;

import com.eshore.yxt.media.core.constants.Constants;
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

@Component
public class FfmpegMediaJob {

    public static final Logger logger = LoggerFactory.getLogger(FfmpegMediaJob.class);

    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private MediaFileService mediaFileService;

    @Value("root.file.path")
    private String mediaFileRootPath;

    @Scheduled(cron = "1 0/30 *  * * ? ")//每隔1秒隔行一次
    public void run(){
        //获得已下载完成的列表
        List<Long> taskIdList = taskMessageService.findIdListTaskMessageByStatus(Constants.TaskMessageStatus.DOWNLOADED);
        File file = null;
        //循环处理
        for(Long taskId:taskIdList){
            TaskMessage taskMessage = taskMessageService.getByid(taskId);
            if(taskMessage!=null&&taskMessage.getStatus()== Constants.TaskMessageStatus.DOWNLOADED){

            }
        }
        //判断文件是否存在，进行转码处理
        //转码处理中


        //转码完成。新增视频文件数据

        //转码完成
        //发送回调数据给第三方

        //完成转码流程
    }
}
