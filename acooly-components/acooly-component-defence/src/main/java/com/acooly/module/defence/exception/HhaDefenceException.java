/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-24 23:49
 */
package com.acooly.module.defence.exception;

import com.acooly.core.utils.enums.Messageable;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhangpu
 * @date 2019-11-24 23:49
 */
@Slf4j
public class HhaDefenceException extends AccessDeniedException {
    public HhaDefenceException(String message) {
        super(message);
    }

    public HhaDefenceException(Messageable messageable, String msg) {
        super(messageable, msg);
    }
}
