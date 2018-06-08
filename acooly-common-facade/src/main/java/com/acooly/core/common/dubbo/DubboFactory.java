package com.acooly.core.common.dubbo;

/**
 * @author qiubo@yiji.com
 */
public interface DubboFactory {
    public <T> T getProxy(Class<T> clazz, String group, String version, int timeout);

}
