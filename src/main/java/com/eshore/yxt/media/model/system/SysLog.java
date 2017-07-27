/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：SysLog.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年9月22日	Create		
 */
package com.eshore.yxt.media.model.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.eshore.yxt.media.core.mvc.json.JsonDTimeSerializer;
import com.eshore.yxt.media.model.base.AutoModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * 描述:系统操作日志实体
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@Entity
@Table(name = "wx_syslog")
public class SysLog extends AutoModel implements Serializable {

	/**
	 * 变量说明：持久化变量
	 */
	private static final long serialVersionUID = 1L;
	
	private long userId;   //操作人ID
	
	@JsonSerialize(using=JsonDTimeSerializer.class)
	private Date operateTime;  //操作时间
	
	private String beforeContent;  //操作前内容
	
	private String afterContent;   //操作后内容
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
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
	
	
	
}
