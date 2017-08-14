package com.eshore.yxt.media.quartz;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskLogService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DownloadMediaJob_1 {

    public static final Logger logger = LoggerFactory.getLogger(DownloadMediaJob_1.class);
    private static AtomicInteger count = new AtomicInteger(0);//线程安全的计数变量

    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private MediaFileService mediaFileService;
    @Autowired
    private TaskLogService taskLogService;

    @Value("${yxt.ftp.downloadcount}")
    private Integer downloadCount;//控制单台任务数


    @Scheduled(cron = "1 0/1 *  * * ?")//每隔30分钟隔行一次
    public synchronized void run(){
        if ((count.get()) > downloadCount) {
            logger.error("定时任务运行失败。当前服务的定时下载任务数已经超过了"+downloadCount+"个。暂停增加线程数。");
            return;
        }
        int countV2 = count.incrementAndGet();// 自增1,返回更新值
        logger.info("定时任务运行成功。当前服务的定时下载任务数递增到："+countV2+"个。");
        //检测文件下载
        List<Long> taskIdList = taskMessageService.findIdListTaskMessageByStatus(Constants.TaskMessageStatus.NO_DULE);
        File file = null;
        for(Long taskId:taskIdList){
            taskMessageService.duleDownload(taskId);
        }
        int countV =count.decrementAndGet();// 自减1,返回更新值
        logger.info("定时任务运行结束。当前服务的定时下载任务数递减到："+countV+"个。");
    }


}
