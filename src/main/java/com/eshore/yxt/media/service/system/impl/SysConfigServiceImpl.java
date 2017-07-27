package com.eshore.yxt.media.service.system.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eshore.yxt.media.model.system.SysConfig;
import com.eshore.yxt.media.service.system.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshore.yxt.media.repository.system.SysConfigRepo;
import com.eshore.yxt.media.web.base.Result;

/**
 * 描述:系统配置服务类实体
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {
	
	@Autowired
	private SysConfigRepo sysConfigRepo;

	
	@Override
	public List<SysConfig> findSysConfigByType(String type) {
		return sysConfigRepo.findSysConfigByType(type);
	}


	@Override
	public Result updateSysConfig(Map p) {
		Result result = new Result();
		result.setMsg("更新系统配置成功！");
		Iterator<Entry<String,Object>> iter = p.entrySet().iterator();
		Entry<String,Object> entry = null;
		while(iter.hasNext()) {
			entry = iter.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			String tValue = "";
			
//			SysConfig sysConfig = new SysConfig();
//			sysConfig.setKey(key);
//			sysConfig.setUpdateTime(new Date());
			if(value instanceof String[]) {
				String [] values = (String[])value;
				for(String temp : values) {
					tValue = tValue+temp + ",";
				}
				if(StringUtils.isNotBlank(tValue)) {
					tValue = tValue.substring(0,tValue.length()-1);
				}
			}else {
				tValue = (String)value;
			}
			
//			sysConfig.setValue(tValue);
			sysConfigRepo.updateSysConfig(key,tValue);
		}
		return result;
	}


	@Override
	public String getSysConfigByDkey(String key) {
		return sysConfigRepo.getSysConfigByDkey(key);
	}
	
	
}
