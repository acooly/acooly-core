package com.acooly.module.security.domain;

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

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OrderBy;

import com.acooly.core.common.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 系统角色
 * 
 * @author zhangpu
 */
@Entity
@Table(name = "SYS_ROLE")
@JsonIgnoreProperties(value = "users", ignoreUnknown = true)
@Getter
@Setter
public class Role extends AbstractEntity {

	/** UID */
	private static final long serialVersionUID = 7250772367888874004L;

	/** 名称 */
	@Column(name = "NAME")
	private String name;

	private Long orgId;

	/** 描述 */
	@Column(name = "DESCN")
	private String descn;

	/** 包含的用户 */
	@ManyToMany(mappedBy = "roles", targetEntity = User.class, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private Set<User> users;

	/** 包含的资源 */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY, targetEntity = com.acooly.module.security.domain.Resource.class)
	@JoinTable(name = "SYS_ROLE_RESC", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = @JoinColumn(name = "RESC_ID"))
	@OrderBy(clause = "resc_id")
	@LazyCollection(value = LazyCollectionOption.EXTRA)
	private Set<Resource> rescs;

	public Role() {

	}

	public Role(String name) {
		this.name = name;
	}

	/**
	 * full constructor
	 */
	public Role(String name, String descn, Set<User> users, Set<Resource> rescs) {
		this.name = name;
		this.descn = descn;
		this.users = users;
		this.rescs = rescs;
	}

	@Override
	public String toString() {
		return String.format("Role: {name:%s, descn:%s}", name, descn);
	}

}
