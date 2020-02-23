/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-26 10:08 创建
 */
package com.acooly.module.tenant.cache;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.StringUtils;
import com.acooly.module.tenant.core.TenantContext;
import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author qiubo@yiji.com
 */
public class DefaultKeySerializerX implements RedisSerializer<Object> {

    private final Charset charset = Charset.forName("UTF8");

    @Override
    public byte[] serialize( Object o ) throws SerializationException {
        byte[] bytes;
        if (o == null) {
            return null;
        } else if (o instanceof byte[]) {
            bytes = (byte[]) o;
        } else {
            bytes = o.toString().getBytes(charset);
        }
        String t = TenantContext.get();
        if (StringUtils.isEmpty(t)) {
            throw new BusinessException("线程上下文中没有租户信息,或者租户ID为空");
        }
        byte[] prefix = ( t + '>' ).getBytes(charset);
        byte[] res = new byte[prefix.length + bytes.length];
        System.arraycopy(prefix, 0, res, 0, prefix.length);
        System.arraycopy(bytes, 0, res, prefix.length, bytes.length);
        bytes = res;

        return bytes;
    }

    @Override
    public Object deserialize( byte[] bytes ) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        String key = new String(bytes, charset);
        int i = key.indexOf('>');
        if (i != -1) {
            key = key.substring(i + 1);
        }
        return key;
    }
}
