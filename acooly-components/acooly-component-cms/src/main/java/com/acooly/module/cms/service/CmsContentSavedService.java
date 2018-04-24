/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-04-24 17:44 创建
 */
package com.acooly.module.cms.service;

import com.acooly.module.cms.domain.Content;

/**
 * @author shuijing
 */
public interface CmsContentSavedService {
    void afterCmsContentSaved(Content content);
}
