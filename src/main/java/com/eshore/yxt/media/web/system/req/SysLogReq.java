/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：SysLogReq.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年9月26日	Create		
 */
package com.eshore.yxt.media.web.system.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述：日志查询实体类
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public class SysLogReq {
	private Long userId;   //操作人Id
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startOpTime;   //开始操作时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endOpTime;     //结束操作时间

	private String beforeContent;  //操作前内容
	
	private String afterContent;   //操作后内容
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getBeforeContent() {
		return beforeContent;
	}

	public void setBeforeContent(String beforeContent) {
		this.beforeContent = beforeContent;
	}

	public String getAfterContent() {
		return afterContent;
	}

	public void setAfterContent(String afterContent) {
		this.afterContent = afterContent;
	}

	public Date getStartOpTime() {
		return startOpTime;
	}

	public void setStartOpTime(Date startOpTime) {
		this.startOpTime = startOpTime;
	}

	public Date getEndOpTime() {
		return endOpTime;
	}

	public void setEndOpTime(Date endOpTime) {
		this.endOpTime = endOpTime;
	}
}
