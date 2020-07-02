/**
 * create by zhangpu date:2016年1月10日
 */
package com.acooly.core.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 环境操作工具
 *
 * @author zhangpu
 * @date 2016年1月10日
 */
public class Profiles {

    public static Profile withOf(String val) {
        for (Profile p : Profile.values()) {
            if (Strings.equals(p.name(), val)) {
                return p;
            }
        }
        return Profile.other;
    }

    public static Profile getProfile() {
        String activeProfile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        if (Strings.isBlank(activeProfile)) {
            activeProfile = System.getenv(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME);
        }
        if (activeProfile == null) {
            throw Exceptions.runtimeException(
                    "需要配置系统或环境变量:" + AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        }
        return withOf(activeProfile);
    }

    public static void setProfile(Profile profile) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile.name());
    }

    public static String getProfileFileName(String fileName) {
        return FilenameUtils.getFullPathNoEndSeparator(fileName)
                + FilenameUtils.getBaseName(fileName)
                + "."
                + getProfile()
                + "."
                + FilenameUtils.getExtension(fileName);
    }

    public static boolean isDev() {
        return getProfile() == Profile.dev || getProfile() == Profile.sdev;
    }

    public static boolean isTest() {
        return getProfile() == Profile.test || getProfile() == Profile.stest;
    }

    public static boolean isOnline() {
        return getProfile() == Profile.online || getProfile() == Profile.prev;
    }


    /**
     * 环境感知枚举
     */
    public enum Profile {
        /**
         * 开发
         */
        dev,

        /**
         * 系统公用开发
         */
        sdev,
        /**
         * 测试
         */
        test,
        /**
         * 系统公共测试
         */
        stest,
        /**
         * 演示
         */
        net,
        /**
         * 系统公共演示
         */
        snet,
        /**
         * 预发布
         */
        prev,
        /**
         * 生产
         */
        online,

        /**
         * 其他
         */
        other
    }
}
