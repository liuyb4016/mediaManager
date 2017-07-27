package com.eshore.yxt.media.web.system;

import com.eshore.yxt.media.model.system.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

	public static final Logger logger  =  LoggerFactory.getLogger(BaseController.class);

	/**
	 * 绑定时间转换器
	 * 
	 * @param binder
	 */
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		SimpleDateFormat datetimeFormat = new SimpleDateFormat(
//				"yyyy-MM-dd HH:mm:ss");
//		datetimeFormat.setLenient(false);
//		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
//				datetimeFormat, true));
//	}

	/**
	 * 获取安全实体
	 * 
	 * @return
	 */
	public Subject getShiroSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获取Sessioon
	 * 
	 * @return
	 */
	public Session getSession() {
		Subject subject = SecurityUtils.getSubject();
		return subject.getSession();
	}

	/**
	 * 获取当前操作用户的ID
	 * 
	 * @return
	 */
	public long getCurrentUserId() {
		User user = (User) getSession().getAttribute("user");
		return user.getId();
	}

	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		return (User) getSession().getAttribute("user");
	}

	
	

	
}
