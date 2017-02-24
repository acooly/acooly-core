/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * kuli@yiji.com 2017-02-24 04:42 创建
 */
package com.acooly.module.app.web;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.service.EntityService;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.app.AppProperties;
import com.acooly.module.ofile.OFileProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @author acooly
 */
public abstract class AppAbstractManageController<T extends Entityable, M extends EntityService<T>> extends AbstractJQueryEntityController<T, M> {

    @Autowired
    protected OFileProperties oFileProperties;

    @Autowired
    protected AppProperties appProperties;

    /**
     * 获取APP模块存储路径
     *
     * @return
     */
    protected String getStorageRoot() {
        String home = oFileProperties.getStorageRoot() + File.separator + getAppStorageRoot();
        File file = new File(home);
        if (!file.exists()) {
            file.mkdirs();
        }
        return home;
    }

    /**
     * APP模块的存储相对路径
     *
     * @return
     */
    protected String getAppStorageRoot() {
        return appProperties.getStoragePath();
    }


    private String getDatabasePath(UploadResult uploadResult) {
        String filePath = uploadResult.getFile().getPath();
        String rootPath = new File(getStorageRoot()).getPath();
        return StringUtils.substringAfter(filePath, rootPath);
    }

}
