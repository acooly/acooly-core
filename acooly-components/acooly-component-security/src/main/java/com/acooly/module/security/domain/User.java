package com.acooly.module.security.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;

import com.acooly.core.common.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author cac
 */
@Entity
@Table(name = "SYS_USER")
@JsonIgnoreProperties({ "password", "roles", "salt","hibernateLazyInitializer"})
@Getter
@Setter
public class User extends AbstractEntity {
	/** uid */
	private static final long serialVersionUID = -1740401537348774052L;

	public static final int STATUS_ENABLE = 1;
	public static final int STATUS_LOCK = 2;
	public static final int STATUS_EXPIRES = 9;
	public static final int STATUS_DISABLE = 10;

	public static final int USER_TYPE_ADMIN = 1;

	/** 登陆名 */
	private String username;

	/** 密码 */
	private String password;

	private String salt;

	/** 名字 */
	private String realName;

	/** 电子邮件 */
	private String email;

	private String mobileNo;

	/** 用户类型 1:管理员 2:操作员 3:代理商 4:客户等 */
	private int userType;

	private Date createTime = new Date();

	private Date lastModifyTime = new Date();

	/** 密码失效时间 */
	private Date expirationTime = new Date();

	/** 解鎖时间 */
	private Date unlockTime;

	private int loginFailTimes;

	/** 状态 {1:有效,2:过期,3:锁定,10:禁用} */
	private int status = STATUS_ENABLE;

	/** 描述 */
	@Column(name = "DESCN")
	private String descn;

	/** 包含的角色 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, targetEntity = com.acooly.module.security.domain.Role.class)
	@JoinTable(name = "SYS_USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	@OrderBy(clause = "role_id")
	private Set<Role> roles;

	private Long orgId;

	private String orgName;

	/** 最后登陆时间 */
	private Date loginTime;

	/** 登陆状态{1:未登陆,2:已登陆} */
	private int loginStatus;

	/**
	 * default constructor
	 */
	public User() {
	}

	/**
	 * minimal constructor
	 */
	public User(String username, String password, String realName, String email) {
		this.username = username;
		this.password = password;
		this.realName = realName;
		this.email = email;
	}

	/**
	 * full constructor
	 */
	public User(String username, String password, String realName, String email, int status, String descn,
			Set<Role> roles) {
		this.username = username;
		this.password = password;
		this.realName = realName;
		this.email = email;
		this.status = status;
		this.descn = descn;
		this.roles = roles;
	}


	@Transient
	public boolean isEnabled() {
		return (this.status == STATUS_ENABLE);
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", username, realName);
	}

}
