package com.acooly.core.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Images {

    private static Logger logger = LoggerFactory.getLogger(Images.class);

    private static final String IMAGE_EXTS = "JPG,PNG,GIF,BMP";

    /**
     * 生成缩略图
     *
     * @param filePath
     * @param target
     * @param height
     * @param width
     * @param bb
     */
    public static void resize(String filePath, String target, int height, int width, boolean bb) {
        try {
            double ratio = 1; // 缩放比例
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
            }
            Thumbnails.of(filePath).scale(ratio).toFile(target);
        } catch (IOException e) {
            logger.warn("图片缩放", e);
            throw new RuntimeException("图片缩放");
        }
    }

    public static void resize(String filePath, String target, int height, int width) {
        resize(filePath, target, height, width, false);
    }

    /**
     * 把图片印刷到图片上
     *
     * @param pressImg  -- 水印文件
     * @param targetImg -- 目标文件
     * @param x
     * @param y
     */
    public static final void pressImage(String pressImg, String targetImg, int x, int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            // 水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(
                    src_biao,
                    wideth - wideth_biao - x,
                    height - height_biao - y,
                    wideth_biao,
                    height_biao,
                    null);
            g.dispose();
            ImageIO.write(image, getImageFormat(targetImg), _file);
        } catch (Exception e) {
            logger.warn("添加图片水印失败", e);
            throw new RuntimeException("添加图片水印失败");
        }
    }

    /**
     * 文字水印
     *
     * @param pressText 水印文字
     * @param targetImg 目标图片
     * @param fontName  字体名称
     * @param fontStyle 字体样式
     * @param color     字体颜色
     * @param fontSize  字体大小
     * @param x         修正值
     * @param y         修正值
     * @param alpha     透明度 0-1
     * @param alpha     角度 0-360
     */
    public static void pressText(
            String pressText,
            String targetImg,
            String fontName,
            int fontStyle,
            Color color,
            int fontSize,
            int x,
            int y,
            float alpha,
            Integer degree) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            if (getImageFormat(targetImg).equalsIgnoreCase("png")) {
                image =
                        g.getDeviceConfiguration()
                                .createCompatibleImage(width, height, Transparency.TRANSLUCENT);
                g.dispose();
                g = image.createGraphics();
            }
            g.drawImage(src, 0, 0, width, height, null);
            if (null != degree) {
                // 设置水印旋转
                g.rotate(
                        Math.toRadians(degree), (double) image.getWidth() / 2, (double) image.getHeight() / 2);
            }
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(
                    pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize + y));
            g.dispose();
            ImageIO.write((BufferedImage) image, getImageFormat(targetImg), img);
        } catch (Exception e) {
            logger.warn("生成文字水印失败", e);
            throw new RuntimeException("生成文字水印失败");
        }
    }

    public static void pressText(String pressText, String targetImg) {
        pressText(pressText, targetImg, null, Font.BOLD, Color.LIGHT_GRAY, 14, 0, 0, 1f, 300);
    }

    public static boolean isImage(String fileName) {
        String ext = StringUtils.substringAfterLast(fileName, ".");
        if (Strings.indexOfIgnoreCase(IMAGE_EXTS, ext) != -1) {
            return true;
        }
        return false;
    }


    public static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    private static String getImageFormat(String targetImg) {
        String extention = StringUtils.substringAfterLast(targetImg, ".");
        if (extention.isEmpty()) {
            return "jpg";
        } else {
            return extention;
        }
    }
}
