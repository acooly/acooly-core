package com.acooly.module.app.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.app.domain.AppStartGuide;
import com.acooly.module.app.enums.EntityStatus;
import com.acooly.module.app.service.AppStartGuideService;
import com.acooly.module.ofile.OFileProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.Map;

@Controller
@RequestMapping(value = "/manage/module/app/appStartGuide")
public class AppStartGuideManagerController extends AbstractJQueryEntityController<AppStartGuide, AppStartGuideService> {

    private static Map<String, String> allStatuss = EntityStatus.mapping();

    @Autowired
    private OFileProperties oFileProperties;
    @Autowired
    private AppStartGuideService appStartGuideService;

    @Override
    protected AppStartGuide onSave(HttpServletRequest request, HttpServletResponse response, Model model,
                                   AppStartGuide entity, boolean isCreate) throws Exception {
        Map<String, UploadResult> uploadResults = doUpload(request);
        if (uploadResults != null && !uploadResults.isEmpty()) {
            UploadResult uploadResult = uploadResults.get("fileDefault");
            if (uploadResult != null) {
                entity.setImageDefault(getImagePath(uploadResult));
            }
            uploadResult = uploadResults.get("fileIphone4");
            if (uploadResult != null) {
                entity.setImageIphone4(getImagePath(uploadResult));
            }
            uploadResult = uploadResults.get("fileIphone6");
            if (uploadResult != null) {
                entity.setImageIphone6(getImagePath(uploadResult));
            }
            uploadResult = uploadResults.get("fileAndroid");
            if (uploadResult != null) {
                entity.setImageAndroid(getImagePath(uploadResult));
            }
        }
        if (isCreate) {
            entity.setUpdateTime(entity.getCreateTime());
        }
        return entity;
    }

    @Override
    protected void doRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
            throws Exception {
        if (ids == null || ids.length == 0) {
            return;
        }
        AppStartGuide asg = loadEntity(request);
        if (StringUtils.isNotBlank(asg.getImageDefault())) {
            removeFile(asg.getImageDefault());
        }
        if (StringUtils.isNotBlank(asg.getImageAndroid())) {
            removeFile(asg.getImageAndroid());
        }
        if (StringUtils.isNotBlank(asg.getImageIphone4())) {
            removeFile(asg.getImageIphone4());
        }
        if (StringUtils.isNotBlank(asg.getImageIphone6())) {
            removeFile(asg.getImageIphone6());
        }
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
        return StringUtils.substringAfter(filePath, rootPath).replace("\\", "/");
    }

    @Override
    protected UploadConfig getUploadConfig() {
        UploadConfig config = super.getUploadConfig();
        String storageRoot = getStorageRoot();
        config.setStorageRoot(storageRoot);
        config.setUseMemery(false);
        config.setAllowExtentions("jpg,gif,png");
        return config;
    }

    private String getStorageRoot() {
        String home = oFileProperties.getStorageRoot();
        File file = new File(home);
        if (!file.exists()) {
            file.mkdirs();
        }
        return home;
    }

    @Override
    protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
        model.put("allStatuss", allStatuss);
    }

}
