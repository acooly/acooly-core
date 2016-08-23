package com.acooly.core.common.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 统一定义id的entity基类
 * 
 * 实体基类
 * 
 * @author zhangpu
 * 
 */
@MappedSuperclass
public abstract class BaseEntity extends AbstractEntity {
	/** UID */
	private static final long serialVersionUID = -1L;
	protected Long id;

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
