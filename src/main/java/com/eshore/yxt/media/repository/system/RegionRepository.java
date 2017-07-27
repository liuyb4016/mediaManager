package com.eshore.yxt.media.repository.system;

import java.util.List;

import com.eshore.yxt.media.model.system.Region;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface RegionRepository extends Repository<Region, String>{
	
	
	/**
	 * 根据上级编码查询子级编码
	 * @param pcode
	 * @return
	 */
	@Query(" from Region region where region.pcode = ?1 order by region.code")
	public List<Region> findByPcode(String pcode);
	
	/**
	 * 获取城市节点
	 * @param pcode  "00"
	 * @return
	 */
	public List<Region> findByPcodeNot(String pcode);
}
