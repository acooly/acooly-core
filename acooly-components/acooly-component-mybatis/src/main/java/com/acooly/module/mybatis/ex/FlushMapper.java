/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-04 18:33 创建
 */
package com.acooly.module.mybatis.ex;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;

/**
 * @author qiubo@yiji.com
 */
public interface FlushMapper<T> {
    @DeleteProvider(type = FlushProvider.class, method = "dynamicSQL")
    void flush();

    class FlushProvider extends MapperTemplate {

        public FlushProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
            super(mapperClass, mapperHelper);
        }

        public String flush(MappedStatement ms) {
            return "SELECT now();";
        }
    }
}
