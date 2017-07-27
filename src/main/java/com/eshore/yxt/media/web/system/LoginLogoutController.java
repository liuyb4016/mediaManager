package com.eshore.yxt.media.web.system;

import javax.servlet.http.HttpServletRequest;

import com.eshore.yxt.media.service.system.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eshore.yxt.media.model.system.User;
import com.eshore.yxt.media.web.base.Result;

@Controller
public class LoginLogoutController extends BaseController{

	private static final Log log = LogFactory
			.getLog(LoginLogoutController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 用户请求登录页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request) {
		return "login";
	}

	/**
	 * 用户提交表单登录
	 * 
	 * @param currUser
	 * @return
	 */
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	public Result login(User currUser, Model model, HttpServletRequest req) {
		Result result = new Result();
		result.setMsg("登录成功！");
		result.setSuccess("1");
		String exceptionClassName = (String) req
				.getAttribute("shiroLoginFailure");
		
		if(StringUtils.isEmpty(exceptionClassName)) {
			return result;
		}
		String error = null;
		if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
			error = "用户名/密码错误";
		} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
			error = "用户名/密码错误";
		} else if(LockedAccountException.class.getName().equals(exceptionClassName)) {
			error = "账户被冻结！请联系管理员！";
		}else if(ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
			error = "登录尝试五次错误！系统锁定十分钟！";
		}else if (exceptionClassName != null) {
			error = "其他错误：" + exceptionClassName;
		}
		
		result.setMsg(error);
		result.setSuccess("0");
		return result;
		//model.addAttribute("error", error);
		//return "login";

		// Subject user = SecurityUtils.getSubject();
		// UsernamePasswordToken token = new UsernamePasswordToken(
		// currUser.getTelephone(), currUser.getPassword());
		// token.setRememberMe(true);
		//
		// try {
		// user.login(token);
		// return "redirect:/index";
		// } catch (AuthenticationException e) {
		// log.error("登录失败错误信息:" + e);
		// token.clear();
		// return "redirect:/login";
		// }
	}

	/**
	 * 用户注销
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		Subject user = SecurityUtils.getSubject();
		user.logout();
		return "redirect:index.do";
	}
	
	
	/**
	 * 描述： 
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	@RequestMapping(value = "/forward")
	public String forward() {
		return "forward";
	}
	
	@RequestMapping(value="/test")
	public String test() {
		return "login";
	}
}
