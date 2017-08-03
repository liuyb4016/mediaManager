package com.eshore.yxt.media.web.mdeia.req;

import javax.persistence.Column;
import java.io.Serializable;

public class TaskMessageReq implements Serializable {

    private static final long serialVersionUID = -7200545272744477970L;

    private String type;//数据id  1 院线通片花

    private String fileId;//文件ID

    private String videoId;//视频ID

    private String videoName;//视频名称

    private Long videoSize;//视频大小

    private String videoMd5;//视频md5

    private Integer urlType;//视频下载地址  1 :ftp 相对路径   2: http://
    private String videoUrl;//视频下载路径

    private String callbackUrl;//处理结果回调  提供一个接收结果的接口

    public Integer getUrlType() {
        return urlType;
    }

    public void setUrlType(Integer urlType) {
        this.urlType = urlType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
