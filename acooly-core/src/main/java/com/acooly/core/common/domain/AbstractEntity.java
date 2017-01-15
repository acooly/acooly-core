package com.acooly.core.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 创建时间
	 */
	private Date createTime = new Date();
	
	/**
	 * 修改时间
	 */
	private Date updateTime = new Date();
	
	@Transient
	public boolean isNew() {
		return null == getId();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AbstractEntity))
			return false;
		
		AbstractEntity that = (AbstractEntity) o;
		
		return id != null ? id.equals(that.id) : that.id == null;
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
	
}
