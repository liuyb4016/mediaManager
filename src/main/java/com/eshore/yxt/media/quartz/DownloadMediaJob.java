package com.eshore.yxt.media.quartz;

import com.eshore.yxt.media.service.media.impl.MediaFileSerivceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DownloadMediaJob {

    public static final Logger logger = LoggerFactory.getLogger(DownloadMediaJob.class);

    @Scheduled(cron = "0/1 * *  * * ? ")//每隔1秒隔行一次
    public void run(){
        logger.info("Hello DownloadMediaJob  "+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
    }

}
