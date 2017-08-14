package com.eshore.yxt.media.service.media;

import com.eshore.yxt.media.model.media.TaskLog;
import com.eshore.yxt.media.model.media.TaskMessage;

import java.util.List;


public interface TaskLogService {

    TaskLog addLog(String taskId, Integer status,String duleLog);

}
