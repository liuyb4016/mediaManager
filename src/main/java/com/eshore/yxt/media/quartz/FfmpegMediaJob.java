package com.eshore.yxt.media.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FfmpegMediaJob {

    public static final Logger logger = LoggerFactory.getLogger(FfmpegMediaJob.class);

    @Scheduled(cron = "0/1 * *  * * ? ")//每隔1秒隔行一次
    public void run(){
        logger.info("Hello FfmpegMediaJob  "+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
    }
}
