package com.eshore.yxt.media.web.system;

import com.eshore.yxt.media.service.system.RoleService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eshore.yxt.media.model.system.Role;
import com.eshore.yxt.media.web.base.Pager;

@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	
	/**
	 * 角色管理页面
	 * @return
	 */
	@RequestMapping(value = "/listRole")
	public String manager() {
		return "system/role/listRole";
	}
	
	/**
	 * 添加角色页面
	 * @return
	 */
	@RequestMapping(value = "/addRole")
	public String addRole() {
		return "system/role/addRole";
	}
	
	/**
	 * 更新角色页面
	 * @return
	 */
	@RequestMapping(value = "/updateRole")
	public String updateRole(long id,Model model) {
		Role role = roleService.findRoleById(id);
		model.addAttribute("role", role);
		return "system/role/updateRole";
	}
	
	/**
	 * 授权角色页面
	 * @return
	 */
	@RequestMapping(value = "/grantRole")
	public String grantRole(long id,Model model) {
		String ids = roleService.getPermissionIds(id);
		Role role = roleService.findRoleById(id);
		model.addAttribute("role", role);
		model.addAttribute("ids", ids);
		return "system/role/grantRole";
	}
	
	
	/**
	 * 分页显示角色
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Grid list(Pager pager) {
		return roleService.findAllByPager(pager);
	}
	
	/**
	 * 保存新增角色
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Result add(Role role) {
		boolean b = roleService.save(role);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
		}else {
			result.setSuccess("0");
		}
		return result;
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Result delete(long id) {
		return roleService.delete(id);
	}
	
	/**
	 * 更新角色
	 * @param permission
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Result update(Role role) {
		boolean b = roleService.updateRole(role);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
			result.setMsg("更新角色成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("更新角色失败！");
		}
		return result;
	}
	
	/**
	 * 授权角色
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/grant")
	@ResponseBody
	public Result grant(long id,String ids) {
		boolean b = roleService.grantPermission(id,ids);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
			result.setMsg("授权成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("授权失败！");
		}
		return result;
	}
	
	
}
