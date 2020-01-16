/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-08-31 14:20
 */
package com.acooly.core;

import com.acooly.core.utils.Strings;

/**
 * @author zhangpu
 * @date 2019-08-31 14:20
 */
public class AcoolyVersion {

    public static final String VERSION_5_0_0_SNAPSHOT = "5.0.0-SNAPSHOT";

    private AcoolyVersion() {
    }

    /**
     * Return the full version string of the present Spring Boot codebase, or {@code null}
     * if it cannot be determined.
     *
     * @return the version of Spring Boot or {@code null}
     * @see Package#getImplementationVersion()
     */
    public static String getVersion() {
        Package pkg = AcoolyVersion.class.getPackage();
        String currentVersion = pkg.getImplementationVersion();
        if(Strings.isBlank(currentVersion)){
            currentVersion = VERSION_5_0_0_SNAPSHOT;
        }
        return currentVersion;
    }

    public static String getMajorVersion() {
        return Strings.first(getVersion());
    }


}