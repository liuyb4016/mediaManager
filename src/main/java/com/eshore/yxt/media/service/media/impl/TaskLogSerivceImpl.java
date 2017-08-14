package com.eshore.yxt.media.service.media.impl;

import com.eshore.yxt.media.model.media.TaskLog;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.repository.media.TaskLogRepository;
import com.eshore.yxt.media.repository.media.TaskMessageRepository;
import com.eshore.yxt.media.service.media.TaskLogService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskLogSerivceImpl implements TaskLogService {

    public static final Logger logger = LoggerFactory.getLogger(TaskLogSerivceImpl.class);

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Override
    public TaskLog addLog(String taskId,Integer status, String duleLog) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setDuleLog(duleLog);
        taskLog.setStatus(status);
        taskLog.setCreateTime(new Date());
        return taskLogRepository.save(taskLog);
    }

}
