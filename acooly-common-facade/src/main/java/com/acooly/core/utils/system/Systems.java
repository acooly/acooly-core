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
import com.google.common.base.Charsets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author zhangpu
 * @date 2019-10-19 23:16
 */
@Slf4j
public class Systems {


    /**
     * 执行命令
     *
     * @param cmd
     * @return
     */
    public static String exec(String cmd) {
        try (InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
             StringWriter stringWriter = new StringWriter()) {
            IOUtils.copy(in, stringWriter, Charsets.UTF_8);

            return stringWriter.toString();
        } catch (Exception e) {
            throw Exceptions.runtimeException("执行命令失败。命令: " + cmd + ", 错误: " + e.getMessage());
        }
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

    public static String getCpu() {

        OsPlatform osPlatform = getOS();

        switch (osPlatform.name) {
            case Mac:
                return getSerialNumber("system_profiler SPHardwareDataType", "UUID",":");
            case Linux:
                return getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
            default:
        }
        return null;
    }

    /**
     * @param cmd    命令语句
     * @param record 要查看的字段
     * @param symbol 分隔符
     * @return
     */
    public static String getSerialNumber(String cmd, String record, String symbol) {
        String execResult = exec(cmd);
        String[] infos = execResult.split("\n");

        for (String info : infos) {
            info = info.trim();
            if (info.indexOf(record) != -1) {
                info.replace(" ", "");
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }
        return null;
    }

    public static String getSerialNumber(String cmd, String symbol) {
        String execResult = exec(cmd);
        if (Strings.isBlank(execResult)) {
            return null;
        }
        String[] infos = Strings.split(execResult, symbol);
        if (infos != null && infos.length > 1) {
            return Strings.trimToEmpty(infos[1]);
        }
        return null;
    }

    public void windowCPU() {
        // windows
        try {
            long start = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(
                    new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            String serial = sc.next();
            System.out.println(property + ": " + serial);
            System.out.println("time:" + (System.currentTimeMillis() - start));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<String, String> snVo = new HashMap<String, String>();

        System.out.println("=============>for linux");
        String cpuid = getSerialNumber("dmidecode -t processor | grep 'ID'", "ID", ":");
        System.out.println("cpuid : " + cpuid);
        String mainboardNumber = getSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number", ":");
        System.out.println("mainboardNumber : " + mainboardNumber);
        String diskNumber = getSerialNumber("fdisk -l", "Disk identifier", ":");
        System.out.println("diskNumber : " + diskNumber);
        String mac = getSerialNumber("ifconfig -a", "ether", " ");

        snVo.put("cpuid", cpuid.toUpperCase().replace(" ", ""));
        snVo.put("diskid", diskNumber.toUpperCase().replace(" ", ""));
        snVo.put("mac", mac.toUpperCase().replace(" ", ""));
        snVo.put("mainboard", mainboardNumber.toUpperCase().replace(" ", ""));
        System.out.println(snVo);
    }

    public static void main(String[] args) throws Exception{

        log.info("System test...");
        log.info("OS: {}", getOS());
        log.info("CPU: {}", getCpu());

        Process process = Runtime.getRuntime().exec("system_profiler SPHardwareDataType");
        InputStream in = process.getInputStream();
        InputStream err = process.getErrorStream();

        StringWriter sw = new StringWriter();
        IOUtils.copy(in,sw, Charsets.UTF_8);
        System.out.println("OK:");
        System.out.println(sw.toString());

        StringWriter swErr = new StringWriter();
        IOUtils.copy(err,swErr, Charsets.UTF_8);
        System.out.println("ERR:");
        System.out.println(swErr.toString());
    }


    /**
     * 操作系统
     */
    public static enum OS {
        /**
         * OS类型
         */
        Linux, Windows, Mac, Other;

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
    }

    @Getter
    @Setter
    public static class OsPlatform extends InfoBase {


        private OS name;
        private String arch;
        private String version;

        public OsPlatform() {
        }

        public OsPlatform(OS name, String arch, String version) {
            this.name = name;
            this.arch = arch;
            this.version = version;
        }
    }

}
