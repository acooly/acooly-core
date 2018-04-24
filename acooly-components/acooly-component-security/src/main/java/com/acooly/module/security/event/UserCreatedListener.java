/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-04-23 09:54 创建
 */
package com.acooly.module.security.event;

import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserCreatedService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shuijing
 */
public class UserCreatedListener implements ApplicationListener<UserCreatedEvent>, DisposableBean {

    private ExecutorService executorService =
            Executors.newSingleThreadExecutor(new CustomizableThreadFactory("UserCreatedEventThread"));

    private UserCreatedService userCreatedService;

    public UserCreatedListener(UserCreatedService userCreatedService) {
        this.userCreatedService = userCreatedService;
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = (User) event.getSource();
        executorService.execute(() -> {
            if (user != null) {
                userCreatedService.afterUserCreated(user);
            }
        });
    }
}
