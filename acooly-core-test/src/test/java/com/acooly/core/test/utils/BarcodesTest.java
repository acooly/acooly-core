/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2023-06-14 14:58
 */
package com.acooly.core.test.utils;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Barcodes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 二维码条形码工具测试
 *
 * @author zhangpu
 * @date 2023-06-14 14:58
 */
@Slf4j
public class BarcodesTest {


    @Test
    public void testEncodeQRcode() {
        String content = "http://www.acooly.cn?username=张飞";
        int size = 300;
        String format = "png";
        String filePath = "/Users/zhangpu/temp/qrcode/acoolyX.png";

        try (FileOutputStream out = new FileOutputStream(filePath)) {
            Barcodes.encodeQRcode(content, "UTF-8", size, out, true);
        } catch (Exception e) {
            throw new BusinessException();
        }
    }

    @Test
    public void testEncodeQRcodeFile() {
        String content = "测试QRcode，有无中文，12123=username=张飞";
        int size = 300;
        String filePath = "/Users/zhangpu/temp/qrcode/acooly2.png";
        Barcodes.encodeQRcodeFile(content, size, filePath);
    }

}
