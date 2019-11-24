/**
 * create by zhangpu date:2015年10月15日
 */
package com.acooly.integration.web;

import org.springframework.context.ApplicationContextException;
import org.springframework.web.servlet.view.JstlView;

import java.io.File;
import java.util.Locale;

/**
 * JSTL springMVC View 扩展实现
 *
 * <p>增强对resource是否存在的检查
 * @Deprecated：用于springV3+acoolyV3时代的JSP和Freemarker整合
 * @author zhangpu
 * @date 2015年10月15日
 */
@Deprecated
public class ResourceCheckJstlView extends JstlView {

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        try {
            String filePath = getServletContext().getRealPath(getUrl());
            File file = new File(filePath);
            return file.exists();
        } catch (Exception ex) {
            throw new ApplicationContextException(
                    "Could not load webapp file for URL [" + getUrl() + "]", ex);
        }
    }
}
