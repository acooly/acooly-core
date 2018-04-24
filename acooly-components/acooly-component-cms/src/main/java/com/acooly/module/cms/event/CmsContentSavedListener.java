/*
 * www.prosysoft.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-04-23 09:54 创建
 */
package com.acooly.module.cms.event;

import com.acooly.module.cms.domain.Content;
import com.acooly.module.cms.service.CmsContentSavedService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shuijing
 */
public class CmsContentSavedListener implements ApplicationListener<CmsContentSavedEvent>, DisposableBean {

    private ExecutorService executorService =
            Executors.newSingleThreadExecutor(new CustomizableThreadFactory("cmsContentSavedServiceThread"));

    private CmsContentSavedService cmsContentSavedService;

    public CmsContentSavedListener(CmsContentSavedService cmsContentSavedService) {
        this.cmsContentSavedService = cmsContentSavedService;
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }

    @Override
    public void onApplicationEvent(CmsContentSavedEvent event) {
        Content content = (Content) event.getSource();
        executorService.execute(() -> {
            if (content != null) {
                cmsContentSavedService.afterCmsContentSaved(content);
            }
        });
    }
}
