package com.eshore.yxt.media.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eshore.yxt.media.model.system.Permission;
import com.eshore.yxt.media.model.system.Role;
import com.eshore.yxt.media.repository.system.RoleRepository;
import com.eshore.yxt.media.service.system.RoleService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;

@Service
public class RoleServiceImpl implements RoleService  {
	
	@Autowired
	RoleRepository roleRepository;
	
	
	public Grid findAllByPager(Pager pager) {
		Pageable pageable = new PageRequest(pager.getPage()-1, pager.getRows());
		Page page = roleRepository.findAll(pageable);
		Grid grid = new Grid();
		grid.setTotal(page.getTotalElements());
		
		List<Role> roles =  new ArrayList<Role>();
		
		
//		for(Object c : page.getContent()) {
//			Role ct = (Role)c;
//			Role r = new Role();
//			r.setId(ct.getId());
//			r.setRoleName(ct.getRoleName());
//			r.setRemark(ct.getRemark());
//			roles.add(r);
//		}
		
//		grid.setRows(roles);
		grid.setRows(page.getContent());
		return grid;
	}


	public boolean save(Role role) {
		role.setCreateTime(new Date());
		role.setUpdateTime(new Date());
		Role r = roleRepository.save(role);
		if(r == null) {
			return false;
		}
		return true;
	}


	public Result delete(long id) {
		Result result = new Result();
		if(id == 1) {
			result.setSuccess("0");
			result.setMsg("不能删除超级管理员！");
		}
		
		Role role = roleRepository.findOne(id);
		if(role.getUsers()!=null && role.getUsers().size() != 0) {
			result.setSuccess("0");
			result.setMsg("该角色关联着用户！不能删除！");
			
		}else {
			roleRepository.delete(id);
			result.setSuccess("1");
			result.setMsg("删除角色成功！");
		}
		return result;
	}


	public Role findRoleById(long id) {
		return roleRepository.findOne(id);
	}


	public String getPermissionIds(long id) {
		StringBuffer sb = new StringBuffer();
		Role role = roleRepository.findOne(id);
		Set<Permission> permissions = role.getPermissions();
		
		for(Permission permission : permissions) {
			sb.append(permission.getId()+",");
		}
		return sb.toString();
	}


	public boolean grantPermission(long id, String ids) {
		Role role = roleRepository.findOne(id);
		String[] sids = StringUtils.split(ids,',');
		Set<Permission> permissions = new HashSet<Permission>();
		for(String s : sids) {
			Permission p = new Permission();
			p.setId(Long.valueOf(s));
			permissions.add(p);
		}
		role.setPermissions(permissions);
		role = roleRepository.save(role);
		if(role != null) {
			return true;
		}
		return false;
	}


	public List<Role> findAll() {
		return (List<Role>) roleRepository.findAll();
	}


	public boolean updateRole(Role role) {
		Role r = roleRepository.findOne(role.getId());
		role.setPermissions(r.getPermissions());
		if(roleRepository.save(role) != null) {
			return true;
		}
		return false;
	}
	
	
}
