package com.eshore.yxt.media.repository.system;

import java.util.List;

import com.eshore.yxt.media.model.system.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Long>{
	
	
		
	/**
	 * 获取数据库全部权限
	 * @return
	 */
	public List<Permission> findAll();
	
	/**
	 * 根据父节点ID查询子节点
	 * @param pid
	 * @return
	 */
	public List<Permission> findByParentId(Long pid);
	
}
