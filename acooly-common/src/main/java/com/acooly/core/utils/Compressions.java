package com.acooly.core.utils;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.io.Streams;
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
import java.util.zip.ZipFile;
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
            Streams.close(zipOut);
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
            Streams.close(in);
            if (needCloseOutStream) {
                Streams.close(zipOut);
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
                        Streams.close(fouts);
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
            Streams.close(zin);
        }
    }

    public static final String UNIX_FILE_SEPARATOR = "/";

    public static String readZipFile(String zipPath, String internalFilePath) {
        ZipFile zf = null;
        try {
            File zipFile = new File(zipPath);
            if (!zipFile.exists()) {
                logger.error("Zip文件不存在. zipPath: {}", zipPath);
                throw new RuntimeException("Zip文件不存在");
            }
            zf = new ZipFile(zipFile);
            if (Strings.startsWith(internalFilePath, UNIX_FILE_SEPARATOR)) {
                internalFilePath = Strings.removeFirst(internalFilePath, UNIX_FILE_SEPARATOR);
            }
            return Streams.toString(zf.getInputStream(zf.getEntry(internalFilePath)), "UTF-8");
        } catch (Exception e) {
            logger.error("抓取Zip内文件失败。 zipPath: {}, interFilePath:{},  error: {}", zipPath, internalFilePath, e);
            throw new BusinessException("抓取Zip内文件失败", "GRASP_ZIP_INTERNAL_FILE_FAIL");
        } finally {
            Streams.close(zf);
        }
    }

    public static String readZipFile(InputStream inputStream, String internalFilePath) {
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(inputStream);
            if (Strings.startsWith(internalFilePath, UNIX_FILE_SEPARATOR)) {
                internalFilePath = Strings.removeFirst(internalFilePath, UNIX_FILE_SEPARATOR);
            }
            ZipEntry entry = null;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (Strings.equals(entry.getName(), internalFilePath)) {
                    return Streams.toString(zipInputStream, "UTF-8");
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("抓取Zip流内文件失败。 interFilePath:{},  error: {}", internalFilePath, e);
            throw new BusinessException("抓取Zip流内文件失败", "GRASP_ZIP_INTERNAL_FILE_FAIL");
        } finally {
            Streams.close(zipInputStream);
        }
    }
}
