/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：SysLogController.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年9月26日	Create		
 */
package com.eshore.yxt.media.web.system;

import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.system.req.SysLogReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eshore.yxt.media.service.system.SysLogService;
import com.eshore.yxt.media.web.base.Pager;

/**
 * 描述:日志描述控制层
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@Controller
@RequestMapping(value="/syslog")
public class SysLogController extends BaseController {
	
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 描述：日志列表界面 
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	@RequestMapping("/listLog")
	public String listLog() {
		return "system/syslog/listLog";
	}
	
	/**
	 * 描述：日志记录查询
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param sysLogReq
	 * @param pager
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Grid list(SysLogReq sysLogReq, Pager pager) {
		return sysLogService.qrySysLogByPager(sysLogReq,pager);
	}
	
}
