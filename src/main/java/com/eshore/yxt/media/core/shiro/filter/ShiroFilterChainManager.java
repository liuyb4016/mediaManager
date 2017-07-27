package com.eshore.yxt.media.core.shiro.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eshore.yxt.media.model.system.Permission;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;


public class ShiroFilterChainManager {

    private DefaultFilterChainManager filterChainManager;

    private Map<String, NamedFilterList> defaultFilterChains;

    public void init() {
        defaultFilterChains = new HashMap<String, NamedFilterList>(filterChainManager.getFilterChains());
    }

    public void initFilterChains(List<Permission> permissions) {
        //1、首先删除以前老的的filter chain并注册默认的
        filterChainManager.getFilterChains().clear();
        if(defaultFilterChains != null) {
            filterChainManager.getFilterChains().putAll(defaultFilterChains);
        }

        //2.循环注册权限标识
        for(Permission permission : permissions) {
        	  //注册roles filter
//            if (!StringUtils.isEmpty(urlFilter.getRoles())) {
//                filterChainManager.addToChain(url, "roles", urlFilter.getRoles());
//            }
            //注册perms filter
            if (!StringUtils.isEmpty(permission.getPkey()) && !StringUtils.isEmpty(permission.getAction())) {
                filterChainManager.addToChain(permission.getAction(), "perms", permission.getPkey());
            }
        }
    }

    
    
	public DefaultFilterChainManager getFilterChainManager() {
		return filterChainManager;
	}

	public void setFilterChainManager(DefaultFilterChainManager filterChainManager) {
		this.filterChainManager = filterChainManager;
	}

	public Map<String, NamedFilterList> getDefaultFilterChains() {
		return defaultFilterChains;
	}

	public void setDefaultFilterChains(
			Map<String, NamedFilterList> defaultFilterChains) {
		this.defaultFilterChains = defaultFilterChains;
	}

}
