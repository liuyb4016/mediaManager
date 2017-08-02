package com.eshore.yxt.media.repository.media;

import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface TaskMessageRepository extends CrudRepository<TaskMessage, Long>,JpaSpecificationExecutor<TaskMessage> {

    @Query("from TaskMessage taskMessage where taskMessage.type = ?1 and taskMessage.fileId=?2 ")
    public TaskMessage getTaskMessageByFileId(String type,String fileId);
}

