//package com.acooly.core.test.utils;
//
//import com.acooly.core.utils.ImagePositions;
//import com.acooly.core.utils.Images;
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import net.coobird.thumbnailator.Thumbnails;
//import net.coobird.thumbnailator.geometry.Position;
//import net.coobird.thumbnailator.geometry.Positions;
//import org.junit.Before;
//import org.junit.Test;
//import sun.font.FontDesignMetrics;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.color.ColorSpace;
//import java.awt.image.BufferedImage;
//import java.awt.image.ColorConvertOp;
//import java.io.File;
//import java.util.List;
//
///**
// * @author zhangpu
// * @date 2019-01-18 19:06
// */
//@Slf4j
//public class ImagesTest {
//
//    String sourcePath = null;
//    String[] sourceFilePath = null;
//    String watermarkPath = null;
//
//
//    @Before
//    public void setup() {
//        sourcePath = getImagePath() + "source/sourceImage2.jpg";
//        sourceFilePath = new String[]{getImagePath() + "source/sourceImage.jpg",
//                getImagePath() + "source/sourceImage1.jpg",
//                getImagePath() + "source/sourceImage2.jpg"};
//        watermarkPath = getImagePath() + "ihunlizhe.png";
//    }
//
//
//    @Test
//    public void testGray() {
//        String distPath = getImagePath() + "/dist/gray.batch";
//        Images.gray(sourcePath, distPath);
//    }
//
//    @Test
//    public void testClip() {
//        String distPath = getImagePath() + "/dist/resize_clip1.batch";
//        Images.clip(sourcePath, distPath, 0, 0, 380, 210);
//        distPath = getImagePath() + "/dist/resize_clip2.batch";
//        Images.clip(sourcePath, distPath, 0, 0, null, 300);
//    }
//
//
//    @Test
//    public void testResizeWithScale() {
//        String distPath = getImagePath() + "/dist/resize_scale.batch";
//        Images.resize(sourcePath, distPath, new Images.ImageSize(0.2f));
//    }
//
//    @Test
//    public void testResizeWithWidth() {
//        String distPath = getImagePath() + "/dist/resize_width.batch";
//        Images.resize(sourcePath, distPath, new Images.ImageSize(380));
//    }
//
//    @Test
//    public void testResizeWithWidthAndHeight() {
//        String distPath = getImagePath() + "/dist/resize_width_height.batch";
//        Images.resize(sourcePath, distPath, new Images.ImageSize(380, 80));
//    }
//
//    @Test
//    public void testWatermarkSigle() {
//        String distPath = getImagePath() + "/dist/watermark_sigle.batch";
//        doWatermark(sourcePath, distPath, watermarkPath, 330d, 0.4f, null, Lists.newArrayList(Positions.BOTTOM_RIGHT));
//    }
//
//    @Test
//    public void testWatermarkFullStandard() {
//        String distPath = getImagePath() + "/dist/watermark_full.batch";
//        List<Position> positions = Lists.newArrayList(ImagePositions.values());
//        doWatermark(sourcePath, distPath, watermarkPath, null, 0.4f, new Images.ImageSize(100), positions);
//        log.info("testWatermarkFullStandard distFilePath: \n{}", distPath);
//    }
//
//
//    @Test
//    public void testWatermarkMulti() {
//        long start = System.currentTimeMillis();
//        String distPath = getImagePath() + "/dist/watermark_multi.batch";
//        List<Position> positions = Lists.newArrayList(
//                ImagePositions.TOP_LEFT, ImagePositions.TOP_CENTER, ImagePositions.TOP_RIGHT,
//                ImagePositions.ONE_QUARTER_ONE_QUARTER, ImagePositions.ONE_QUARTER_THREE_QUARTER,
//                ImagePositions.CENTER_LEFT, ImagePositions.CENTER, ImagePositions.CENTER_RIGHT,
//                ImagePositions.THREE_QUARTER_ONE_QUARTER, ImagePositions.THREE_QUARTER_THREE_QUARTER,
//                ImagePositions.BOTTOM_LEFT, ImagePositions.BOTTOM_CENTER, ImagePositions.BOTTOM_RIGHT
//        );
//        Images.watermark(sourcePath, distPath, watermarkPath, 330d, null, null, positions);
//        log.info("testWatermarkMulti times: {}", System.currentTimeMillis() - start);
//    }
//
//    @Test
//    public void testBatchWatermarkMulti() {
//        long start = System.currentTimeMillis();
//        String distPath = getImagePath() + "/dist/batch";
//        List<Position> positions = Lists.newArrayList(
//                ImagePositions.TOP_LEFT, ImagePositions.TOP_CENTER, ImagePositions.TOP_RIGHT,
//                ImagePositions.ONE_QUARTER_ONE_QUARTER, ImagePositions.ONE_QUARTER_THREE_QUARTER,
//                ImagePositions.CENTER_LEFT, ImagePositions.CENTER, ImagePositions.CENTER_RIGHT,
//                ImagePositions.THREE_QUARTER_ONE_QUARTER, ImagePositions.THREE_QUARTER_THREE_QUARTER,
//                ImagePositions.BOTTOM_LEFT, ImagePositions.BOTTOM_CENTER, ImagePositions.BOTTOM_RIGHT
//        );
//        Images.watermarkBatch(sourceFilePath, null, watermarkPath, 330d, null, null, positions);
//        log.info("testBatchWatermarkMulti times: {}", System.currentTimeMillis() - start);
//    }
//
//
//    @Test
//    public void testWatermarkCustom() {
//        String distPath = getImagePath() + "/dist/watermark_custom.batch";
//
//        int padding = 10;
//        List<Position> positions = Lists.newArrayList();
//        positions.add(new Position() {
//            @Override
//            public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
//                int hPadding = (enclosingWidth - (enclosingWidth / width) * width) / 2;
//                int vPadding = (enclosingHeight - (enclosingHeight / height) * height) / 2;
//
//                return new Point(hPadding, vPadding);
//            }
//        });
//        positions.add(new Position() {
//            @Override
//            public Point calculate(int enclosingWidth, int enclosingHeight, int width, int height, int insetLeft, int insetRight, int insetTop, int insetBottom) {
//                int hPadding = (enclosingWidth - (enclosingWidth / width) * width) / 2;
//                int vPadding = (enclosingHeight - (enclosingHeight / height) * height) / 2;
//                int cols = (enclosingWidth - padding * 2) / width;
//                System.out.println(cols);
//                return new Point(hPadding + width * 2, vPadding);
//            }
//        });
//
//
//        doWatermark(sourcePath, distPath, watermarkPath, null, 0.4f, null, positions);
//        log.info("testWatermarkFull distFilePath: {}", distPath);
//    }
//
//    @Test
//    public void testTextWatermarkFull() {
//        String distPath = getImagePath() + "/dist/watermark_text_full.batch";
//        List<Position> positions = Lists.newArrayList(ImagePositions.values());
//        String watermarkText = "Accoly框架";
//        Font font = new Font("微软雅黑", Font.BOLD, 20);
//        Color color = Color.LIGHT_GRAY;
//        Images.watermark(sourcePath, distPath, watermarkText, font, color,
//                330d, 0.4f, new Images.ImageSize(200), positions);
//        log.info("testWatermarkFullStandard distFilePath: \n{}", distPath);
//    }
//
//
//    /**
//     * 测试使用文字绘制水印图片
//     *
//     * @throws exception
//     */
//    @Test
//    public void testDrawWatermarkWithText() throws exception {
//        Color color = Color.WHITE;
//        Font font = new Font("微软雅黑", Font.BOLD, 20);
//        String text = "Acooly框架";
//        String distFile = getImagePath() + "dist/textWatermark.png";
//        // PNG格式
//        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
//        float x = 0;
//        float y = metrics.getAscent();
//        int width = 0;
//        for (int i = 0; i < text.length(); i++) {
//            width += metrics.charWidth(text.charAt(i));
//        }
//        int height = metrics.getHeight();
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = image.createGraphics();
//        //消除文字锯齿
//        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        //消除画图锯齿
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setColor(color);
//        g.setFont(font);
//        g.drawString(text, x, y);
//        g.dispose();
//        ImageIO.write(image, "png", new File(distFile));
//    }
//
//
//    @Test
//    public void testClipImageWithThumbnails() throws exception {
//        String distFile = getImagePath() + "dist/clip_test_Thumbnails.png";
//        int x = 0;
//        int y = 0;
//        int width = 200;
//        int heigth = 100;
//        Thumbnails.of(sourcePath).scale(1.0f).sourceRegion(x, y, width, heigth).toFile(distFile);
//    }
//
//    @Test
//    // todo
//    public void testCropWithThumbnails() throws exception {
//        String distFile = getImagePath() + "dist/crop_test_Thumbnails.png";
//        int x = 0;
//        int y = 0;
//        int w = 200;
//        int h = 100;
////
////
////        BufferedImage bufferedImage = ImageIO.read(new File(sourcePath));
////        int sourceWidth = bufferedImage.getWidth();
////        int sourceHeight = bufferedImage.getHeight();
//
//        Thumbnails.of(sourcePath).scale(1.0f).sourceRegion(x, y, w, h).toFile(distFile);
//        ;
//    }
//
//    @Test
//    public void testGrayWithColorConvertO() throws exception {
//        long start = System.currentTimeMillis();
//        String distPath = getImagePath() + "dist/gray_ColorConvertOp.batch";
//        BufferedImage source = ImageIO.read(new File(sourcePath));
//        ColorSpace gray_space = ColorSpace.getInstance(ColorSpace.CS_GRAY);
//        ColorConvertOp convert_to_gray_op = new ColorConvertOp(gray_space, null);
//        convert_to_gray_op.filter(source, source);
//        ImageIO.write(source, "batch", new File(distPath));
//        log.info("handle times: {}", (System.currentTimeMillis() - start));
//    }
//
//
//    protected void doWatermark(String sourcePath, String distPath, String watermarkPath, Double angle, Float opacity, Images.ImageSize
//            watermarkImageSize, List<Position> postions) {
//        Images.watermark(sourcePath, distPath, watermarkPath, angle, 0.4f, watermarkImageSize, postions);
//    }
//
//
//    public static String getImagePath() {
//        String file = ImagesTest.class.getClassLoader().getResource(".").getFile();
//        String testModulePath = file.substring(0, file.indexOf("/target/"));
//        return testModulePath + "/src/test/resources/image/";
//    }
//
//}
