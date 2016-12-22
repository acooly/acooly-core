package com.acooly.core.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 统一定义id的entity基类
 * 
 * 实体基类
 * 
 * @author zhangpu
 * 
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity implements Serializable, Persistable<Long> {
	/** UID */
	private static final long serialVersionUID = -1L;
	@Id
	@GeneratedValue
	private Long id;

	@Transient
	public boolean isNew() {
		return null == getId();
	}
	
}
