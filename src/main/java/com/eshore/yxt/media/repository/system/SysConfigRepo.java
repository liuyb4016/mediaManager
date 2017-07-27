package com.eshore.yxt.media.repository.system;

import java.util.List;

import com.eshore.yxt.media.model.system.SysConfig;
import com.eshore.yxt.media.repository.BaseEntityManager;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 描述:系统配置Dao层
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public interface SysConfigRepo extends BaseEntityManager<SysConfig,Long> {

	
	/**
	 * 描述：根据类型查找系统配置信息
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param type
	 * @return
	 */
	@Query(" from SysConfig sysConfig where sysConfig.type = ?1 ")
	public List<SysConfig> findSysConfigByType(String type);

	/**
	 * 描述：根据Key值更新系统配置
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param key
	 * @param value
	 */
	@Modifying
	@Query(" update SysConfig sc set sc.value = ?2,updateTime=now() where sc.dkey = ?1 ")
	public int updateSysConfig(String key, String value);
	
	/**
	 * 描述：根据Key值查找Value值  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	@Query( " select sc.value from SysConfig sc where sc.dkey = ?1 ")
	public String getSysConfigByDkey(String key);
	
	@Query(" from SysConfig s where s.type = ? and s.dkey = ? ")
	public List<SysConfig> querySysconfigByTypeAndKey(String type, String dkey);
}
