package com.eshore.yxt.media.web.system;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController{
	
	@RequestMapping(value="/index")
	public String index(Model model) {
		return "index";
	}
	
	@RequestMapping(value="/403")
	public String au403() {
		return "error/403";
	}
	
}
