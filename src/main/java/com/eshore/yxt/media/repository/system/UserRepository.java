package com.eshore.yxt.media.repository.system;

import java.util.Collection;
import java.util.Date;

import com.eshore.yxt.media.model.system.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>,JpaSpecificationExecutor<User> {


		
	/**
	 * 根据登录手机号码查找用户
	 * @param username
	 * @return
	 */
	public User getUserBytelephone(String telephone);
	
	/**
	 * 根据用户ID批量冻结用户
	 * @param ids
	 * @return
	 */
	@Modifying
	@Query("update User u set u.locked = 1 where u.id in(?1)")
	public int batchFreezeUser(Collection<Long> ids);
	
	@Modifying
	@Query("update User u set u.notifyDate = ? where u.id = ?")
	public int updateNotifyDate(Date date,long id);
}

