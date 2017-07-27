package com.eshore.yxt.media.service.system;

import java.util.List;
import java.util.Map;

import com.eshore.yxt.media.model.system.SysConfig;
import com.eshore.yxt.media.web.base.Result;


/**
 * 描述:系统配置服务层接口
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public interface SysConfigService {
	
	/**
	 * 描述：根据类型查找系统配置
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param type
	 * @return
	 */
	public List<SysConfig> findSysConfigByType(String type);

	/**
	 * 描述：更新系统配置值
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param p
	 */
	public Result updateSysConfig(Map p);
	
	
	
	/**
	 * 描述：根据Key值查找Value值  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param key
	 * @return
	 */
	public String getSysConfigByDkey(String key);
}
