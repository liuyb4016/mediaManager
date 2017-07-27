package com.eshore.yxt.media.web.system.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserReq {
	private String username;
	private String telephone;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTimeStart;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTimeEnd;
	
	
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
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
	public Date getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	
}
