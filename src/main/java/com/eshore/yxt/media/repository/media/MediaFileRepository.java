package com.eshore.yxt.media.repository.media;

import java.util.List;

import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface MediaFileRepository extends CrudRepository<MediaFile, Long>,JpaSpecificationExecutor<MediaFile> {

    @Query("from MediaFile mediaFile where mediaFile.taskId = ?1")
    public MediaFile getMediaFileBTaskId(String taskId);
}

