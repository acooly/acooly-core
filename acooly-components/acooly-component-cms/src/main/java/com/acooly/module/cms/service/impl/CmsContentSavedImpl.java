/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-04-24 17:50 创建
 */
package com.acooly.module.cms.service.impl;

import com.acooly.module.cms.domain.Content;
import com.acooly.module.cms.service.CmsContentSavedService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuijing
 */
@Slf4j
public class CmsContentSavedImpl implements CmsContentSavedService {
    @Override
    public void afterCmsContentSaved(Content content) {
        log.info("新增内容,todo,contentId={}", content.getId());
    }
}
