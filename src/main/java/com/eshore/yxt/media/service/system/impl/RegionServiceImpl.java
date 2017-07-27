package com.eshore.yxt.media.service.system.impl;

import java.util.List;

import com.eshore.yxt.media.model.system.Region;
import com.eshore.yxt.media.repository.system.RegionRepository;
import com.eshore.yxt.media.service.system.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
    RegionRepository regionRepository;
	
	public List<Region> findProv() {
		return regionRepository.findByPcode("00");
	}

	public List<Region> findCityByProvCode(String pcode) {
		List<Region> list = regionRepository.findByPcode(pcode);
		return list;
	}

	public List<Region> findCity() {
		return regionRepository.findByPcodeNot("00");
	}

}
