package com.eshore.yxt.media.model.system;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.eshore.yxt.media.model.base.AutoModel;

@Entity
@Table(name="wx_region")
public class Region extends AutoModel implements Serializable {
	
	private String code;
	private String name;
	private String pcode;
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
}
