/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：SysLogService.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年9月22日	Create		
 */
package com.eshore.yxt.media.service.system;

import com.eshore.yxt.media.model.system.SysLog;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.system.req.SysLogReq;

/**
 * 描述：系统日志操作服务类接口
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public interface SysLogService {
	
	/**
	 * 描述：保存用户的系统操作日志
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param sysLog
	 */
	public void saveLog(SysLog sysLog);

	/**
	 * 描述：根据条件分页查询系统操作日志  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param sysLogReq
	 * @param pager
	 * @return
	 */
	public Grid qrySysLogByPager(SysLogReq sysLogReq, Pager pager);
	
	
	
}
