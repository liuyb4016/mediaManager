/**
 * Copyright (C) 2001-2014 ESHORE Inc.All Rights Reserved.		
 * 																	
 * FileName：SysLogServiceImpl.java						
 *			
 * Description：简要描述本文件的内容								
 * 																	
 * History：
 * 版本号	作者		日期			简要介绍相关操作
 * 1.0	yxt_majintao	2014年9月22日	Create		
 */
package com.eshore.yxt.media.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import com.eshore.yxt.media.model.system.SysLog;
import com.eshore.yxt.media.repository.system.SysLogRepository;
import com.eshore.yxt.media.service.system.SysLogService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.system.req.SysLogReq;

/**
 * 描述:系统操作日志服务层
 * 
 * @author yxt_majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
@Service
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogRepository sysLogRepository;
	
	

	/* (non-Javadoc)
	 * @see SysLogService#saveLog(SysLog)
	 */
	@Override
	public void saveLog(SysLog sysLog) {
		sysLogRepository.save(sysLog);
	}
	
	public SysLogRepository getSysLogRepository() {
		return sysLogRepository;
	}

	public void setSysLogRepository(SysLogRepository sysLogRepository) {
		this.sysLogRepository = sysLogRepository;
	}

	/* (non-Javadoc)
	 * @see SysLogService#qrySysLogByPager(SysLogReq, Pager)
	 */
	@Override
	public Grid qrySysLogByPager(SysLogReq sysLogReq, Pager pager) {
		PageRequest pageRequest = new PageRequest(pager.getPage()-1, pager.getRows(),
				new Sort(Direction.DESC, new String[] { "operateTime" })); 
	
		Specification<SysLog> spec = queryByMutiCondition(sysLogReq);
		//查询
		Page page = sysLogRepository.findAll(spec,pageRequest);
		Grid grid = new Grid();
		grid.setTotal(sysLogRepository.count(spec));
		grid.setRows(page.getContent());
		
		return grid;
	}

	
	public Specification<SysLog> queryByMutiCondition(final SysLogReq sysLogReq) {
		
		return new Specification<SysLog>() {

			@SuppressWarnings("unchecked")
			public Predicate toPredicate(Root<SysLog> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				  //存放多个查询条件
				  List<Predicate> predicateList = new ArrayList<Predicate>();
				  
				  if(sysLogReq.getUserId() != null && sysLogReq.getUserId() != 0) {
					  Path p = root.get("userId");
					  Long userId = sysLogReq.getUserId();
					  predicateList.add(cb.equal(p, userId));
				  }
				  
				  if(sysLogReq.getStartOpTime()!= null && sysLogReq.getEndOpTime() != null) {
					  Path p = root.get("operateTime");
					  Date startOpTime = sysLogReq.getStartOpTime();
					  Date endOpTime =  sysLogReq.getEndOpTime();
					  predicateList.add(cb.between(p, startOpTime, endOpTime));
				  }
				  
				  if(!StringUtils.isEmpty(sysLogReq.getBeforeContent())) {
					  Path p = root.get("beforeContent");
					  predicateList.add(cb.like(p,"%"+sysLogReq.getBeforeContent()+"%"));
				  }
				  
				  if(!StringUtils.isEmpty(sysLogReq.getAfterContent())) {
					  Path p = root.get("afterContent");
					  predicateList.add(cb.like(p,"%"+sysLogReq.getAfterContent()+"%"));
				  }
				  
				  Predicate[] p = new Predicate[predicateList.size()];
				  
				  return cb.and(predicateList.toArray(p));  
			}
			 
		};
	}
}
