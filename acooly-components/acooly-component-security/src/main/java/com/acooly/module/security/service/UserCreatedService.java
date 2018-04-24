package com.acooly.module.security.service;

import com.acooly.module.security.domain.User;

/**
 * @author shuijing
 */
public interface UserCreatedService {
    void afterUserCreated(User user);
}
