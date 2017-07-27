package com.eshore.yxt.media.service.system;

import java.util.List;

import com.eshore.yxt.media.model.system.Role;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;

public interface RoleService {
	
	/**
	 * 分页查询角色列表(数据实体Grid)
	 * @param pager
	 * @return
	 */
	public Grid findAllByPager(Pager pager);
	
	/**
	 * 保存新增角色与更新角色
	 * @param role
	 * @return
	 */
	public boolean save(Role role);
	
	/**
	 * 根据ID删除角色
	 * @param id
	 */
	public Result delete(long id);
	
	/**
	 * 根据ID查询角色
	 */
	public Role findRoleById(long id);
	
	/**
	 * 根据角色ID获取权限字符串集合（逗号分隔）
	 * @param id
	 * @return
	 */
	public String getPermissionIds(long id);

	/**
	 * 角色ID授权
	 * @param id
	 * @param ids
	 * @return
	 */
	public boolean grantPermission(long id, String ids);
	
	/**
	 * 获取全部角色
	 * @return
	 */
	public List<Role> findAll();
	
	/**
	 * 更新角色
	 * @param role
	 * @return
	 */
	public boolean updateRole(Role role);
}
