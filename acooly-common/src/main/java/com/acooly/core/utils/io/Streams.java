/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-12 16:49
 */
package com.acooly.core.utils.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.Closeable;

/**
 * @author zhangpu
 * @date 2019-09-12 16:49
 */
@Slf4j
public class Streams extends IOUtils {


    /**
     * 重启IOUtils的优雅关闭功能
     * <p>
     * 原因：部分需要先条件初始化资源的场景，try-with-resource并不能很好的解决。
     *
     * @param closeables
     */
    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }

        for (final Closeable closeable : closeables) {
            closeQuietly(closeable);
        }
    }

}
