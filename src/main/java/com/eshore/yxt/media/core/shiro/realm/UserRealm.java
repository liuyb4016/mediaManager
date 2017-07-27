package com.eshore.yxt.media.core.shiro.realm;


import com.eshore.yxt.media.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.eshore.yxt.media.model.system.User;


public class UserRealm extends AuthorizingRealm {

    private UserService userService;

    
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String telephone = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//      authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.getPermissionKeyByTelephone(telephone));
        return authorizationInfo;
    }

    
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String telephone = (String)token.getPrincipal();

        User user = userService.getUserBytelephone(telephone);

        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
       }

       if("1".equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
               user.getTelephone(), //登录手机号码
               user.getPassword(), //密码
               ByteSource.Util.bytes(user.getSalt()),//盐
               getName()  //realm name
        );
        SecurityUtils.getSubject().getSession().setAttribute("user", user);
        return authenticationInfo;
    }

    
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
