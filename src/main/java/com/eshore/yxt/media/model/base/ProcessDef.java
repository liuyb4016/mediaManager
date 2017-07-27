/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：ProcessDef.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年8月26日	Create		
 */
package com.eshore.yxt.media.model.base;

/**
 * 描述:流程定义实体
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public class ProcessDef {
	private int id;
	private String processName;
	private String roleKey;
	
	
	/**
	 * @param id
	 * @param processName
	 * @param roleKey
	 */
	public ProcessDef(int id, String processName, String roleKey) {
		super();
		this.id = id;
		this.processName = processName;
		this.roleKey = roleKey;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	
}
