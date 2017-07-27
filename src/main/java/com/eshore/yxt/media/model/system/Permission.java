package com.eshore.yxt.media.model.system;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.eshore.yxt.media.core.mvc.json.JsonDateSerializer;
import com.eshore.yxt.media.model.base.AutoModel;

@Entity
@Table(name = "wx_permission")
public class Permission extends AutoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name; // 权限名称

	@Column(name = "action")
	private String action; // 请求地址

	@Column(name = "pkey")
	private String pkey; // 资源标识符（权限管理使用：自定义唯一）

	@Column(name = "type")
	private String type; // 权限类型 1:菜单 2：按钮

	@Column(name = "remark")
	private String remark; // 备注

	@Column(name = "seq")
	private int seq; // 序号(排序使用)

	@JsonSerialize(using=JsonDateSerializer.class) 
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "createtime")
	private Date createTime; // 创建时间

	@JsonSerialize(using=JsonDateSerializer.class) 
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	@Temporal(TemporalType.DATE)
	@Column(name = "updatetime")
	private Date updateTime; // 创建时间

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY,mappedBy="permissions")
	//@JoinTable(name = "wx_role_permission", joinColumns = { @JoinColumn(name = "fk_role_id") }, inverseJoinColumns = { @JoinColumn(name = "fk_permission_id") })
	private Set<Role> roles; // 资源对应的角色

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentid")
	private Permission parent; // 父级

	@JsonIgnore
	@OrderBy("seq ASC")
	@OneToMany(targetEntity = Permission.class,fetch = FetchType.LAZY,cascade = { CascadeType.ALL }, mappedBy = "parent")
	private Set<Permission> children; // 孩子节点

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Permission getParent() {
		return parent;
	}

	public void setParent(Permission parent) {
		this.parent = parent;
	}



	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Permission> getChildren() {
		return children;
	}

	public void setChildren(Set<Permission> children) {
		this.children = children;
	}

	

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
