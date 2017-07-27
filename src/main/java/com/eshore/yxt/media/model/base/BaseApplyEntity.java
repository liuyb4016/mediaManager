/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：BaseApplyEntity.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年8月26日	Create		
 */
package com.eshore.yxt.media.model.base;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.eshore.yxt.media.core.mvc.json.JsonDTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述:申请实体的基类（记录申请的状态）
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@MappedSuperclass
public class BaseApplyEntity {
	
	@Id
	@Column(name="id",columnDefinition="bigint COMMENT '主键ID'")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;                    //主键ID
	
	@Column(name="processId",columnDefinition="int COMMENT '当前所处节点的ID'")
	private int processId;
	
	@Column(name="processName",columnDefinition="varchar(255) COMMENT '当前所处节点的名称'")
	private String processName;          //当前所处的节点（记录当前所处节点的名称）
	
	@Column(name="roleKey",columnDefinition="varchar(255) COMMENT '当前节点的处理角色'")
	private String roleKey;             //当前所处节点的处理角色
	
	@Column(name="prevReason",columnDefinition="varchar(255) COMMENT '上一级审批的理由'")
	private String prevReason;          //上一级审批的理由
	
	@Column(name="userId",columnDefinition="bigint COMMENT '申请人ID'")
	private Long userId;                
	
	@JsonSerialize(using=JsonDTimeSerializer.class)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	@Column(name="applyTime",columnDefinition="datetime COMMENT '申请时间'")
	private Date applyTime;
	
	@Column(name="state")
	private String state = "1";    //申请状态    1：表示正常    -1：表示逻辑删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public String getPrevReason() {
		return prevReason;
	}

	public void setPrevReason(String prevReason) {
		this.prevReason = prevReason;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}


}
