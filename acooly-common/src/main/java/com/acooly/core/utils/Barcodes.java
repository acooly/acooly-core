package com.acooly.core.utils;

import com.acooly.core.common.exception.BusinessException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 条形码工具类
 *
 * <p>1、二维码采用QRcode 2、一维条形码采用CODE_128
 *
 * @author zhangpu
 */
public final class Barcodes {

  public static final String DEFAULT_ENCODING = "UTF-8";

  /**
   * 主方法用于测试
   *
   * @param args
   */
  /*public static void main(String[] args) throws Exception {
  		// QRcode二维码测试
  		String content = "http://www.woldd.com/app/app.jsp";
  		String encoding = "utf-8";
  		int size = 200;
  		File file = new File("d:\\temp\\woldd_android_online.png");
  		encodeQRcode(content, encoding, size, new FileOutputStream(file), true);
  		System.out.println("生成QRcode二维码图片：" + file.getPath());
  		String decodeContent = decode(file);
  		System.out.println("解码完成：" + decodeContent);

  		// 条形码测试
  //		content = "0123456789";
  //		File barcodeFile = new File("e:\\temp\\testBarcode.png");
  //		encodeBarcode(content, 200, 100, new FileOutputStream(barcodeFile), true);
  //		System.out.println("生成barcode条形码图片：" + barcodeFile.getPath());
  //		decodeContent = decode(barcodeFile);
  //		System.out.println("条形码解码完成：" + decodeContent);

  	}*/

  /**
   * 通用条形码生成
   *
   * @param content
   * @param encoding
   * @param formart
   * @param width
   * @param height
   * @return
   */
  public static BufferedImage encode(
      String content, String encoding, BarcodeFormat formart, int width, int height) {
    try {
      ConcurrentHashMap<EncodeHintType, String> hints = new ConcurrentHashMap<>();
      hints.put(EncodeHintType.CHARACTER_SET, encoding);
      BitMatrix bitMatrix = new MultiFormatWriter().encode(content, formart, width, height, hints);
      return toBufferedImage(bitMatrix);
    } catch (Exception e) {
      throw new BusinessException("生成条形码[" + formart + "]失败", e);
    }
  }

  /**
   * 创建QRcode二维码
   *
   * <p>创建后，可以输出到文件或HTTP-Response输出流
   * <li>servlet输出：ImageIO.write(image, "png", response.getOutputStream());
   * <li>文件输出：ImageIO.write(image, "png", file);
   *
   * @param content 二维码数据内容
   * @param encoding 编码格式
   * @param size QRcode一般是正方形的，表示边长
   * @return QRcode
   */
  public static BufferedImage encodeQRcode(String content, String encoding, Integer size) {
    return encode(content, encoding, BarcodeFormat.QR_CODE, size, size);
  }

  public static void encodeQRcode(
      String content,
      String encoding,
      Integer size,
      OutputStream output,
      boolean closeAfterComplete) {
    try {
      ImageIO.write(encodeQRcode(content, encoding, size), "png", output);
    } catch (Exception e) {
      throw new BusinessException("生成QRcode输出到流失败", e);
    } finally {
      if (closeAfterComplete) {
        quietlyClose(output);
      }
    }
  }

  public static BufferedImage encodeBarcode(String content, Integer width, Integer height) {
    try {
      return encode(content, DEFAULT_ENCODING, BarcodeFormat.CODE_128, width, height);
    } catch (Exception e) {
      throw new RuntimeException("生成条形码失败", e);
    }
  }

  public static void encodeBarcode(
      String str, Integer width, Integer height, OutputStream output, boolean closeAfterComplete) {
    try {
      ImageIO.write(encodeBarcode(str, width, height), "png", output);
    } catch (Exception e) {
      throw new BusinessException("输出条形码失败", e);
    } finally {
      if (closeAfterComplete) {
        quietlyClose(output);
      }
    }
  }

  /**
   * 解码
   *
   * @param in
   * @return
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
      throw new BusinessException("二维码解码失败", e);
    } finally {
      quietlyClose(in);
    }
  }

  public static String decode(File file) {
    InputStream in = null;
    try {
      in = new FileInputStream(file);
    } catch (Exception e) {
      throw new BusinessException("获取条形码/二维码文件输入流失败", e);
    }
    return decode(in);
  }

  /**
   * 转换成图片
   *
   * @param matrix
   * @return
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
