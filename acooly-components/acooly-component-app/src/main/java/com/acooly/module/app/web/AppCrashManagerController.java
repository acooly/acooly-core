package com.acooly.module.app.web;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.app.domain.AppCrash;
import com.acooly.module.app.service.AppCrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.acooly.module.app.web.AppAbstractManageController.convertCharSet;

@Controller
@RequestMapping(value = "/manage/module/app/appCrash")
public class AppCrashManagerController
        extends AbstractJQueryEntityController<AppCrash, AppCrashService> {

    @Autowired
    private AppCrashService appCrashService;

    @Override
    protected AppCrash onSave(HttpServletRequest request, HttpServletResponse response, Model model, AppCrash entity, boolean isCreate) throws Exception {
        String title = convertCharSet(request, "title");
        entity.setAppName("");
        return super.onSave(request, response, model, entity, isCreate);
    }
}
