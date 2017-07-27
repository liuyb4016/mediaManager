package com.eshore.yxt.media.model.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.eshore.yxt.media.core.mvc.json.JsonDTimeSerializer;
import com.eshore.yxt.media.model.base.AutoModel;

/**
 * 描述:系统配置实体
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@Entity
@Table(name="wx_sysconfig")
public class SysConfig  extends AutoModel implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String name;   //名称
	private String type;   //类型
	private String dkey;   //键
	private String value;  //键值
	private String remark; //备注
	
	@JsonSerialize(using=JsonDTimeSerializer.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date createTime; //创建时间
	
	@JsonSerialize(using=JsonDTimeSerializer.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date updateTime; //更新时间
	
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the dkey
	 */
	public String getDkey() {
		return dkey;
	}
	/**
	 * @param dkey the dkey to set
	 */
	public void setDkey(String dkey) {
		this.dkey = dkey;
	}
	
}
