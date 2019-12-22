/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-06-25 11:25 创建
 *
 */
package com.acooly.module.defence.exception;

import com.acooly.core.utils.enums.Messageable;

/**
 * @author qiubo
 * @author zhangpu : 修改基类为BusinessException，便于统一异常处理
 */
public class AccessDeniedException extends DefenceException {
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(Messageable messageable, String msg) {
        super(messageable, msg);
    }
}
