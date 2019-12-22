/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-23 17:41
 */
package com.acooly.module.defence.exception;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.enums.Messageable;
import lombok.extern.slf4j.Slf4j;

/**
 * 防御相关的异常基类
 *
 * @author zhangpu
 * @date 2019-11-23 17:41
 */
@Slf4j
public class DefenceException extends BusinessException {

    public DefenceException(String message) {
        super(message);
    }

    public DefenceException(Messageable messageable, String msg) {
        super(messageable, msg);
    }
}
