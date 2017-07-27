package com.eshore.yxt.media.web.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eshore.yxt.media.service.system.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 描述：系统配置控制层
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@RequestMapping("/sysConfig")
@Controller
public class SysConfigController {
	
	@Autowired
	private SysConfigService sysConfigService;
	
	/**
	 * 描述：方法说明  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateSysConfig")
	public String updateSysConfig(HttpServletRequest request) {
		Map p = request.getParameterMap();
		sysConfigService.updateSysConfig(p);
		return "forward:/stock/config.do";  
	}
	
}
