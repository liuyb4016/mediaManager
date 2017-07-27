package com.eshore.yxt.media.model.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eshore.yxt.media.core.mvc.json.JsonDateSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.eshore.yxt.media.model.base.AutoModel;

@Entity
@Table(name = "wx_user")
public class User extends AutoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "username")
	private String username; // 管理员真实姓名

	@Column(name = "telephone")
	private String telephone; // 该管理员的手机号码（登录使用）
	
	@JsonIgnore   
	@Column(name = "salt")
	private String salt; // 加密使用的盐

	@Column(name = "email")
	private String email; // 该管理员使用的邮箱

	@JsonIgnore
	@Column(name = "password")
	private String password; // 用于登录时填写的密码（MD5加密）

	@Column(name = "provcode")
	private String provCode; // 省份编码

	@Column(name = "provname")
	private String provName; // 省份名称

	@Column(name = "citycode")
	private String cityCode; // 城市编码

	@Column(name = "cityname")
	private String cityName; // 城市名称

	@JsonSerialize(using=JsonDateSerializer.class)
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	@Column(name = "createtime")
	private Date createTime; // 创建时间

	@JsonSerialize(using=JsonDateSerializer.class) 
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	@Column(name = "updatetime")
	private Date updateTime; // 更新时间

	@Column(name = "locked")
	private String locked; // 锁定用户 0：开启 1：锁定
	
	@Column(name="notifyDate")
	private Date notifyDate;  //库存告警提醒日期
	
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role; // 用户拥有的角色（多对一的关系）

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getProvCode() {
		return provCode;
	}

	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	/**
	 * @return the notifyDate
	 */
	public Date getNotifyDate() {
		return notifyDate;
	}

	/**
	 * @param notifyDate the notifyDate to set
	 */
	public void setNotifyDate(Date notifyDate) {
		this.notifyDate = notifyDate;
	}
}
