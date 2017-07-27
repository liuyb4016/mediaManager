package com.eshore.yxt.media.service.system;

import java.util.List;

import com.eshore.yxt.media.model.system.Permission;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.resp.PtreeData;

public interface PermissionService {

	/**
	 * 查询全部资源
	 */
	public List<PtreeData> findAll();
	
	/**
	 * 保存权限
	 * @param permission
	 * @return
	 */
	public boolean save(Permission permission);
	
	/**
	 * 根据ID删除权限
	 * @param id
	 * @return 
	 */
	public Result delete(long id);
	
	/**
	 * 根据ID获取权限
	 * @param id
	 * @return
	 */
	public Permission findPermissionById(long id);
	
	/**
	 * 更新权限
	 * @param permission
	 * @return
	 */
	public boolean update(Permission permission);
	
	/**
	 * 根据父节点异步查询子节点
	 * @param pid
	 * @return
	 */
	public List<PtreeData> findPermissionByParentId(long pid);
}
