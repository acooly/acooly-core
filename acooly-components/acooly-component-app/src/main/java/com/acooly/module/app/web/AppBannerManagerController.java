package com.acooly.module.app.web;

import com.acooly.module.app.AppProperties;
import com.acooly.module.app.domain.AppBanner;
import com.acooly.module.app.service.AppBannerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/module/app/appBanner")
public class AppBannerManagerController extends AppAbstractManageController<AppBanner, AppBannerService> {


    @Autowired
    private AppProperties appProperties;

    @Autowired
    private AppBannerService appBannerService;

    @Override
    protected AppBanner onSave(HttpServletRequest request, HttpServletResponse response, Model model, AppBanner entity,
                               boolean isCreate) throws Exception {
        Map<String, UploadResult> uploadResults = doUpload(request);
        if (uploadResults != null && !uploadResults.isEmpty()) {
            UploadResult uploadResult = uploadResults.get("bannerFile");
            if (uploadResult != null) {
                entity.setMediaUrl(getImagePath(uploadResult));
            }
        }
        entity.setUpdateTime(new Date());
        return entity;
    }

    @Override
    protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
            throws Exception {
        if (ids == null || ids.length == 0) {
            return;
        }
        AppBanner appBanner = loadEntity(request);
        removeFile(appBanner.getMediaUrl());
        super.doRemove(request, response, model, ids);
    }

    private void removeFile(String path) {
        File file = new File(getStorageRoot() + path);
        try {
            try {
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    private String getImagePath(UploadResult uploadResult) {
        String filePath = uploadResult.getFile().getPath();
        String rootPath = new File(getStorageRoot()).getPath();
        return StringUtils.substringAfter(filePath, rootPath);
    }

    @Override
    protected UploadConfig getUploadConfig() {
        UploadConfig config = super.getUploadConfig();
        String storageRoot = getStorageRoot();
        config.setStorageRoot(storageRoot + File.separator + appProperties.getStoragePath());
        config.setUseMemery(false);
        config.setAllowExtentions("jpg,gif,png");
        return config;
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("serverRoot", oFileProperties.getServerRoot());
    }


}
