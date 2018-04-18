package com.acooly.core.common.web.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 动态代理对象的JsonMapper 解决在Json解析Hibernate生成的延迟加载动态代理对象中生成的部分属性不可以用反射访问的问题。
 *
 * @author zhangpu
 */
public class DynamicProxyObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 6931339368333683269L;

    public DynamicProxyObjectMapper() {
        super();
        disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
