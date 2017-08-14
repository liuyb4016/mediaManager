package com.eshore.yxt.media.repository.media;

import com.eshore.yxt.media.model.media.TaskLog;
import com.eshore.yxt.media.model.media.TaskMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TaskLogRepository extends CrudRepository<TaskLog, Long>,JpaSpecificationExecutor<TaskLog> {


}

