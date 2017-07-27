/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：CustomFormAuthenticationFilter.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年9月5日	Create		
 */
package com.eshore.yxt.media.core.shiro.custom;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 描述:重写Form表单登录验证器
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	   @Override
	   protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
               ServletRequest request, ServletResponse response) throws Exception {
			return true;
	   }
}
