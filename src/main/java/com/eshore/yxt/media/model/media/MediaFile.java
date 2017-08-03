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
 * @CLASSNAME: MediaFile
 * ---------------------------------------
 * @AUTHOR: liuyb
 * @CREATEDATE: 2017/7/27
 * @CREATETIME: 11:49
 */
@Entity
@Table(name = "media_file")
public class MediaFile extends AutoModel implements Serializable {

    private static final long serialVersionUID = -7717809130965680007L;

    @Column(name = "file_id")
    private String fileId;//文件ID

    @Column(name = "title")
    private String title;//名称

    @Column(name = "media_desc")
    private String mediaDesc;//描述

    @Column(name = "file_name")
	private String fileName;//文件名
	 
	@Column(name = "size")
	private Long size;//文件大小   bit

    @Column(name = "md5")
    private String md5;//md5值

    @Column(name = "type")
    private Integer type;//数据id  1 院线通片花

    @Column(name = "def_type")
    private Integer defType;//文件类型 0源文件   1标清  2高清

    @Column(name = "source_id")
    private Long sourceId;//源文件id 0本身

	@JsonSerialize(using=JsonDateSerializer.class) 
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "create_time")
	private Date createTime; // 

    @JsonSerialize(using=JsonDateSerializer.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name = "update_time")
	private Date updateTime;

    public Integer getDefType() {
        return defType;
    }

    public void setDefType(Integer defType) {
        this.defType = defType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaDesc() {
        return mediaDesc;
    }

    public void setMediaDesc(String mediaDesc) {
        this.mediaDesc = mediaDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
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
