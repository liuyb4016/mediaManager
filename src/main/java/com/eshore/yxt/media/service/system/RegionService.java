package com.eshore.yxt.media.service.system;

import java.util.List;

import com.eshore.yxt.media.model.system.Region;

public interface RegionService {

	/**
	 * 获取省份列表
	 * @return
	 */
	public List<Region> findProv();
	
	/**
	 * 获取城市列表
	 */
	public List<Region> findCity();
	
	/**
	 * 根据省份编码获取地市列表
	 * @param pCode
	 * @return
	 */
	public List<Region> findCityByProvCode(String pCode);
	
	
}
