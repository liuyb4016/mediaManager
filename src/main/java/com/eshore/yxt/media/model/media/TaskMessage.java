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
 *      流媒体文件
 * @PACKAGE: com.eshore.yxt.media.model.media
 * @CLASSNAME: TaskMessage
 * ---------------------------------------
 * @AUTHOR: liuyb
 * @CREATEDATE: 2017/7/27
 * @CREATETIME: 11:49
 */
@Entity
@Table(name = "task_message")
public class TaskMessage extends AutoModel implements Serializable {

    private static final long serialVersionUID = -7808456372349338255L;

    @Column(name = "task_id")
    private String taskId;//任务ID   对应多个文件id列表

    @Column(name = "type")
    private Integer type;//数据id  1 院线通片花

    @Column(name = "file_id")
    private String fileId;//文件ID

    @Column(name = "video_name")
    private String videoName;//视频名称

    @Column(name = "video_size")
    private Long videoSize;//视频大小

    @Column(name = "video_md5")
    private String videoMd5;//视频md5

    @Column(name = "video_url")
    private String videoUrl;//默认使用ftp的方式

    @Column(name = "status")
    private Integer status;//处理状态  0未处理 1 正在下载  2 已下载   3 正在转码处理 4 转码已处理     5 正在回调通知给第三方 6 已完成处理 -1 下载处理出错 -2 转码处理出错

    @Column(name = "callback_url")
    private String callbackUrl;//处理结果回调  提供一个接收结果的接口

    @JsonSerialize(using=JsonDateSerializer.class)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime; //

    @JsonSerialize(using=JsonDateSerializer.class)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;//更新时间

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(Long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoMd5() {
        return videoMd5;
    }

    public void setVideoMd5(String videoMd5) {
        this.videoMd5 = videoMd5;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
