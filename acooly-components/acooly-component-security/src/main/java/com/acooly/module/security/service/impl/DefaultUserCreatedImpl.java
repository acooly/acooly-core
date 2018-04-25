/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-04-24 14:36 创建
 */
package com.acooly.module.security.service.impl;

import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserCreatedService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuijing
 */
@Slf4j
public class DefaultUserCreatedImpl implements UserCreatedService {
    @Override
    public void afterUserCreated(User user) {
    }
}
