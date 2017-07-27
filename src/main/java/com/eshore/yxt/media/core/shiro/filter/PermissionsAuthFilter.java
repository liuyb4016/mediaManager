/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：PermissionsAuthFilter.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年8月23日	Create		
 */
package com.eshore.yxt.media.core.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 描述：重写权限角色拦截器
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public class PermissionsAuthFilter extends AuthorizationFilter {

	/* (non-Javadoc)
	 * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	 public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

//        boolean isPermitted = true;  
//        if (perms != null && perms.length > 0) {
//            if (perms.length == 1) {
//                if (!subject.isPermitted(perms[0])) {
//                    isPermitted = false;
//                }
//            } else {
//                if (!subject.isPermittedAll(perms)) {
//                    isPermitted = false;
//                }
//            }
//        }else {
//        	isPermitted = false;    
//        }
        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
        	isPermitted = true;
        }else {
        	isPermitted = false;    
        }
        
        return isPermitted;
    }

}
