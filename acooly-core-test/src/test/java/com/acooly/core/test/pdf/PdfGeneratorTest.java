package com.acooly.core.test.pdf;

import com.acooly.module.pdf.PDFService;
import com.acooly.module.pdf.PdfProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;

import static com.acooly.core.test.web.PdfServletTest.getVO;

@Slf4j
public class PdfGeneratorTest {

    private PDFService pdfService;

    @Before
    public void setUp() throws Exception {
        PdfProperties pdfProperties = new PdfProperties();
        pdfProperties.setResourceLoader(new DefaultResourceLoader());
        pdfService = new PDFService(pdfProperties);
    }

    @Test
    public void test() throws Exception {
        //模板名
        String template = "pdftest.html";
        // 生成pdf路径
        File outputFile = File.createTempFile("PdfGeneratorTest", ".pdf");
        // 生成pdf
        pdfService.generate(template, getVO(), outputFile);
        log.info("生成pdf路径:{}", outputFile.getAbsolutePath());
        Thread.sleep(10000);
    }


    @Test
    public void testWartermark() throws Exception {
        pdfService.addWatermark(new File("C:\\Users\\shuijing\\AppData\\Local\\Temp\\PdfGeneratorTest8263544598473906703.pdf"), "版权所有 重庆笨熊科技");
        Thread.sleep(10000);
    }
}