package com.eshore.yxt.media.web.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.eshore.yxt.media.model.system.Region;
import com.eshore.yxt.media.model.system.User;
import com.eshore.yxt.media.service.system.RegionService;
import com.eshore.yxt.media.service.system.RoleService;
import com.eshore.yxt.media.service.system.UserService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.req.UserReq;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eshore.yxt.media.model.system.Role;
import com.eshore.yxt.media.web.base.Tree;

@Controller
@RequestMapping(value="/user")
public class UserController extends BaseController{
	

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	private RoleService roleService;
	
	/**
	 * 用户管理页面
	 * @return
	 */
	@RequestMapping(value = "/listUser")
	public String listUser(Model model) {
		String telephone = (String)SecurityUtils.getSubject().getPrincipal();
		User user= userService.getUserBytelephone(telephone);
		model.addAttribute("userId", user.getId());
		
		return "system/user/listUser";
	}
	
	/**
	 * 新增用户页面
	 * @return
	 */
	@RequestMapping(value = "/addUser")
	public String addUser(Model model) {
		List<Region> regions = regionService.findProv();
		List<Region> regions2 = regionService.findCityByProvCode(regions.get(0).getCode());
		List<Role> roles = roleService.findAll();
		
		model.addAttribute("regions", regions);
		model.addAttribute("regions2", regions2);
		model.addAttribute("roles", roles);
		
		return "system/user/addUser";
	}
	
	/**
	 * 更新用户页面
	 * @return
	 */
	@RequestMapping(value = "/updateUser")
	public String updateUser(long id,Model model) {
		User user= userService.findUserById(id);
		List<Role> roles = roleService.findAll();
		List<Region> regions = regionService.findProv();
		List<Region> regions2 = regionService.findCityByProvCode(user.getProvCode());
		model.addAttribute("regions", regions);
		model.addAttribute("regions2", regions2);
		model.addAttribute("user", user);
		model.addAttribute("roles", roles);
		return "system/user/updateUser";
	}
	
	/**
	 * 获取登录用户所有的权限
	 * @return
	 */
	@RequestMapping(value = "/tree")
	@ResponseBody
	public List<Tree> userPermissions() {
		Subject subject = SecurityUtils.getSubject();
		String tel =  (String)subject.getPrincipal();
		return userService.getPermissionByTelephone(tel);
	}
	
	
	
	/**
	 * 根据条件分页查询用户列表
	 * @param pager
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Grid list(Pager pager, UserReq user) {
		return userService.findAllByPager(pager, user);
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Result add(User user) {
		boolean b = userService.save(user);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
			result.setMsg("添加用户成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("添加用户失败！");
		}
		return result;
	}
	
	/**
	 * 根据用户ID删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Result delete(long id) {
		boolean b = userService.deleteByUserId(id);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
			result.setMsg("删除用户成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("删除用户失败！");
		}
		return result;
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Result update(User user,String password2) {
		boolean b = userService.update(user,password2);
		Result result = new Result();
		
		if(b) {
			result.setSuccess("1");
			result.setMsg("更新用户成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("更新用户失败！");
		}
		return result;
	}
	
	/**
	 * 根据ids批量删除用户
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/batchDelete")
	@ResponseBody
	public Result batchDelete(String ids) {
		boolean b = userService.batchDelete(ids);
		Result result = new Result();
		
		if(b) {
			result.setSuccess("1");
			result.setMsg("批量删除成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("批量删除失败！");
		}
		
		return result;
	}
	
	/**
	 * 级联获取地市列表
	 * @param pcode
	 * @return
	 */
	@RequestMapping(value = "/getRegion")
	@ResponseBody
	public List<Region> getRegion(String pcode) {
		return regionService.findCityByProvCode(pcode);
	}
	
	/**
	 * 冻结或者激活用户 
	 * @param id
	 * @param state   0:激活  1：冻结
	 * @return
	 */
	@RequestMapping(value = "/freezeOrActivate")
	@ResponseBody
	public Result freezeOrActivate(long id,String state) {
		boolean b = userService.freezeOrActivate(id, state);
		Result result = new Result();
		String prefix = "";
		if(state.equals("1")) {
			prefix = "冻结";
		}else if (state.equals("0")){
			prefix = "激活";
		}
		
		if(b) {
			result.setSuccess("1");
			result.setMsg(prefix+"成功！");			
		}else {
			result.setSuccess("0");
			result.setMsg(prefix+"失败！");
		}
		
		return result;
	}
	
	/**
	 * 根据ids批量冻结用户
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/batchFreeze")
	@ResponseBody
	public Result batchFreeze(String ids) {
		int count = userService.batchFreeze(ids);
		Result result = new Result();
		
		if(count > 0 ) {
			result.setSuccess("1");
			result.setMsg("批量冻结"+count+"个用户成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("批量冻结失败！");
		}
		
		return result;
	}
	
	/**
	 * 描述：异步验证手机号码的唯一性 
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @return
	 */
	@RequestMapping(value = "/validateTelphone")
	@ResponseBody
	public Result validateTelphone(String tel) {
		return userService.validateTelphone(tel);
	}
	
	
	/**
	 * 描述：批量导出用户信息 
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param ids
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/batchExport")
	public String batchExport(String ids,HttpServletResponse response) {
		response.setContentType("application/binary;charset=ISO8859_1");  
        try  
        {  
            ServletOutputStream outputStream = response.getOutputStream();  
            String fileName = new String(("用户报表").getBytes(), "ISO8859_1");  
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式  
            userService.batchExport(ids,outputStream);  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        
		return null;
	}
}
