package com.eshore.yxt.media.model.system;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.eshore.yxt.media.core.mvc.json.JsonDateSerializer;
import com.eshore.yxt.media.model.base.AutoModel;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@Entity
@Table(name = "wx_role")
public class Role extends AutoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "rolename")
	private String roleName; // 角色名称
	
	@Column(name="rolekey")
	private String roleKey; //角色标识（唯一）

	@Column(name = "remark")
	private String remark; // 角色备注

	@JsonSerialize(using=JsonDateSerializer.class)
	@Column(name = "createtime")
	private Date createTime; // 创建时间

	@JsonSerialize(using=JsonDateSerializer.class) 
	@Column(name = "updatetime")
	private Date updateTime; // 更新时间

	@JsonIgnore
	@OneToMany(mappedBy="role")  //mapedBy 角色不维护关系
	//@JoinColumn(name="role_id")  
	private Set<User> users;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@OrderBy("seq ASC")
	@JoinTable(name = "wx_role_permission", joinColumns = { @JoinColumn(name = "fk_role_id") }, inverseJoinColumns = { @JoinColumn(name = "fk_permission_id") })
	private Set<Permission> permissions; // 角色拥有的权限

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}



}
