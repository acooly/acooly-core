/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-06-22 17:57
 */
package com.acooly.core.common.boot.support;

import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.*;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * copy from boot1.5.x
 *
 * @author zhangpu
 * @date 2019-06-22 17:57
 */
@Slf4j
public class PropertySourceUtils {

    public static Map<String, Object> getSubProperties(Environment environment, String keyPrefix) {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if (!Strings.endsWith(keyPrefix, ".")) {
            keyPrefix = keyPrefix + ".";
        }
        return getSubProperties(env.getPropertySources(), keyPrefix);

    }

    /**
     * Return a Map of all values from the specified {@link PropertySources} that start
     * with a particular key.
     *
     * @param propertySources the property sources to scan
     * @param keyPrefix       the key prefixes to test
     * @return a map of all sub properties starting with the specified key prefixes.
     * @see PropertySourceUtils#getSubProperties(PropertySources, String, String)
     */
    public static Map<String, Object> getSubProperties(PropertySources propertySources,
                                                       String keyPrefix) {
        return PropertySourceUtils.getSubProperties(propertySources, null, keyPrefix);
    }

    /**
     * Return a Map of all values from the specified {@link PropertySources} that start
     * with a particular key.
     *
     * @param propertySources the property sources to scan
     * @param rootPrefix      a root prefix to be prepended to the keyPrefix (can be
     *                        {@code null})
     * @param keyPrefix       the key prefixes to test
     * @return a map of all sub properties starting with the specified key prefixes.
     * @see #getSubProperties(PropertySources, String, String)
     */
    public static Map<String, Object> getSubProperties(PropertySources propertySources,
                                                       String rootPrefix, String keyPrefix) {
        RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
        Map<String, Object> subProperties = new LinkedHashMap<String, Object>();
        for (PropertySource<?> source : propertySources) {
            if (source instanceof EnumerablePropertySource) {
                for (String name : ((EnumerablePropertySource<?>) source)
                        .getPropertyNames()) {
                    String key = PropertySourceUtils.getSubKey(name, rootPrefix,
                            keyPrefixes);
                    if (key != null && !subProperties.containsKey(key)) {
                        subProperties.put(key, source.getProperty(name));
                    }
                }
            }
        }
        return Collections.unmodifiableMap(subProperties);
    }

    private static String getSubKey(String name, String rootPrefixes,
                                    RelaxedNames keyPrefix) {
        rootPrefixes = (rootPrefixes != null ? rootPrefixes : "");
        for (String rootPrefix : new RelaxedNames(rootPrefixes)) {
            for (String candidateKeyPrefix : keyPrefix) {
                if (name.startsWith(rootPrefix + candidateKeyPrefix)) {
                    return name.substring((rootPrefix + candidateKeyPrefix).length());
                }
            }
        }
        return null;
    }
}
