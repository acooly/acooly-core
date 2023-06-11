package com.acooly.core.utils;

import com.acooly.core.common.exception.BusinessException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotBlank;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 条形码工具类
 * 提供生成、解析等功能
 *
 * <p>
 * <li>1、二维码采用QRcode </li>
 * <li>2、一维条形码采用CODE_128</li>
 *
 * @author zhangpu
 */
public final class Barcodes {

    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 通用码生成
     * 支持条形码和二维码
     *
     * @param content  [必选] 内容
     * @param encoding 编码，默认：UTF-8
     * @param format   条形码格式，默认：BarcodeFormat.CODE_128
     * @param width    [必选] 宽度，单位：像素
     * @param height   [必选] 高度，单位：像素
     * @return 条形码图片对象
     * @see BarcodeFormat
     */
    public static BufferedImage encode(@NotBlank String content, String encoding, BarcodeFormat format, int width, int height) {
        try {
            ConcurrentHashMap<EncodeHintType, String> hints = new ConcurrentHashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
            if (format == null) {
                format = BarcodeFormat.CODE_128;
            }
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, format, width, height, hints);
            return toBufferedImage(bitMatrix);
        } catch (Exception e) {
            throw new BusinessException("BARCODES_ENCODE_ERROR", "生成条形码[" + format + "]失败", e);
        }
    }

    /**
     * 生成二维码（QRcode）
     *
     * <p>创建后，可以输出到文件或HTTP-Response输出流
     *
     * <pre class="code">
     * BufferedImage image = encodeQRcode("http://www.acooly.cn", "UTF-8", 200);
     * // Servlet输出
     * ImageIO.write(image, "png", response.getOutputStream());
     * // OR 文件输出
     * ImageIO.write(image, "png", file);
     * </pre>
     *
     * @param content  二维码数据内容
     * @param encoding 编码格式
     * @param size     QRcode一般是正方形的，表示边长
     * @return QRcode的图片对象
     */
    public static BufferedImage encodeQRcode(String content, String encoding, Integer size) {
        return encode(content, encoding, BarcodeFormat.QR_CODE, size, size);
    }


    /**
     * 生成并输出二维码
     *
     * @param content            二维码数据内容
     * @param encoding           编码格式：默认UTF-8
     * @param size               QRcode一般是正方形的，表示边长，单位像素
     * @param output             输出的流
     * @param closeAfterComplete 完成输出后是否关闭流
     */
    public static void encodeQRcode(String content, String encoding, Integer size, OutputStream output, boolean closeAfterComplete) {
        try {
            ImageIO.write(encodeQRcode(content, encoding, size), "png", output);
        } catch (Exception e) {
            throw new BusinessException("QRCODE_ENCODE_ERROR", "生成QRcode输出到流失败", e);
        } finally {
            if (closeAfterComplete) {
                quietlyClose(output);
            }
        }
    }

    /**
     * 生成条形码
     *
     * @param content 数据内容
     * @param width   宽，单位：像素
     * @param height  高，单位：像素
     * @return 条形码图片对象
     */
    public static BufferedImage encodeBarcode(String content, Integer width, Integer height) {
        try {
            return encode(content, DEFAULT_ENCODING, BarcodeFormat.CODE_128, width, height);
        } catch (Exception e) {
            throw new RuntimeException("生成条形码失败", e);
        }
    }

    /**
     * 生成并输出条形码
     *
     * @param content            数据内容
     * @param width              宽，单位：像素
     * @param height             高，单位：像素
     * @param output             输出的流
     * @param closeAfterComplete 完成输出后是否关闭流
     */
    public static void encodeBarcode(String content, Integer width, Integer height, OutputStream output, boolean closeAfterComplete) {
        try {
            ImageIO.write(encodeBarcode(content, width, height), "png", output);
        } catch (Exception e) {
            throw new BusinessException("BARCODES_ENCODE_ERROR", "输出条形码失败", e);
        } finally {
            if (closeAfterComplete) {
                quietlyClose(output);
            }
        }
    }

    /**
     * 解码数据
     * 通用，支持条形码和二维码
     *
     * @param in 条形码/二维码图片输入流
     * @return 解码后的内容
     */
    public static String decode(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            // 解码设置编码方式为：utf-8，
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, DEFAULT_ENCODING);
            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            throw new BusinessException("BARCODES_DECODE_ERROR", "二维码解码失败", e);
        } finally {
            quietlyClose(in);
        }
    }

    /**
     * 解码文件
     * 通用，支持条形码和二维码
     *
     * @param file 条形码/二维码图片文件
     * @return 解码后的内容
     */
    public static String decode(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (Exception e) {
            throw new BusinessException("BARCODES_DECODE_ERROR", "获取条形码/二维码文件输入流失败", e);
        }
        return decode(in);
    }

    /**
     * 转换成图片
     *
     * @param matrix
     * @return BufferedImage, 图片对象
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return image;
    }

    private static void quietlyClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e2) {
            // ignore
        }
    }
}
