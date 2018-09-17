package com.acooly.module.dubbo;

import com.acooly.core.common.dubbo.DubboFactory;
import com.acooly.core.utils.Assert;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author qiubo@yiji.com
 */
public class DubboFactoryImpl implements DubboFactory, InitializingBean {
    private ReferenceConfigCache referenceConfigCache;

    @Override
    public <T> T getProxy(Class<T> clazz, String group, String version, int timeout) {
        Assert.notNull(clazz);
        Assert.notNull(group);
        Assert.notNull(version);
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setInterface(clazz);
        reference.setVersion(version);
        reference.setTimeout(timeout);
        reference.setGroup(group);
        return clazz.cast(referenceConfigCache.get(reference));
    }

    @Override
    public <T> T getProxy(Class<T> clazz, String group, String version) {
        return this.getProxy(clazz, group, version, DEFAUTL_TIMEOUT);
    }

    @Override
    public <T> T getProxy(Class<T> clazz, String group) {
        return this.getProxy(clazz, group, DEFAULT_VERSION, DEFAUTL_TIMEOUT);
    }

    @Override
    public <T> T getProxy(Class<T> clazz) {
        return this.getProxy(clazz, null, DEFAULT_VERSION, DEFAUTL_TIMEOUT);
    }

    @Override
    public void afterPropertiesSet() {
        referenceConfigCache = ReferenceConfigCache.getCache();
    }
}
