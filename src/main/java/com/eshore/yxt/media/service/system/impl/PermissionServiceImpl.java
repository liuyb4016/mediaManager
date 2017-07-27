package com.eshore.yxt.media.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.eshore.yxt.media.core.shiro.filter.ShiroFilterChainManager;
import com.eshore.yxt.media.model.system.Permission;
import com.eshore.yxt.media.repository.system.RoleRepository;
import com.eshore.yxt.media.service.system.PermissionService;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.resp.PtreeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshore.yxt.media.model.system.Role;
import com.eshore.yxt.media.repository.system.PermissionRepository;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionRepository permissionRepository;
	
	@Autowired
    RoleRepository roleRepository;
	
	@Autowired
    ShiroFilterChainManager shiroFilterChainManager;
	
	
	@PostConstruct
    public void initFilterChain() {
		shiroFilterChainManager.initFilterChains(permissionRepository.findAll());
    }


	public List<PtreeData> findAll() {
		List<Permission> permissions = permissionRepository.findAll();
		List<PtreeData> plist = new ArrayList<PtreeData>();
		//转换数据实体
		for(Permission permission : permissions) {
			PtreeData ptd = new PtreeData();
			ptd.setId(permission.getId());
			ptd.setText(permission.getName());
			ptd.setAction(permission.getAction());
			ptd.setType(permission.getType());
			ptd.setSeq(String.valueOf(permission.getSeq()));
			Permission parent = permission.getParent();
			if(parent != null) {
				ptd.setPid(permission.getParent().getId());
			}else {
				ptd.setPid(0);
			}
			ptd.setPkey(permission.getPkey());
			ptd.setRemark(permission.getRemark());
			plist.add(ptd);
		}
		
		if(plist.size() == 0) {
			return null;
		}
		return plist;
		
//		if(permissions.size() == 0) {
//			return null;
//		}
//		return permissions;
	}


	public boolean save(Permission permission) {
		permission.setCreateTime(new Date());
		permission.setUpdateTime(new Date());
		
		Permission p = permissionRepository.save(permission);
		
		//超级管理员角色拥有新增权限
		Role role = roleRepository.findOne(Long.valueOf(1));
		role.getPermissions().add(p);
		
		if(p == null) {
			return false;
		}
		return true;
	}


	public Result delete(long id) {
		Result result = new Result();
		Permission permission = permissionRepository.findOne(id);
		if(permission.getChildren().size() != 0) {
			 result.setSuccess("0");
			 result.setMsg("该权限拥有子权限！禁止删除！");
		}else {
			permissionRepository.delete(id);
			result.setSuccess("1");
			 result.setMsg("删除权限成功！");
		}
		return result;
	}


	public Permission findPermissionById(long id) {
		return permissionRepository.findOne(id);
	}


	@Override
	public boolean update(Permission permission) {
		permission.setUpdateTime(new Date());
		if(permissionRepository.save(permission) != null) {
			return true;
		}
		return false;
	}


	@Override
	public List<PtreeData> findPermissionByParentId(long pid) {
		List<Permission> permissions = permissionRepository.findByParentId(pid);
		List<PtreeData> plist = new ArrayList<PtreeData>();
		//转换数据实体
		for(Permission permission : permissions) {
			PtreeData ptd = new PtreeData();
			ptd.setId(permission.getId());
			ptd.setText(permission.getName());
			ptd.setAction(permission.getAction());
			ptd.setType(permission.getType());
			ptd.setSeq(String.valueOf(permission.getSeq()));
			Permission parent = permission.getParent();
			if(parent != null) {
				ptd.setPid(permission.getParent().getId());
			}else {
				ptd.setPid(0);
			}
			if(permission.getChildren().size() != 0) {
				ptd.setState("closed");
			}else {
				ptd.setState("open");
			}
			
			ptd.setPkey(permission.getPkey());
			ptd.setRemark(permission.getRemark());
			plist.add(ptd);
		}
		
		if(plist.size() == 0) {
			return null;
		}
		return plist;
	}
	
	
	
	
	
}
