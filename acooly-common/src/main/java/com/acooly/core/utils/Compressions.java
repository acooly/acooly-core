package com.acooly.core.utils;

import com.acooly.core.utils.lang.Paths;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具
 *
 * @author zhangpu
 */
public class Compressions {

    private static final Logger logger = LoggerFactory.getLogger(Compressions.class);

    public static void zip(List<File> files, File target) {
        zip(files, null, target);
    }

    public static void zip(List<File> files, String zipRelativePath, File target) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        zip(files, zipRelativePath, out);
    }

    public static void zip(List<File> files, OutputStream out) {
        zip(files, null, out);
    }

    public static void zip(List<File> files, String zipRelativePath, OutputStream out) {
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(out);
            for (File file : files) {
                if (file.exists()) {
                    zip(file, zipRelativePath, zipOut, false);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("ZIP压缩失败 -> " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(zipOut);
        }
    }

    public static void zip(File source, File target, String password, boolean deleteSource) {
        List<File> sources = Lists.newArrayList(source);
        zip(sources, target);
        if (deleteSource) {
            source.delete();
        }
    }

    public static File zip(File source) {
        return zip(source, null);
    }

    public static File zip(File source, String password) {
        File target =
                new File(
                        source.getParentFile(),
                        StringUtils.substringBeforeLast(source.getName(), ".") + ".zip");
        zip(source, target, password, true);
        return target;
    }

    public static void zip(
            File source, String zipRelativePath, ZipOutputStream zipOut, boolean needCloseOutStream)
            throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(source);
            String entryName = Strings.trimToEmpty(zipRelativePath);
            entryName =
                    Strings.isBlank(entryName) ? source.getName() : entryName + "/" + source.getName();
            zipOut.putNextEntry(new ZipEntry(entryName));
            byte[] buf = new byte[1024];
            int num;
            while ((num = in.read(buf)) != -1) {
                zipOut.write(buf, 0, num);
                zipOut.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("Zip [" + source.getName() + "] fail. <" + e.getMessage() + ">");
        } finally {
            IOUtils.closeQuietly(in);
            if (needCloseOutStream) {
                IOUtils.closeQuietly(zipOut);
            }
        }
    }

    /**
     * 解压
     *
     * @param zipFilePath
     * @param destPath
     */
    public static List<File> unzip(String zipFilePath, String destPath, boolean cleanZipFile) {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            throw new RuntimeException("压缩文件不存在:" + zipFilePath);
        }

        if (Strings.isBlank(destPath)) {
            destPath = Paths.getSysTmpDir();
        }
        File destPathFile = new File(destPath);
        if (!destPathFile.exists()) {
            destPathFile.mkdirs();
        }

        ZipInputStream zin = null;
        try {
            zin = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = null;
            File zfile = null;
            File zPath = null;
            List<File> destFiles = Lists.newArrayList();
            while ((ze = zin.getNextEntry()) != null) {
                zfile = new File(destPath, ze.getName());
                zPath = new File(zfile.getParent());

                if (ze.isDirectory()) {
                    if (!zfile.exists()) {
                        zfile.mkdirs();
                    }
                    zin.closeEntry();
                } else {
                    if (!zPath.exists()) {
                        zPath.mkdirs();
                    }
                    FileOutputStream fouts = new FileOutputStream(zfile);
                    try {
                        IOUtils.copyLarge(zin, fouts);
                    } catch (Exception e) {
                        logger.error("解压写文件失败:{}", zfile.getPath());
                    } finally {
                        IOUtils.closeQuietly(fouts);
                    }
                    destFiles.add(zfile);
                    zin.closeEntry();
                }
            }
            logger.info("解压文件成功，路径:{}", destPath);
            if (cleanZipFile) {
                FileUtils.deleteQuietly(zipFile);
            }
            return destFiles;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(zin);
        }
    }

    /*public static void main(String[] args) {
        // File target = new
        // File("/Users/zhangpu/workspace/temp/pic/frzjf.zip");
        // File source = new
        // File("/Users/zhangpu/workspace/temp/pic/frzjf.jpg");
        // zip(Lists.newArrayList(new File[] { source }), target);

        List<File> destFiles = unzip("/Users/zhangpu/temp/messageFactory.zip", null, false);
        System.out.println(destFiles);
    }*/
}
