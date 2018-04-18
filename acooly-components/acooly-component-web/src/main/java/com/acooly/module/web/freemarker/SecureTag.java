/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * shuijing 2018-03-29 11:06 创建
 */
package com.acooly.module.web.freemarker;

import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author shuijing
 */
public abstract class SecureTag implements TemplateDirectiveModel {
    @Override
    public void execute(
            Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        verifyParameters(params);
        render(env, params, body);
    }

    protected void verifyParameters(Map params) throws TemplateModelException {
    }

    public abstract void render(Environment env, Map params, TemplateDirectiveBody body)
            throws IOException, TemplateException;

    protected String getParam(Map params, String name) {
        Object value = params.get(name);

        if (value instanceof SimpleScalar) {
            return ((SimpleScalar) value).getAsString();
        }

        return null;
    }
}
