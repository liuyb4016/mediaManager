/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：AbstractApply.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年8月31日	Create		
 */
package com.eshore.yxt.media.core.workflow;

import java.io.Serializable;
import java.util.Date;

import com.eshore.yxt.media.model.base.BaseApplyEntity;
import com.eshore.yxt.media.model.base.ProcessDef;
import com.eshore.yxt.media.model.system.User;
import com.eshore.yxt.media.web.base.Result;

/**
 * 描述:申请的服务基类
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public abstract class AbstractApply<T extends BaseApplyEntity,Pk extends Serializable>{
	
	/**
	 * 描述：提交申请 
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param t
	 * @param user
	 * @return
	 */
	public Result submit(T t,User user) {		
		//获取提交后的下一个节点信息
		ProcessDef pdf = getStartDef();
		
		//设置申请的当前节点信息
		t.setApplyTime(new Date());
		t.setUserId(user.getId());
		t.setProcessId(pdf.getId());
		t.setProcessName(pdf.getProcessName());
		t.setRoleKey(pdf.getRoleKey());
		
		return null;
		
	}
	
	/**
	 * 描述：重新申请  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	public Result reSubmit() {
		return null;
	}
	
	/**
	 * 
	 * 描述：获取申请的下一个节点信息
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	public abstract ProcessDef getStartDef();
	
	/**
	 * 
	 * 描述：获取重新申请的节点信息
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	public abstract ProcessDef getReSubmitDef();
	
	
	/**
	 * 描述：根据当前节点获取下一个节点信息
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param pdf
	 * @return
	 */
	public abstract ProcessDef getNextDef(ProcessDef pdf);
	
}
