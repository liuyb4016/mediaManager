package com.eshore.yxt.media.web.mdeia.resp;

import java.io.Serializable;
import java.util.List;

public class TaskMessageResq implements Serializable {

    private static final long serialVersionUID = 572899711532001517L;

    public TaskMessageResq(){
    }

    public TaskMessageResq(String taskId,String fileId,String type,String url){
        this.fileId = fileId;
        this.taskId = taskId;
        this.type = type;
        this.url = url;
    }

    private String fileId;//文件ID
    private String type;//类型  1 标清  2 高清
    private String url;// 链接
    private String status;//0 成功  -1 失败
    private String errorMsg;// 错误信息
    private String taskId;// 任务id

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
