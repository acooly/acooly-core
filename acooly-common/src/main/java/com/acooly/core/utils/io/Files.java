/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-05-08 23:32
 */
package com.acooly.core.utils.io;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.FileOperateErrorCodes;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * 文件操作工具类扩展 from apache common io
 *
 * @author zhangpu
 * @date 2021-05-08 23:32
 */
@Slf4j
@SuppressWarnings("deprecation")
public abstract class Files extends FileUtils {


	public static final List<String> DANGER_FS_PATHS = Lists.newArrayList(
            "/etc/", "/bin/", "/sbin/", "/dev/", "/usr/", "/sys/", "/root/", "/proc"
    );

    /**
     * 判断路径是否安全
     *
     * @param path
     * @return true 安全 false 不安全
     */
    public static boolean isSafe(String path, boolean silence) {
        // 判断是否文件系统根目录
        File[] roots = File.listRoots();
        for (File root : roots) {
            if (Strings.equalsIgnoreCase(root.getPath(), path)) {
                log.info("文件安全判断 false {} path: {}", FileOperateErrorCodes.OPT_UN_SAFETY_FS_ROOT.message(), path);
                if (silence) {
                    return false;
                } else {
                    throw new BusinessException(FileOperateErrorCodes.OPT_UN_SAFETY_FS_ROOT);
                }
            }
        }

        // 判断是否操作系统目录
        for (String dangerPrefix : DANGER_FS_PATHS) {
            if (Strings.startsWith(path, dangerPrefix)) {
                log.info("文件安全判断 false {} path: {}", FileOperateErrorCodes.OPT_UN_SAFETY_OS_DIR.message(), path);
                if (silence) {
                    return false;
                } else {
                    throw new BusinessException(FileOperateErrorCodes.OPT_UN_SAFETY_OS_DIR);
                }
            }
        }
        return true;
    }

    public static boolean isSafe(String path) {
        return isSafe(path, true);
    }

    public static boolean isSafe(File file, boolean silence) {
        return isSafe(file.getPath(), silence);
    }

    public static boolean isSafe(File file) {
        return isSafe(file.getPath());
    }

    /**
     * 安全静默删除文件
     *
     * @param file
     */
    public static void deleteSafely(File file) {
        if (!file.exists()) {
            return;
        }
        if (!isSafe(file.getPath())) {
            return;
        }
        deleteQuietly(file);
    }

    public static void deleteSafely(String filePath) {
        deleteSafely(new File(filePath));
    }

}
