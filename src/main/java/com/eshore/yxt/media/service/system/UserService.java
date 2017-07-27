package com.eshore.yxt.media.service.system;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import com.eshore.yxt.media.model.system.User;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.base.Tree;
import com.eshore.yxt.media.web.system.req.UserReq;

public interface UserService {
	
	/**
	 * 保存新管理员
	 * @param user
	 * @return
	 */
	public boolean save(User user);
	/**
	 * 根据登录手机号码查找用户
	 * @param username
	 * @return
	 */
	public User getUserBytelephone(String telephone);

	/**
	 * 根据登录手机号码获取用户所拥有的权限标识
	 * @param telephone
	 * @return
	 */
	public Set<String> getPermissionKeyByTelephone(String telephone);
	
	/**
	 * 根据登录手机号码获取用户拥有的全部权限
	 * @param telephone
	 * @return
	 */
	public List<Tree> getPermissionByTelephone(String telephone);
	
	/**
	 * 根据属性分页查询用户列表
	 * @param pager
	 * @return
	 */
	public Grid findAllByPager(Pager pager,UserReq user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @param password2   密码更新
	 * @return
	 */
	public boolean update(User user,String password2);
	
	
	/**
	 * 根据用户Id删除用户
	 * @param id
	 * @return
	 */
	public boolean deleteByUserId(long id);
	
	
	/**
	 * 批量删除用户
	 * @param ids
	 * @return
	 */
	public boolean batchDelete(String ids);
	
	/**
	 * 根据用户ID查询用户
	 * @param id
	 * @return
	 */
	public User findUserById(long id);
	
	/**
	 * 根据ID冻结或者激活账号
	 * @param id
	 * @param state
	 * @return
	 */
	public boolean freezeOrActivate(long id,String state);
	
	/**
	 * 根据ID列表批量冻结用户
	 * @param ids
	 * @return
	 */
	public int batchFreeze(String ids);
	
	
	/**
	 * 描述：验证手机号码的唯一性
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param tel
	 * @return
	 */
	public Result validateTelphone(String tel);
	
	

	/**
	 * 描述：根据ids批量导出用户信息  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param ids
	 * @param outputStream
	 */
	public void batchExport(String ids,ServletOutputStream outputStream);
	
	/**
	 * 描述：更新告警通知时间
	 * 描述：方法说明  
	 * 
	 * @author yxt_majintao
	 * @version 1.0   
	 * @param date
	 */
	public void updateNotifyDate(Date date,long id);
}
