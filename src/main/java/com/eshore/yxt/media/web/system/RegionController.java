/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：RegionController.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年8月18日	Create		
 */
package com.eshore.yxt.media.web.system;

import java.util.List;

import com.eshore.yxt.media.model.system.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eshore.yxt.media.service.system.RegionService;

/**
 * 描述:省市地域列表Controllder
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@Controller
@RequestMapping("/region")
public class RegionController extends BaseController {
	
	@Autowired
	private RegionService regionService;
	
	/**
	 * 描述：获取所有省份列表  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	@RequestMapping("/getAllProv")
	@ResponseBody
	public List<Region> getAllProv() {
		return regionService.findProv();
	}
	
	
	/**
	 * 描述：获取所有的城市列表
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	@RequestMapping("/getAllCity")
	@ResponseBody
	public List<Region> getAllCity() {
		return regionService.findCity();
	}
	
	/**
	 * 描述：根据省份Code查询城市列表
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param pCode
	 * @return
	 */
	@RequestMapping("/getCityByProvCode")
	@ResponseBody
	public List<Region> getCityByProvCode(String pCode) {
		return regionService.findCityByProvCode(pCode);
	}
	
}
