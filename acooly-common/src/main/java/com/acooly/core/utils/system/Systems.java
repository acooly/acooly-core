/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-10-19 23:16
 */
package com.acooly.core.utils.system;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.core.utils.Exceptions;
import com.acooly.core.utils.Strings;
import com.acooly.core.utils.io.Streams;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author zhangpu
 * @date 2019-10-19 23:16
 */
@Slf4j
public class Systems {


    /**
     * 获取HostName
     *
     * @return
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("获取HostName失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取系统唯一标志
     *
     * @return
     */
    public static String getSystemId() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCpuId()).append(getMainboardId()).append(getMac());
        return Strings.upperCase(DigestUtils.md5Hex(sb.toString()));
    }


    /**
     * 执行命令
     *
     * @param cmd
     * @return
     */
    public static String exec(String... cmd) {
        try (InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
             StringWriter stringWriter = new StringWriter()) {
            IOUtils.copy(in, stringWriter, Charset.defaultCharset());

            return stringWriter.toString();
        } catch (Exception e) {
            throw Exceptions.runtimeException("执行命令失败。命令: " + Arrays.toString(cmd) + ", 错误: " + e.getMessage());
        }
    }

    /**
     * 获取执行命令行的匹配数据
     *
     * @param cmd    命令语句
     * @param label  要查看的字段
     * @param symbol 分隔符
     * @return
     */
    protected static String getExecValue(String cmd, String label, String symbol) {
        String execResult = exec(cmd);
        return doExecValue(execResult, label, symbol);
    }

    protected static String getExecValue(String[] cmd, String label, String symbol) {
        String execResult = exec(cmd);
        return doExecValue(execResult, label, symbol);
    }

    /**
     * 执行VBS
     * windows下创建临时文件执行
     *
     * @param vbs
     * @return
     */
    public static String execVbs(String vbs) {
        String result = null;
        File file = null;
        BufferedReader input = null;
        try (StringWriter stringWriter = new StringWriter()) {
            file = File.createTempFile("tmp", ".vbs");
            FileUtils.write(file, vbs, Charset.defaultCharset(), false);
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            IOUtils.copyLarge(input, stringWriter);
            result = stringWriter.toString();
        } catch (Exception e) {
            log.warn("执行windows/vbs命令失败: {}", e.getMessage());
        } finally {
            if (file != null) {
                file.deleteOnExit();
            }
            Streams.close(input);
        }
        return Strings.trimToEmpty(result);
    }


    /**
     * 操作系统信息
     *
     * @return
     */
    public static OsPlatform getOS() {
        return new OsPlatform(OS.of(System.getProperty("os.name")), System.getProperty("os.arch"),
                System.getProperty("os.version"));
    }

    public static String getCpuId() {
        return getOS().os.getCpuId();
    }

    public static String getDiskId() {
        return getOS().os.getDiskId();
    }

    public static String getMainboardId() {
        return getOS().os.getMainboardId();
    }

    public static String getMac() {
        try {
            return IPUtil.getMACAddress();
        } catch (Exception e) {
            log.warn("获取MAC地址失败: {}", e.getMessage());
        }
        return null;
    }


    protected static String doExecValue(String execResult, String label, String symbol) {
        String[] infos = Strings.split(execResult, "\n");
        for (String info : infos) {
            info = Strings.trimToEmpty(info);
            if (!Strings.contains(info, label)) {
                continue;
            }
            String[] sn = Strings.split(info, symbol);
            if (sn != null && sn.length > 1) {
                return sn[1];
            }
        }
        return null;
    }

    public static interface HardWareInfo {
        /**
         * CPU串号
         *
         * @return
         */
        String getCpuId();

        /**
         * 主板串号
         *
         * @return
         */
        String getMainboardId();

        /**
         * 硬盘串号
         *
         * @return
         */
        String getDiskId();


    }


    /**
     * 操作系统
     */
    @Slf4j
    public static enum OS implements HardWareInfo {
        /**
         * OS类型
         */
        Linux {
            @Override
            public String getCpuId() {
                return getExecValue("dmidecode -t processor | grep 'ID'", "ID", ":");
            }

            @Override
            public String getMainboardId() {
                return doExecValue("dmidecode |grep 'Serial Number'", "Serial Number", ":");
            }

            @Override
            public String getDiskId() {
                return doExecValue("fdisk -l", "Disk identifier", ":");
            }

        },

        Windows {
            @Override
            public String getCpuId() {
                return getExecValue(new String[]{"wmic", "cpu", "get", "ProcessorId", "/value"}, "ProcessorId", "=");
            }

            @Override
            public String getMainboardId() {
                return getExecValue(new String[]{"wmic", "baseboard", "get", "serialnumber", "/value"}, "SerialNumber", "=");
            }

            @Override
            public String getDiskId() {
                return getExecValue(new String[]{"wmic", "DISKDRIVE", "where", "index=0", "get", "SerialNumber", "/value"}, "SerialNumber", "=");
            }

        },
        Mac {
            // system_profiler -listDataTypes
            @Override
            public String getCpuId() {
                return getExecValue(new String[]{"system_profiler", "SPHardwareDataType"}, "UUID", ":");
            }

            @Override
            public String getMainboardId() {
                return getExecValue(new String[]{"system_profiler", "SPHardwareDataType"}, "Serial Number", ":");
            }

            @Override
            public String getDiskId() {
                return getExecValue(new String[]{"system_profiler", "SPStorageDataType"}, "UUID", ":");
            }

        }, Other {
            @Override
            public String getCpuId() {
                return null;
            }

            @Override
            public String getMainboardId() {
                return null;
            }

            @Override
            public String getDiskId() {
                return null;
            }

        };

        public static OS of(String name) {
            if (Strings.containsIgnoreCase(name, Linux.name())) {
                return OS.Linux;
            } else if (Strings.containsIgnoreCase(name, Windows.name())) {
                return OS.Windows;
            } else if (Strings.containsIgnoreCase(name, Mac.name())) {
                return OS.Mac;
            } else {
                return OS.Other;
            }
        }


        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"cpuId\":\"").append(this.getCpuId()).append("\",");
            sb.append("\"mainboardId\":\"").append(this.getMainboardId()).append("\",");
            sb.append("\"diskId\":\"").append(this.getDiskId()).append("\"}");
            return sb.toString();
        }
    }

    @Getter
    @Setter
    public static class OsPlatform extends InfoBase {

        private OS os;
        private String arch;
        private String version;

        public OsPlatform() {
        }

        public OsPlatform(OS os, String arch, String version) {
            this.os = os;
            this.arch = arch;
            this.version = version;
        }
    }

}
