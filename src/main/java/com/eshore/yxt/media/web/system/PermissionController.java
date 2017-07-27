package com.eshore.yxt.media.web.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eshore.yxt.media.model.system.Permission;
import com.eshore.yxt.media.service.system.PermissionService;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.resp.PtreeData;

@Controller
@RequestMapping(value="/permission")
public class PermissionController extends BaseController{


	
	@Autowired
	PermissionService permissionService;
	
	/**
	 * 权限管理页面
	 * @return
	 */
	@RequestMapping(value = "/listPermission")
	public String manager() {
		return "system/permission/listPermission";
	}
	/**
	 * 添加权限页面
	 * @return
	 */
	@RequestMapping(value = "/addPermission")
	public String addPermission() {
		return "system/permission/addPermission";
	}
	/**
	 * 更新权限页面
	 */
	@RequestMapping(value = "/updatePermission")
	public String updatePermission(long id,Model model) {
		Permission p = permissionService.findPermissionById(id);
		model.addAttribute("permission",p);
		return "system/permission/updatePermission";
	}
	
	
	
	/**
	 * 请求全部权限列表
	 * @return
	 */
	@RequestMapping(value = "/listAll")
	@ResponseBody
	public List<PtreeData> listAll() {
		return permissionService.findAll();
	}
	
	/**
	 * 根据父ID查找子节点
	 * @param pid
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public List<PtreeData> list(long pid) {
		return permissionService.findPermissionByParentId(pid);
	}
	
	/**
	 * 保存新增权限
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Result add(Permission permission) {
		boolean b = permissionService.save(permission);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
		}else {
			result.setSuccess("0");
		}
		
		return result;
	}
	
	/**
	 * 删除权限
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Result delete(long id) {
		return permissionService.delete(id);
	}
	
	/**
	 * 更新权限
	 * @param permission
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Result update(Permission permission) {
		boolean b = permissionService.update(permission);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
		}else {
			result.setSuccess("0");
		}
		
		return result;
	}
	
}
