/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-05-19 03:42 创建
 */
package com.acooly.module.portlet.test;

import com.acooly.module.portlet.entity.Feedback;
import com.acooly.module.portlet.integration.FeedbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author acooly
 */
//@Slf4j
//@Primary
//@Component
public class TestFeedbackHandler implements FeedbackHandler {

    @Override
    public void handle(Feedback feedback) {
        //log.info("集成测试，自定义feedback处理...");
    }
}
