package com.acooly.core.common.facade;

import com.acooly.core.common.exception.OrderCheckException;
import com.acooly.core.utils.ToString;

import java.io.Serializable;

/**
 * @author qiubo@yiji.com
 */
public class InfoBase implements Serializable {
    @Override
    public String toString() {
        return ToString.toString(this);
    }

    /**
     * 请求参数校验,在使用@AppService并且没有启用校验组@AppService.ValidationGroup时会被调用
     */
    public void check() throws OrderCheckException {
    }
}
