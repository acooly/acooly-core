/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-12-13 15:21 创建
 */
package com.acooly.core.test.dal;

import com.acooly.core.common.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qiubo@yiji.com
 */
@Entity
@Table(name = "app")
@Getter
@Setter
public class App extends BaseEntity {
	
	@Column(name = "name", length = 64)
	private String name;
	
	@Column(name = "display_name", length = 512)
	private String displayName;
	
	@Column(name = "type", length = 64)
	private String type;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "parent_id")
	private Long parentAppId;
}