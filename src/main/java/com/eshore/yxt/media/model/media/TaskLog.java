package com.eshore.yxt.media.model.media;

import com.eshore.yxt.media.core.mvc.json.JsonDateSerializer;
import com.eshore.yxt.media.model.base.AutoModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * @DESCRIPTION:
 *      任务 日志
 * @PACKAGE: com.eshore.yxt.media.model.media
 * @CLASSNAME: TaskLog
 * ---------------------------------------
 * @AUTHOR: liuyb
 * @CREATEDATE: 2017/7/27
 * @CREATETIME: 11:49
 */
@Entity
@Table(name = "task_log")
public class TaskLog extends AutoModel implements Serializable {


    private static final long serialVersionUID = -7047121137906466237L;

    @Column(name = "task_id")
    private String taskId;//任务ID   对应多个文件id列表

    @Column(name = "dule_log")
    private String duleLog;//处理结果描述

    @Column(name = "status")
    private Integer status;//0失败   1成功

    @JsonSerialize(using=JsonDateSerializer.class)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime; //

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDuleLog() {
        return duleLog;
    }

    public void setDuleLog(String duleLog) {
        this.duleLog = duleLog;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
