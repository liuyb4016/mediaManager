package com.eshore.yxt.media.web.mdeia.resp;

import java.io.Serializable;
import java.util.List;

public class TaskMessageResq implements Serializable {


    private static final long serialVersionUID = 572899711532001517L;

    private String type;//数据id  1 院线通片花

    private String fileId;//文件ID

    private String status;//0 成功  -1 失败

    private String errorMsg;// 错误信息

    private List<String> videoUrls;//视频地址

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(List<String> videoUrls) {
        this.videoUrls = videoUrls;
    }
}
