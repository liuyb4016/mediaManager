package com.eshore.yxt.media.web.mdeia.req;

public class MediaFileReq {

    private Long id;//id
    private Long notId;//判断使用
    private String title;//名称
    private Long sourceId;//源文件id 0本身
    private Integer type;//文件类型 0源文件   1标清  2高清

    public Long getNotId() {
        return notId;
    }

    public void setNotId(Long notId) {
        this.notId = notId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
