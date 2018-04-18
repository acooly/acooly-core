package com.acooly.module.dubbo;

import com.acooly.core.common.dubbo.DubboFactory;

/**
 * @author qiubo@yiji.com
 */
public class DubboFactoryImpl implements DubboFactory {
    private DubboRemoteProxyFacotry dubboRemoteProxyFacotry;

    public DubboFactoryImpl(DubboRemoteProxyFacotry dubboRemoteProxyFacotry) {
        this.dubboRemoteProxyFacotry = dubboRemoteProxyFacotry;
    }

    public <T> T getProxy(Class<T> clazz, String group, String version, int timeout) {
        return dubboRemoteProxyFacotry.getProxy(clazz, group, version, timeout);
    }
}
