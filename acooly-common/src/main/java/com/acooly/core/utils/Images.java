package com.acooly.core.utils;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import sun.font.FontDesignMetrics;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 常用图片处理
 * <p>
 * 提供j简单通用的图片常用处理工具集。主要包括：
 * 1、缩放(缩略图)
 * 2、水印
 * 3、裁剪
 * 4、灰度
 *
 * @author zhangpu
 */
@Slf4j
public class Images {

    private static final String IMAGE_EXTS = "JPG,JPEG,PNG,GIF,BMP";

    public static final String IMAGE_PNG_EXT = "png";

    /**
     * 裁剪
     *
     * @param filePath 源文件
     * @param distPath 目标文件
     * @param x        从左上角开始的坐标x
     * @param y        从左上角开始的坐标y
     * @param w        裁剪宽度，为空则表示整个图片宽度
     * @param h        裁剪高度，为空则表示整个图片宽度
     */
    public static void clip(String filePath, String distPath, int x, int y, Integer w, Integer h) {
        try {
            BufferedImage source = loadImage(filePath);
            source = doClip(source, x, y, w, h);
            distPath = Strings.isBlankDefault(distPath, filePath);
            saveImage(distPath, source);
        } catch (Exception e) {
            log.warn("灰度 失败 {}", e.getMessage());
            throw Exceptions.runtimeException("灰度处理失败");
        }
    }


    /**
     * 灰度处理
     *
     * @param filePath 源文件路径
     * @param distPath 输出文件路径
     */
    public static void gray(String filePath, String distPath) {
        try {
            BufferedImage source = loadImage(filePath);
            doGray(source);
            distPath = Strings.isBlankDefault(distPath, filePath);
            saveImage(distPath, source);
        } catch (Exception e) {
            log.warn("灰度 失败 {}", e.getMessage());
            throw Exceptions.runtimeException("灰度处理失败");
        }
    }


    /**
     * 灰度处理
     * <p>
     * 覆盖filePath
     *
     * @param filePath 源文件路径
     */
    public static void gray(String filePath) {
        gray(filePath, filePath);
    }


    /**
     * 生成缩略图
     *
     * @param filePath 输入路径
     * @param target   输出路径
     * @param height   缩放的高度
     * @param width    缩放的宽度
     * @param bb       是否按比例缩放
     */
    public static void resize(String filePath, String target, int height, int width, boolean bb) {
        try {
            Assert.hasLength(filePath);
            File f = new File(filePath);
            if (!f.exists()) {
                throw new RuntimeException("filePath文件不存在");
            }
            target = Strings.isBlankDefault(target, filePath);
            BufferedImage bi = ImageIO.read(f);
            if (bb) {
                // 按比例
                double ratio = 1;
                // 计算比例
                if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                    if (bi.getHeight() > bi.getWidth()) {
                        Thumbnails.of(filePath).height(height).toFile(target);
                    } else {
                        Thumbnails.of(filePath).width(width).toFile(target);
                    }
                }
                Thumbnails.of(filePath).scale(ratio).toFile(target);
            } else {
                Thumbnails.of(filePath).size(width, height).toFile(target);
            }
        } catch (Exception e) {
            log.warn("图片缩放", e);
            throw new RuntimeException("图片缩放:" + e.getMessage());
        }
    }

    /**
     * 生成缩略图
     * 按指定的宽高强制缩放
     *
     * @param filePath
     * @param target
     * @param height
     * @param width
     */
    public static void resize(String filePath, String target, int height, int width) {
        resize(filePath, target, height, width, false);
    }

    /**
     * 生成缩略图
     * <p>
     * 支持按多种方式指定目标大小（ImageSize）
     * 1、强制大小
     * 2、按宽等比缩放
     * 3、按高等比缩放
     * 4、按比例等宽高缩放
     *
     * @param sourceFilePath
     * @param distFilePath
     * @param imageSize
     */
    public static void resize(@NotNull String sourceFilePath, @NotNull String distFilePath, @NotNull ImageSize imageSize) {
        try {
            Assert.hasLength(sourceFilePath);
            Assert.hasLength(distFilePath);
            Assert.notNull(imageSize);
            BufferedImage sourceImage = ImageIO.read(new File(sourceFilePath));
            BufferedImage distImage = doResize(sourceImage, imageSize, null);
            File file = new File(distFilePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            ImageIO.write(distImage, getImageFormat(distFilePath), file);
        } catch (Exception e) {
            log.warn("图片缩放 失败 {}", e.getMessage());
            throw Exceptions.runtimeException("图片缩放失败");
        }
    }


    /**
     * 添加水印
     * 图片输入输出片参数都为图片的全路径，水印图片为bufferedImage
     *
     * @param sourcePath         源文件全路径
     * @param distPath           输出文件全路径
     * @param watermarkImage     BufferedImage格式，以便兼容支持文字生成的缩略图
     * @param watermarkAngle     水印旋转角度（0-360）
     * @param watermarkOpacity   水印透明度（0-1.0）
     * @param watermarkImageSize 水印缩放大小
     * @param positions          水印文字
     */
    public static void watermark(@NotNull String sourcePath, @Nullable String distPath,
                                 @NotNull BufferedImage watermarkImage, @Nullable Double watermarkAngle, @Nullable Float watermarkOpacity,
                                 @Nullable ImageSize watermarkImageSize, @Nullable List<Position> positions) {
        try {
            Assert.hasLength(sourcePath);
            BufferedImage watermaskImage = doResize(watermarkImage, watermarkImageSize, watermarkAngle);
            if (watermarkOpacity == null) {
                watermarkOpacity = 1.0f;
            }
            if (Collections3.isEmpty(positions)) {
                positions = Lists.newArrayList(ImagePositions.BOTTOM_RIGHT);
            }

            Thumbnails.Builder<File> builder = Thumbnails.of(sourcePath).scale(1.0f);
            for (Position position : positions) {
                builder.watermark(position, watermaskImage, watermarkOpacity);
            }
            if (Strings.isBlank(distPath)) {
                distPath = sourcePath;
            }
            File file = new File(distPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            builder.toFile(distPath);
        } catch (Exception e) {
            throw Exceptions.runtimeException("图片水印失败", e);
        }
    }

    /**
     * 批量图片水印
     *
     * @param sourceFilePath     需要生成文件列表
     * @param distPath           目标存放目录（为空着在原目录替换）
     * @param watermarkImage
     * @param watermarkAngle
     * @param watermarkOpacity
     * @param watermarkImageSize
     * @param positions
     */
    public static void watermarkBatch(@NotNull String[] sourceFilePath, @Nullable String distPath,
                                      @NotNull BufferedImage watermarkImage, @Nullable Double watermarkAngle, @Nullable Float watermarkOpacity,
                                      @Nullable ImageSize watermarkImageSize, @Nullable List<Position> positions) {
        try {
            Assert.notEmpty(sourceFilePath);
            BufferedImage watermaskImage = doResize(watermarkImage, watermarkImageSize, watermarkAngle);
            if (watermarkOpacity == null) {
                watermarkOpacity = 1.0f;
            }
            if (Collections3.isEmpty(positions)) {
                positions = Lists.newArrayList(ImagePositions.BOTTOM_RIGHT);
            }

            Thumbnails.Builder<File> builder = Thumbnails.of(sourceFilePath).scale(1.0f);
            for (Position position : positions) {
                builder.watermark(position, watermaskImage, watermarkOpacity);
            }
            if (Strings.isBlank(distPath)) {
                builder.toFiles(Rename.NO_CHANGE);
            } else {
                File file = new File(distPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                builder.toFiles(file, Rename.NO_CHANGE);
            }
        } catch (Exception e) {
            throw Exceptions.runtimeException("图片水印失败", e);
        }
    }


    /**
     * 批量添加水印
     *
     * @param sourceFilePath     需要生成文件列表
     * @param distPath           目标存放目录（为空着在原目录替换）
     * @param watermarkPath
     * @param watermarkAngle
     * @param watermarkOpacity
     * @param watermarkImageSize
     * @param positions
     */
    public static void watermarkBatch(@NotNull String[] sourceFilePath, @Nullable String distPath,
                                      @NotNull String watermarkPath, @Nullable Double watermarkAngle, @Nullable Float watermarkOpacity,
                                      @Nullable ImageSize watermarkImageSize, @Nullable List<Position> positions) {
        BufferedImage watermaskImage = null;
        try {
            Assert.hasLength(watermarkPath);
            watermaskImage = ImageIO.read(new File(watermarkPath));
        } catch (Exception e) {
            throw Exceptions.runtimeException("图片水印:加载水印图片错误", e);
        }
        watermarkBatch(sourceFilePath, distPath, watermaskImage, watermarkAngle, watermarkOpacity, watermarkImageSize, positions);
    }


//    public static void watermarkBatch(@NotNull String sourcePath, @Nullable String distPath,
//                                      @NotNull String watermarkPath, @Nullable Double watermarkAngle, @Nullable Float watermarkOpacity,
//                                      @Nullable ImageSize watermarkImageSize, @Nullable List<Position> positions) {
//        BufferedImage watermaskImage = null;
//        List<String> sourceFilePath = Lists.newArrayList();
//        try {
//            Assert.hasLength(watermarkPath);
//            watermaskImage = ImageIO.read(new File(watermarkPath));
//            Collection<File> files = FileUtils.listFiles(new File(sourcePath), Strings.split(IMAGE_EXTS, ","), true);
//            if (Collections3.isEmpty(files)) {
//                log.warn("图片批量水印：源目录没有有效图片文件. sourcePath:{}", sourcePath);
//                throw new RuntimeException("图片批量水印：源目录没有有效图片文件");
//            }
//
//        } catch (Exception e) {
//            throw Exceptions.runtimeException("图片水印:加载水印图片错误", e);
//        }
//        watermarkBatch(sourceFilePath, distPath, watermaskImage, watermarkAngle, watermarkOpacity, watermarkImageSize, positions);
//    }


    /**
     * 添加水印 （Thumbnails）
     *
     * @param sourcePath         [必填] 源图片
     * @param distPath           [可选] 输出图片路径（为空则使用sourcePath）
     * @param watermarkPath      [必填] 水印图片地址
     * @param watermarkAngle     [可选] 水印图片角度（1-360）,为空则不旋转
     * @param watermarkOpacity   [可选] 水印图片透明度(0-1.0),为空着不透明
     * @param watermarkImageSize [可选] 水印图片缩放大小，为空着不缩放
     * @param positions          [可选] 水印图片位置，多个位置则添加多个对应的水印，为空则默认BOTTOM_RIGHT
     */
    public static void watermark(@NotNull String sourcePath, @Nullable String distPath,
                                 @NotNull String watermarkPath, @Nullable Double watermarkAngle, @Nullable Float watermarkOpacity,
                                 @Nullable ImageSize watermarkImageSize, @Nullable List<Position> positions) {
        BufferedImage watermaskImage = null;
        try {
            Assert.hasLength(watermarkPath);
            watermaskImage = ImageIO.read(new File(watermarkPath));
        } catch (Exception e) {
            throw Exceptions.runtimeException("图片水印:加载水印图片错误", e);
        }
        watermark(sourcePath, distPath, watermaskImage, watermarkAngle, watermarkOpacity, watermarkImageSize, positions);

    }


    /**
     * 添加水印基础方法（位置依赖：java.awt.Point）
     *
     * @param sourcePath
     * @param distPath
     * @param watermarkPath
     * @param opacity
     * @param watermarkImageSize
     * @param points
     */
    public static void watermark(@NotNull String sourcePath, @Nullable String distPath,
                                 @NotNull String watermarkPath, @Nullable Float opacity, @Nullable ImageSize watermarkImageSize,
                                 @Nullable Point[] points) {
        Assert.notEmpty(points);
        List<Position> positions = Lists.newArrayList();
        for (Point point : points) {
            positions.add(new Position() {
                @Override
                public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
                    return point;
                }
            });
        }
        watermark(sourcePath, distPath, watermarkPath, null, opacity, watermarkImageSize, positions);
    }

    /**
     * 添加水印
     * <p>
     * 1、覆盖原图
     * 2、水印图片无透明，角度和大小处理
     * 3、支持多个位置
     *
     * @param sourcePath
     * @param watermarkPath
     * @param positions
     */
    public static void watermark(@NotNull String sourcePath, @NotNull String watermarkPath, @Nullable List<Position> positions) {
        watermark(sourcePath, sourcePath, watermarkPath, null, null, null, positions);
    }


    /**
     * 添加水印
     * <p>
     * 1、覆盖原图
     * 2、水印图片无透明，角度和大小处理
     * 3、单个位置
     *
     * @param sourcePath
     * @param watermarkPath
     * @param position
     */
    public static void watermark(@NotNull String sourcePath, @NotNull String watermarkPath, @Nullable Position position) {
        if (position == null) {
            watermark(sourcePath, sourcePath, watermarkPath, null, null, null,
                    null);
        } else {
            watermark(sourcePath, sourcePath, watermarkPath, null, null, null,
                    Lists.newArrayList(position));
        }
    }

    /**
     * 添加文字水印
     *
     * @param sourcePath
     * @param distPath
     * @param watermarkText      文字水印内容
     * @param font               文字水印字体，默认：微软雅黑，BLOD,20
     * @param color              文字水印颜色，默认：LIGHT_GRAY
     * @param watermarkAngle
     * @param watermarkOpacity
     * @param watermarkImageSize
     * @param positions
     */
    public static void watermark(@NotNull String sourcePath, @Nullable String distPath,
                                 @NotNull String watermarkText, @Nullable Font font, @Nullable Color color,
                                 @Nullable Double watermarkAngle, @Nullable Float watermarkOpacity,
                                 @Nullable ImageSize watermarkImageSize, @Nullable List<Position> positions) {
        BufferedImage watermaskImage = null;
        try {
            Assert.hasLength(watermarkText);
            watermaskImage = buildTextImage(watermarkText, font, color);
        } catch (Exception e) {
            throw Exceptions.runtimeException("水印:生成文字水印图片错误", e);
        }
        watermark(sourcePath, distPath, watermaskImage, watermarkAngle, watermarkOpacity, watermarkImageSize, positions);
    }


    /**
     * 把图片印刷到图片上
     * <p>
     * 本方法直接采用java-image api操作，已被watermark方法代替
     *
     * @param pressImg  -- 水印文件
     * @param targetImg -- 目标文件
     * @param x
     * @param y
     */
    @Deprecated
    public static final void pressImage(String pressImg, String targetImg, int x, int y) {
        try {
            File file = new File(targetImg);
            Image src = ImageIO.read(file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            // 水印文件
            File fileBiao = new File(pressImg);
            Image srcBiao = ImageIO.read(fileBiao);
            int widethBiao = srcBiao.getWidth(null);
            int heightBiao = srcBiao.getHeight(null);
            g.drawImage(
                    srcBiao,
                    wideth - widethBiao - x,
                    height - heightBiao - y,
                    widethBiao,
                    heightBiao,
                    null);
            g.dispose();
            ImageIO.write(image, getImageFormat(targetImg), file);
        } catch (Exception e) {
            log.warn("添加图片水印失败", e);
            throw new RuntimeException("添加图片水印失败");
        }
    }

    /**
     * 添加水印
     * 本方法直接采用java-image api操作，已被watermark方法代替
     *
     * @param markImageStream 水印
     * @param targetImgFile   需要加水印的图片
     * @param x               x轴坐标
     * @param y               y轴坐标
     */
    @Deprecated
    public static final void pressImageFile(InputStream markImageStream, File targetImgFile, int x, int y) {
        try {
            Image src = ImageIO.read(targetImgFile);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);

            Image srcBiao = ImageIO.read(markImageStream);
            int widethBiao = srcBiao.getWidth(null);
            int heightBiao = srcBiao.getHeight(null);
            g.drawImage(
                    srcBiao,
                    wideth - widethBiao - x,
                    height - heightBiao - y,
                    widethBiao,
                    heightBiao,
                    null);
            g.dispose();
            ImageIO.write(image, getImageFormat(targetImgFile.getName()), targetImgFile);
        } catch (Exception e) {
            log.warn("添加图片水印失败", e);
            throw new RuntimeException("添加图片水印失败" + e.getMessage());
        }
    }

    /**
     * 添加水印
     * 本方法直接采用java-image api操作，已被watermark方法代替
     *
     * @param markImageStream 水印
     * @param targetImgPath   需要加水印的图片
     * @param x               x轴坐标
     * @param y               y轴坐标
     */
    @Deprecated
    public static final void pressImage(InputStream markImageStream, String targetImgPath, int x, int y) {
        pressImageFile(markImageStream, new File(targetImgPath), x, y);
    }

    /**
     * 添加水印文字
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
     * @param degree    角度 0-360
     */
    @Deprecated
    public static void pressText(String pressText, String targetImg,
                                 String fontName, int fontStyle, Color color, int fontSize,
                                 int x, int y, float alpha, Integer degree) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            if (IMAGE_PNG_EXT.equalsIgnoreCase(getImageFormat(targetImg))) {
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
            ImageIO.write(image, getImageFormat(targetImg), img);
        } catch (Exception e) {
            log.warn("生成文字水印失败", e);
            throw new RuntimeException("生成文字水印失败:" + e.getMessage());
        }
    }

    @Deprecated
    public static void pressText(String pressText, String targetImg) {
        pressText(pressText, targetImg, null, Font.BOLD, Color.LIGHT_GRAY, 14, 0, 0, 1f, 300);
    }

    @Deprecated
    public static final void pressImages(String pressImg, int w, String sourceImg, String targetImg, int type) {
        try {
            File file = new File(sourceImg);
            Image src = ImageIO.read(file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);

            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(src, 0, 0, wideth, height, null);

            // 水印文件
            File filebiao = new File(pressImg);
            Image srcBiao = ImageIO.read(filebiao);
            int widethBiao = srcBiao.getWidth(null);
            int heightBiao = srcBiao.getHeight(null);

            int h = Money.yuan(w * heightBiao).divide(widethBiao).getAmount().intValue();
            int padding = w;
            int margin = h;
            int space = w;
            int cols = (wideth - padding * 2) / (w + space) + 1;
            int rows = (height - margin * 2) / (h + space) + 1;

            int x = 0;
            int y = 0;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));
            for (int i = 0; i < rows; i++) {
                y = margin + (h + space) * i;
                for (int j = 0; j < cols; j++) {
                    x = padding + j * (w + space);
                    g.drawImage(srcBiao, x, y, w, h, null);
                }
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();
            ImageIO.write(image, getImageFormat(targetImg), new File(targetImg));
        } catch (Exception e) {
            log.warn("添加图片水印失败", e);
            throw new RuntimeException("添加图片水印失败");
        }
    }


    /**
     * 计算文字数量
     * <p>
     * 主要用于计算文字的打印长度，已废弃，采用FontDesignMetrics代替
     *
     * @param text 文字
     * @return 长度
     */
    @Deprecated
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


    public static boolean isImage(String fileName) {
        String ext = StringUtils.substringAfterLast(fileName, ".");
        if (Strings.indexOfIgnoreCase(IMAGE_EXTS, ext) != -1) {
            return true;
        }
        return false;
    }


    private static String getImageFormat(String targetImg) {
        String extention = StringUtils.substringAfterLast(targetImg, ".");
        if (extention.isEmpty()) {
            return "batch";
        } else {
            return extention;
        }
    }


    public static BufferedImage doResize(@NotNull BufferedImage bufferedImage, @NotNull ImageSize imageSize, @Nullable Double angle) throws
            Exception {
        Assert.notNull(bufferedImage);
        Thumbnails.Builder<BufferedImage> watermarkBuilder = Thumbnails.of(bufferedImage);
        if (imageSize == null) {
            imageSize = new ImageSize(1.0f);
        }
        if (imageSize.getImageSizeType() == ImageSizeType.fouce_size &&
                imageSize.getWidth() > 0 && imageSize.getHeight() > 0) {
            watermarkBuilder.forceSize(imageSize.getWidth(), imageSize.getHeight());
        }

        if (imageSize.getImageSizeType() == ImageSizeType.scale && imageSize.getScale() != null &&
                imageSize.getScale() > 0 && imageSize.getScale() <= 1.0f) {
            watermarkBuilder.scale(imageSize.getScale());
        }

        if (imageSize.getImageSizeType() == ImageSizeType.scale_width && imageSize.getWidth() > 0) {
            watermarkBuilder.width(imageSize.getWidth());
        }

        if (imageSize.getImageSizeType() == ImageSizeType.scale_height && imageSize.getHeight() > 0) {
            watermarkBuilder.height(imageSize.getHeight());
        }
        if (angle != null) {
            watermarkBuilder.rotate(angle);
        }
        return watermarkBuilder.asBufferedImage();
    }

    public static BufferedImage doResize(@NotNull BufferedImage bufferedImage, @NotNull ImageSize imageSize) throws
            Exception {
        Assert.notNull(bufferedImage);
        Assert.notNull(imageSize);
        Thumbnails.Builder<BufferedImage> watermarkBuilder = Thumbnails.of(bufferedImage);
        if (imageSize == null) {
            imageSize = new ImageSize(1.0f);
        }
        if (imageSize.getImageSizeType() == ImageSizeType.fouce_size &&
                imageSize.getWidth() > 0 && imageSize.getHeight() > 0) {
            watermarkBuilder.forceSize(imageSize.getWidth(), imageSize.getHeight());
        }

        if (imageSize.getImageSizeType() == ImageSizeType.scale && imageSize.getScale() != null &&
                imageSize.getScale() > 0 && imageSize.getScale() <= 1.0f) {
            watermarkBuilder.scale(imageSize.getScale());
        }

        if (imageSize.getImageSizeType() == ImageSizeType.scale_width && imageSize.getWidth() > 0) {
            watermarkBuilder.width(imageSize.getWidth());
        }

        if (imageSize.getImageSizeType() == ImageSizeType.scale_height && imageSize.getHeight() > 0) {
            watermarkBuilder.height(imageSize.getHeight());
        }
        return watermarkBuilder.asBufferedImage();
    }


    /**
     * 灰度
     *
     * @param bufferedImage 图片源
     */
    public static void doGray(BufferedImage bufferedImage) {
        ColorSpace graySpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ColorConvertOp convertToGrayOp = new ColorConvertOp(graySpace, null);
        convertToGrayOp.filter(bufferedImage, bufferedImage);
    }


    public static BufferedImage doClip(BufferedImage bufferedImage, int x, int y, Integer w, Integer h) throws Exception {
        if (w == null) {
            w = bufferedImage.getWidth();
        }
        if (h == null) {
            h = bufferedImage.getHeight();
        }
        return Thumbnails.of(bufferedImage).scale(1.0f).sourceRegion(x, y, w, h).asBufferedImage();
    }

    public static BufferedImage loadImage(String filePath) {
        try {
            Assert.hasLength(filePath);
            return ImageIO.read(ResourceUtils.getFile(filePath));
        } catch (Exception e) {
            log.warn("图片 加载失败 {}", e.getMessage());
            throw Exceptions.runtimeException("加载图片失败");
        }
    }

    public static void saveImage(String distPath, BufferedImage bufferedImage) {
        try {
            Assert.hasLength(distPath);
            ImageIO.write(bufferedImage, getImageFormat(distPath), ResourceUtils.getFile(distPath));
        } catch (Exception e) {
            log.warn("图片 保存失败 {}", e.getMessage());
            throw Exceptions.runtimeException("图片保存失败");
        }
    }


    /**
     * 生成文字对应的图片
     * @param text
     * @param font
     * @param color
     * @return BufferedImage图片对象
     */
    public static BufferedImage buildTextImage(String text, Font font, Color color) {
        if (color == null) {
            color = Color.LIGHT_GRAY;
        }
        if (font == null) {
            font = new Font("微软雅黑", Font.BOLD, 20);
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        float x = 0;
        float y = metrics.getAscent();
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += metrics.charWidth(text.charAt(i));
        }
        int height = metrics.getHeight();
        // PNG格式
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        //消除文字锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //消除画图锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
        g.dispose();
        return image;
    }


    /**
     * 图片规格
     */
    @Data
    public static class ImageSize {

        /**
         * 缩放比例
         * 0-1.0f
         */
        private Float scale;

        private int width;

        private int height;
        /**
         * 图片操作方式
         */
        private ImageSizeType imageSizeType;

        public ImageSize() {
        }

        public ImageSize(Float scale) {
            this.scale = scale;
            this.imageSizeType = ImageSizeType.scale;
        }

        public ImageSize(int width) {
            this.width = width;
            this.imageSizeType = ImageSizeType.scale_width;
        }

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
            this.imageSizeType = ImageSizeType.fouce_size;
        }
    }


    public static enum ImageSizeType {
        /**
         * 全图缩放
         */
        scale,

        /**
         * 按指定宽度缩放
         */
        scale_width,

        /**
         * 按指定高度缩放
         */
        scale_height,

        /**
         * 按指定宽高缩放
         */
        fouce_size

    }


}
