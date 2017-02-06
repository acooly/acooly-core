/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-03 15:08 创建
 */
package com.acooly.core.utils.service;

import com.acooly.core.utils.Ids;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author qiubo@yiji.com
 */
@Setter
@Getter
public class SingleValueOrder<T> extends OrderBase {
	@Valid
	@NotNull
	private T dto;
	
	public static <T> SingleValueOrder<T> from(T dto) {
		SingleValueOrder<T> singleValueResult = new SingleValueOrder<T>();
		singleValueResult.setDto(dto);
		return singleValueResult;
	}
	public SingleValueOrder<T> gid(){
		return this.gid(Ids.gid());
	}
	public SingleValueOrder<T> gid(String gid){
		this.setGid(gid);
		return this;
	}
	public SingleValueOrder<T> partnerId(String partnerId){
		this.setPartnerId(partnerId);
		return this;
	}
}
