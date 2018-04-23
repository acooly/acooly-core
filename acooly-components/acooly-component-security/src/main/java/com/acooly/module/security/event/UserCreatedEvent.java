/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing@yiji.com 2018-4-20 10:41 创建
 */
package com.acooly.module.security.event;

import org.springframework.context.ApplicationEvent;

import javax.sql.DataSource;

/**
 * @author shuijing@yiji.com
 */
public class UserCreatedEvent extends ApplicationEvent {

    public UserCreatedEvent(DataSource source) {
        super(source);
    }
}
