package com.eshore.yxt.media.service.system.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;

import com.eshore.yxt.media.core.util.ExportUtil;
import com.eshore.yxt.media.model.system.Permission;
import com.eshore.yxt.media.model.system.Region;
import com.eshore.yxt.media.model.system.Role;
import com.eshore.yxt.media.model.system.User;
import com.eshore.yxt.media.repository.system.UserRepository;
import com.eshore.yxt.media.service.system.RegionService;
import com.eshore.yxt.media.service.system.UserService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.req.UserReq;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import com.eshore.yxt.media.web.base.Tree;

@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
    UserRepository userRepository;
	
	@Autowired
	HashedCredentialsMatcher hashedCredentialsMatcher;
	
	@Autowired
    RegionService regionService;
	
	public User getUserBytelephone(String telephone) {
		return userRepository.getUserBytelephone(telephone);
	}

	public boolean save(User user) {
		String salt = user.getTelephone()+new SecureRandomNumberGenerator().nextBytes().toHex();
		user.setSalt(salt);//设置用户加密的盐
		SimpleHash hash = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), 
				user.getPassword(),salt, hashedCredentialsMatcher.getHashIterations());
		user.setPassword(hash.toString());
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setLocked("0");
		User u = userRepository.save(user);
		if(u == null) {
			return false;
		}
		return true;
	}

	public Set<String> getPermissionKeyByTelephone(String telephone) {
		Set<String> pkey = new HashSet<String>();
		
		User user = userRepository.getUserBytelephone(telephone);
		
		Role role = user.getRole();
		
		if(role != null) {
			
			for(Permission permission : role.getPermissions()) {
				if(!StringUtils.isEmpty(permission.getPkey())) {
					pkey.add(permission.getPkey());
				}
			}
		}
		
		if(pkey.size() != 0) {
			return pkey;
		}
		
		return null;
	}

	
	public List<Tree> getPermissionByTelephone(String telephone) {
		List<Tree> plist = new ArrayList<Tree>();
		
		User user = userRepository.getUserBytelephone(telephone);
		
		Role role = user.getRole();
		
		Set<Permission> perms = new HashSet<Permission>();
		
		if(role != null) {
			for(Permission permission : role.getPermissions()) {
				if(permission != null && permission.getType().equals("1")) {
					perms.add(permission);
				}
			}
		}
		
		//转换数据实体
		for(Permission permission : perms) {
			Tree ptd = new Tree();
			ptd.setId(permission.getId());
			ptd.setText(permission.getName());
			Map<String,String> m = new HashMap<String,String>();
			m.put("url", permission.getAction());
			m.put("seq", String.valueOf(permission.getSeq()));
			ptd.setAttributes(m);
			Permission parent = permission.getParent();
			if(parent != null) {
				ptd.setPid(permission.getParent().getId());
			}else {
				ptd.setPid(0);
			}
			plist.add(ptd);
		}
		
		if(plist.size() == 0) {
			return null;
		}
		return plist;
		
	}

	
	public Grid findAllByPager(Pager pager, UserReq user) {
		Pageable pageable = new PageRequest(pager.getPage()-1, pager.getRows());
		
		Page page = userRepository.findAll(queryByMutiCondition(user), pageable);
		
		List<Region> citys = regionService.findCity();
		List<Region> provs = regionService.findProv();
		
		for(User u :(List<User>) page.getContent()) {
			
			for(Region region : citys) {
				if(StringUtils.isEmpty(u.getCityCode())) {
					break ;
				}
				if(u.getCityCode().equals(region.getCode())) {
					u.setCityName(region.getName());
					break;
				}
			}
			
			for(Region region : provs) {
				if(StringUtils.isEmpty(u.getProvCode())) {
					break;
				}
				if(u.getProvCode().equals(region.getCode())) {
					u.setProvName(region.getName());
					break;
				}
			}
		}
		
		Grid grid = new Grid();
		grid.setTotal(page.getTotalElements());
		grid.setRows(page.getContent());
		
		return grid;
	}


	public boolean batchDelete(String ids) {
		String[] id = ids.split(",");
		List<User> users = new ArrayList<User>();
		for(String userId : id) {
			User u = new User();
			u.setId(Long.valueOf(userId));
			users.add(u);
		}
		if(users.size() == 0) {
			return true;
		}
		
		userRepository.delete(users);
		
		return true;
		
	}
	
	public Specification<User> queryByMutiCondition(final UserReq user) {
		
		return new Specification<User>() {

			@SuppressWarnings("unchecked")
			public Predicate toPredicate(Root<User> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				  //存放多个查询条件
				  List<Predicate> predicateList = new ArrayList<Predicate>();
				
				  if(!StringUtils.isEmpty(user.getUsername())) {
					  Path p = root.get("username");
					  String username = user.getUsername();
					  predicateList.add(cb.like(p,"%"+username+"%"));
				  }
				  
				 
				  if(!StringUtils.isEmpty(user.getTelephone())) {
					  Path p = root.get("telephone");
					  String telephone = user.getTelephone();
					  predicateList.add(cb.like(p,"%"+telephone+"%"));
				  }
				  
				  /*if(user.getCreateTimeStart() != null && user.getCreateTimeEnd() != null) {
					  Path p = root.get("createTime");
					  predicateList.add(cb.between(p, user.getCreateTimeStart(), user.getCreateTimeEnd()));
				  }*/
				  if(null != user.getCreateTimeStart()){
					  Path p = root.get("createTime");
					  predicateList.add(cb.greaterThanOrEqualTo(p, user.getCreateTimeStart()));
				  }
				  if(null != user.getCreateTimeEnd()){
					  Path p = root.get("createTime");
					  predicateList.add(cb.lessThanOrEqualTo(p, user.getCreateTimeEnd()));
				  }
				  
				  Predicate[] p = new Predicate[predicateList.size()];
				  
				  return cb.and(predicateList.toArray(p));  
			}
			 
		};
	}

	public boolean deleteByUserId(long id) {
		userRepository.delete(id);
		return true;
	}
	
	public boolean update(User user,String password2) {
		User tu = userRepository.findOne(user.getId());
		user.setCreateTime(tu.getCreateTime());
		user.setUpdateTime(new Date());
		user.setLocked(tu.getLocked());
		
		if(!StringUtils.isEmpty(password2)) {    //管理员重新设置密码
			user.setSalt(user.getTelephone()+new SecureRandomNumberGenerator().nextBytes().toHex());//设置用户加密的盐
			SimpleHash hash = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), 
					password2,user.getSalt(), hashedCredentialsMatcher.getHashIterations());
			user.setPassword(hash.toString());
		}else {
			user.setSalt(tu.getSalt());
			user.setPassword(tu.getPassword());
		}
		User u = userRepository.save(user);
		if(u == null) {
			return false;
		}
		return true;
	}

	public User findUserById(long id) {
		return userRepository.findOne(id);
	}

	@Override
	public boolean freezeOrActivate(long id, String state) {
		User user = userRepository.findOne(id);
		if((state.equals("1")||state.equals("0"))&&user != null) {
			user.setLocked(state);
			if(userRepository.save(user) != null) {
				return true;
			}	
		}
		return false;
	}

	@Override
	public int batchFreeze(String ids) {
		String[] id = ids.split(",");
		List<Long> uids = new ArrayList<Long>();
		for(String userId : id) {
			uids.add(Long.valueOf(userId));
		}
		int count = userRepository.batchFreezeUser(uids);
		return count;
	}

	
	/* (non-Javadoc)
	 * @see UserService#validateTelphone(java.lang.String)
	 */
	@Override
	public Result validateTelphone(String tel) {
		Result result = new Result();
		result.setSuccess("0");
		User u = userRepository.getUserBytelephone(tel);
		if(u == null) {
			result.setMsg("1");   //手机号码可用
		}else {
			result.setMsg("0");   //手机号码不可用
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see UserService#batchExport(java.lang.String, javax.servlet.ServletOutputStream)
	 */
	@Override
	public void batchExport(String ids, ServletOutputStream outputStream) {
		String[] temp = ids.split(",");
		List<Long> ds = new ArrayList<Long>();
		String[] titles = {"真实姓名","登录账号","电子邮件","所属省份","所属城市","创建时间","更新时间","拥有角色","状态"};
		
		if(temp.length != 0) {
			for(String t : temp) {
				ds.add(Long.valueOf(t));
			}
			
			List<Region> citys = regionService.findCity();
			List<Region> provs = regionService.findProv();
			
			Iterator<User> it = userRepository.findAll(ds).iterator();
	  
			// 创建一个workbook 对应一个excel应用文件  
	        XSSFWorkbook workBook = new XSSFWorkbook();  
	        // 在workbook中添加一个sheet,对应Excel文件中的sheet  
	        XSSFSheet sheet = workBook.createSheet("用户信息报表"); 
	       
	       
	        
	        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
	        XSSFCellStyle headStyle = exportUtil.getHeadStyle();  
	        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();  
	        // 构建表头  
	        XSSFRow headRow = sheet.createRow(0);  
	        XSSFCell cell = null;  
	        for (int i = 0; i < titles.length; i++)  
	        {  
	            cell = headRow.createCell(i);  
	            cell.setCellStyle(headStyle);  
	            cell.setCellValue(titles[i]);  
	        }  
	        int j = 0;
	        while(it.hasNext()) {
	        	User user = it.next();
        	    XSSFRow bodyRow = sheet.createRow(j + 1);  
	  
                cell = bodyRow.createCell(0);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellValue(user.getUsername());  
  
                cell = bodyRow.createCell(1);  
                cell.setCellStyle(bodyStyle); 
                
                cell.setCellValue(user.getTelephone());  
  
                cell = bodyRow.createCell(2);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellValue(user.getEmail());  
                
                for(Region region : citys) {
					if(StringUtils.isEmpty(user.getCityCode())) {
						break ;
					}
					if(user.getCityCode().equals(region.getCode())) {
						user.setCityName(region.getName());
						break;
					}
				}
				
				for(Region region : provs) {
					if(StringUtils.isEmpty(user.getProvCode())) {
						break;
					}
					if(user.getProvCode().equals(region.getCode())) {
						user.setProvName(region.getName());
						break;
					}
				}
				
                cell = bodyRow.createCell(3);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellValue(user.getProvName()); 
                
                
                cell = bodyRow.createCell(4);  
                cell.setCellStyle(bodyStyle); 
                cell.setCellValue(user.getCityName());
                
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
                cell = bodyRow.createCell(5);  
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(formater.format(user.getCreateTime()));
                
                cell = bodyRow.createCell(6);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellValue(formater.format(user.getUpdateTime()));
                
                cell = bodyRow.createCell(7);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellValue(user.getRole().getRoleName());
                
                String status = "正常";
                if(user.getLocked().equals("1")) {
                	status = "停用";
                }
                cell = bodyRow.createCell(8);  
                cell.setCellStyle(bodyStyle);  
                cell.setCellValue(status);
                
                j++;
	        }
	        
	        for(int i=0; i < 8 ;i++) {
	        	sheet.autoSizeColumn((short)i); //调整自适应宽度
	        }
	        
	        try  
	        {  
	            workBook.write(outputStream);  
	            outputStream.flush();  
	            outputStream.close();  
	        }  
	        catch (IOException e)  
	        {  
	            e.printStackTrace();  
	        }  
	        finally  
	        {  
	            try  
	            {  
	                outputStream.close();  
	            }  
	            catch (IOException e)  
	            {  
	                e.printStackTrace();  
	            }  
	        }  
	  
		}
	}

	@Override
	public void updateNotifyDate(Date date,long id) {
		userRepository.updateNotifyDate(date, id);
	}
	
	
}
