/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:20 创建
 */
package com.acooly.module.mybatis.ex;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * @author qiubo@yiji.com
 */
public interface GetMapper<T> {
    @SelectProvider(type = GetProvider.class, method = "dynamicSQL")
    T get(Serializable id);

    @SelectProvider(type = GetProvider.class, method = "dynamicSQL")
    List<T> getAll();

    class GetProvider extends BaseSelectProvider {

        public GetProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }

        public String get(MappedStatement ms) {
            return super.selectByPrimaryKey(ms);
        }

        public String getAll(MappedStatement ms) {
            return super.selectAll(ms);
        }
    }
}
