/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing@yiji.com 2018-4-20 10:41 创建
 */
package com.acooly.module.cms.event;

import com.acooly.module.cms.domain.Content;
import org.springframework.context.ApplicationEvent;

/**
 * @author shuijing@yiji.com
 */
public class CmsContentSavedEvent extends ApplicationEvent {
    public CmsContentSavedEvent(Content content) {
        super(content);
    }
}
