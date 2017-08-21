package com.eshore.yxt.media.repository.media;

import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;


public interface TaskMessageRepository extends CrudRepository<TaskMessage, Long>,JpaSpecificationExecutor<TaskMessage> {

    @Query("select taskMessage from TaskMessage taskMessage where taskMessage.type = ?1 and taskMessage.fileId=?2 ")
    public TaskMessage getTaskMessageByFileId(Integer type,String fileId);

    @Query("select taskMessage from TaskMessage taskMessage where taskMessage.status=?1 ")
    public List<TaskMessage> findListTaskMessageByStatus(Integer status);

    @Query("SELECT taskMessage.id from TaskMessage taskMessage where taskMessage.status=?1 ")
    public List<Long> findIdListTaskMessageByStatus(Integer status);

    @Query("select taskMessage from TaskMessage taskMessage where taskMessage.taskId = ?1 ")
    public TaskMessage getTaskMessageByTaskId(String taskId);

}

