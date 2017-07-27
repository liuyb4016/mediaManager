/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：CinemaReq.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年12月30日	Create		
 */
package com.eshore.yxt.media.web.system.req;

/**
 * 描述:影院查询条件实体
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public class CinemaReq {
	private String provCode;
	private String cityCode;
	private String status;
	
	/**
	 * @return the provCode
	 */
	public String getProvCode() {
		return provCode;
	}
	/**
	 * @param provCode the provCode to set
	 */
	public void setProvCode(String provCode) {
		this.provCode = provCode;
	}
	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
}
