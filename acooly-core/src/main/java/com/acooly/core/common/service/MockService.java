package com.acooly.core.common.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标注在需要被mock的接口实现类上，并设置acooly.mock.enable=true
 * <p>
 * 比如XService有两个实现类，一个XServiceImpl，一个XServiceMock，在XServiceMock上标注此注解，
 * 当mock启用时，使用XServiceMock提供服务，当mock关闭时，使用XServiceImpl提供服务
 *
 * @author qiuboboy@qq.com
 * @date 2018-09-03 09:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Primary
@ConditionalOnProperty("acooly.mock.enable")
public @interface MockService {
}
