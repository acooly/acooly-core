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
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

/**
 * 系统环境相关的工具类
 * 合并Envs到Systems
 *
 * @author zhangpu
 * @date 2019-10-19 23:16
 * @see com.acooly.core.utils.system.Envs
 */
@Slf4j
public class Systems {


    /**
     * 获取当前进程Id
     *
     * @return 当前进程Id
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }

    /**
     * 获取系统常规信息
     * 包括：机器码、主机名、内网IP、操作系统名称、操作系统版本
     *
     * @return Map结构的系统信息组
     */
    public static Map<String, String> getSystemInfo() {
        Map<String, String> map = Maps.newHashMap();
        map.put("machineNo", Systems.getSystemId());
        map.put("hostName", Systems.getHostName());
        map.put("internalIp", IPUtil.getFirstNoLoopbackIPV4Address());
        Systems.OsPlatform platform = Systems.getOS();
        map.put("osName", platform.getOs().name() + "_" + platform.getArch());
        map.put("osVersion", platform.getVersion());
        return map;
    }

    /**
     * 获取HostName
     *
     * @return 主机名
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
     * md5(CPU序列号+主板序列号+MAC地址)
     *
     * @return 系统唯一标志
     */
    public static String getSystemId() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCpuId()).append(getMainboardId()).append(getMac());
        return Strings.upperCase(DigestUtils.md5Hex(sb.toString()));
    }


    /**
     * 执行操作系统命令
     *
     * @param cmd 系统命令数组
     * @return 执行结果
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
     * 执行操作系统命令
     *
     * @param cmd 系统命令
     * @return 执行结果
     */
    public static String exec(String cmd) {
        try (InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
             StringWriter stringWriter = new StringWriter()) {
            IOUtils.copy(in, stringWriter, Charset.defaultCharset());
            return stringWriter.toString();
        } catch (Exception e) {
            throw Exceptions.runtimeException("执行命令失败。命令: " + cmd + ", 错误: " + e.getMessage());
        }
    }

    /**
     * 获取执行命令行的匹配数据
     *
     * @param cmd    命令语句
     * @param label  要查看的字段
     * @param symbol 分隔符
     * @return 匹配数据
     */
    protected static String getExecValue(String cmd, String label, String symbol) {
        String execResult = exec(cmd);
        return doExecValue(execResult, label, symbol);
    }

    /**
     * 获取执行命令行的匹配数据
     *
     * @param cmds   命令语句数组
     * @param label  要查看的字段
     * @param symbol 分隔符
     * @return 匹配数据
     */
    protected static String getExecValue(String[] cmds, String label, String symbol) {
        String execResult = exec(cmds);
        return doExecValue(execResult, label, symbol);
    }

    /**
     * 执行VBS
     * windows下创建临时文件执行
     *
     * @param vbs vbs脚本
     * @return 执行结果
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
            Streams.closeQuietly(input);
        }
        return Strings.trimToEmpty(result);
    }


    /**
     * 操作系统信息
     *
     * @return 操作系统信息
     */
    public static OsPlatform getOS() {
        return new OsPlatform(OS.of(System.getProperty("os.name")), System.getProperty("os.arch"),
                System.getProperty("os.version"));
    }

    /**
     * 获取CPU序列号
     *
     * @return CPU序列号
     */
    public static String getCpuId() {
        return getOS().os.getCpuId();
    }

    /**
     * 获取硬盘序列号
     *
     * @return 硬盘序列号
     */
    public static String getDiskId() {
        return getOS().os.getDiskId();
    }

    /**
     * 获取主板序列号
     *
     * @return 主板序列号
     */
    public static String getMainboardId() {
        return getOS().os.getMainboardId();
    }

    /**
     * 获取MAC地址
     *
     * @return MAC地址
     */
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
                return Strings.trimToEmpty(sn[1]);
            }
        }
        return null;
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


    public static interface HardWareInfo {
        /**
         * CPU串号
         *
         * @return CPU串号
         */
        String getCpuId();

        /**
         * 主板串号
         *
         * @return 主板串号
         */
        String getMainboardId();

        /**
         * 硬盘串号
         *
         * @return 硬盘串号
         */
        String getDiskId();


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
