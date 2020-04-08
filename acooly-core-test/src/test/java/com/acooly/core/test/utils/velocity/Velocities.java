/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-03 00:30
 */
package com.acooly.core.test.utils.velocity;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 模板处理工具类，现在只支持类路径方式，不支持jar,绝对路径等方式。
 *
 * @author pandy
 * @author zhangpu
 * @date 2020-01-03 00:30
 */
@Slf4j
public class Velocities {


    private final static String DEFAULT_TEMPLATE_PATH = "";
    private final static String DEFAULT_LOG_TAG = "mystring";
    private static VelocityEngine engine;

    /**
     * 做初始化信息
     */
    static {
        Properties p = new Properties();
        // 设置输入输出编码类型。和这次说的解决的问题无关
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

//        ORDER_PATTERN.setProperty("userdirective", "org.apache.velocity.tools.generic.directive.Ifnull");
        // 这里加载类路径里的模板而不是文件系统路径里的模板
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        engine = new VelocityEngine();
        try {
            engine.init(p);
            Velocity.init();
        } catch (Exception e) {
            e.printStackTrace();
            engine = null;
        }
    }

    /**
     * 参数转换：Pap->VelocityContext
     *
     * @param paramters
     * @return
     */
    private static VelocityContext parseMapToVelocityContext(Map paramters) {
        VelocityContext context = new VelocityContext();
        if (paramters != null && !paramters.isEmpty()) {
            Iterator it = paramters.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                Object value = paramters.get(key);
                context.put(key.toString(), value);
            }
        }
        return context;
    }

    /**
     * 读取模板文件
     *
     * @param templateName
     * @param path
     * @return
     */
    private static Template getTemplate(String templateName, String path) {
        Template template = null;
        try {
            template = engine.getTemplate(path + templateName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }

    /**
     * @param templateName
     * @return 模板的html字符串
     */
    public static String merge(String templateName) {
        return merge(templateName, null, null, null);
    }

    /**
     * @param templateName
     * @param path
     * @return
     */
    public static String merge(String templateName, String path) {
        return merge(templateName, path, null, null);
    }

    /**
     * 这个方法要自动转换Map类型，变成VelocityContext类型
     *
     * @param templateName
     * @param paramters
     * @return 模板的html字符串
     */
    public static String merge(String templateName, Map paramters) {
        return merge(templateName, DEFAULT_TEMPLATE_PATH,
                parseMapToVelocityContext(paramters), null);
    }

    /**
     * @param templateName
     * @param path
     * @param paramters
     * @return
     */
    public static String merge(String templateName, String path, Map paramters) {
        return merge(templateName, path, parseMapToVelocityContext(paramters), null);
    }

    /**
     * @param templateName
     * @param context
     * @return 模板的html字符串
     */
    public static String merge(String templateName, String path,
                               VelocityContext context) {
        return merge(templateName, path, context, null);
    }

    /**
     * @param templateName
     * @param context
     * @return
     */
    public static String merge(String templateName, VelocityContext context) {
        return merge(templateName, DEFAULT_TEMPLATE_PATH, context, null);
    }

    /**
     * @param templateName
     * @param context
     * @param writer
     * @return 模板的html字符串
     */
    public static String merge(String templateName, VelocityContext context,
                               StringWriter writer) {
        return merge(templateName, DEFAULT_TEMPLATE_PATH, context, writer);
    }

    /**
     * 最终执行方法
     *
     * @param templateName
     * @param path
     * @param context
     * @param writer
     * @return 模板的html字符串
     */
    public static String merge(String templateName, String path,
                               VelocityContext context, StringWriter writer) {
        Template template = getTemplate(templateName, path);
        if (writer == null) {
            writer = new StringWriter();
        }

        try {
            template.merge(context, writer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        /* show the World */
        return writer.toString();
    }

    public static String mergeWithStr(Map paramters, String templateStr) {
        StringWriter writer = new StringWriter();
        return mergeWithStr(parseMapToVelocityContext(paramters), writer,
                templateStr);
    }

    public static String mergeWithStr(Map paramters, StringWriter writer,
                                      String templateStr) {
        return mergeWithStr(parseMapToVelocityContext(paramters), writer,
                templateStr);
    }

    public static String mergeWithStr(VelocityContext context,
                                      StringWriter writer, String templateStr) {
        try {
            Velocity.evaluate(context, writer, DEFAULT_LOG_TAG, templateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static String mergeWithStr(Map paramters, Reader reader) {
        StringWriter writer = new StringWriter();
        return mergeWithStr(parseMapToVelocityContext(paramters), writer,
                reader);
    }

    public static String mergeWithStr(Map paramters, StringWriter writer,
                                      Reader reader) {
        return mergeWithStr(parseMapToVelocityContext(paramters), writer,
                reader);
    }

    public static String mergeWithStr(VelocityContext context,
                                      StringWriter writer, Reader reader) {
        try {
            Velocity.evaluate(context, writer, DEFAULT_LOG_TAG, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    @Deprecated
    public static String mergeWithStr(Map paramters, InputStream instream) {
        StringWriter writer = new StringWriter();
        return mergeWithStr(parseMapToVelocityContext(paramters), writer,
                instream);
    }

    @Deprecated
    public static String mergeWithStr(Map paramters, StringWriter writer,
                                      InputStream instream) {
        return mergeWithStr(parseMapToVelocityContext(paramters), writer,
                instream);
    }

    @Deprecated
    public static String mergeWithStr(VelocityContext context,
                                      StringWriter writer, InputStream instream) {
        try {
            Velocity.evaluate(context, writer, DEFAULT_LOG_TAG, instream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Map paramters = new HashMap();
        paramters.put("name", "Pandy");
        paramters.put("site", "http://zhuhaironghui.oicp.net");
//        String str = merge("hello.vm", "com/rh/core/menu/templates/", paramters);
//        System.out.println(str);

        System.out.println(mergeWithStr(paramters, "Hello $name!  Welcome to $site world!"));
    }

}
