package com.acooly.core.common.dubbo;

/**
 * @author qiubo@yiji.com
 */
public interface DubboFactory {
    int DEFAUTL_TIMEOUT = 10000;
    String DEFAULT_VERSION = "1.0";

    /**
     * 获取refence接口代理
     *
     * @param clazz   接口类
     * @param group   分组
     * @param version 版本
     * @param timeout refence超时时间
     * @return 接口代理
     */
    <T> T getProxy(Class<T> clazz, String group, String version, int timeout);

    /**
     * 获取refence接口代理
     * <p>
     * 默认超时为 {@link DubboFactory#DEFAUTL_TIMEOUT}
     *
     * @param clazz   接口类
     * @param group   分组
     * @param version 版本
     * @return 接口代理
     */
    <T> T getProxy(Class<T> clazz, String group, String version);

    /**
     * 获取refence接口代理
     * <p>
     * 默认超时为 {@link DubboFactory#DEFAUTL_TIMEOUT}
     * <p>
     * 默认版本为 {@link DubboFactory#DEFAULT_VERSION}
     *
     * @param clazz 接口类
     * @param group 分组
     * @return 接口代理
     */
    <T> T getProxy(Class<T> clazz, String group);

    /**
     * 获取refence接口代理
     * <p>
     * 默认超时为 {@link DubboFactory#DEFAUTL_TIMEOUT}
     * <p>
     * 默认版本为 {@link DubboFactory#DEFAULT_VERSION}
     * <p>
     * 分组为null
     *
     * @param clazz 接口类
     * @return 接口代理
     */
    <T> T getProxy(Class<T> clazz);
}
