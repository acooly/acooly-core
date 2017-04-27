package com.acooly.module.pdf;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.exception.AppConfigException;
import com.acooly.core.utils.FreeMarkers;
import com.acooly.module.pdf.exception.DocumentGeneratingException;
import com.acooly.module.pdf.factory.ITextRendererObjectFactory;
import com.acooly.module.pdf.vo.DocumentVo;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * pdf 生成器
 *
 * @author shuijing
 */
@Slf4j
public class PDFService {

    private PdfProperties pdfProperties;

    //缓存 baseFont
    private Map baseFontMap = new HashMap(4);

    public PDFService(PdfProperties pdfProperties) {
        this.pdfProperties = pdfProperties;
    }

    private Map<String, String> pdfTemplates = Maps.newConcurrentMap();

    /**
     * 生成pdf
     *
     * @param templateName   模板名(带后缀)
     * @param documentVo     数据对象
     * @param outputFilePath 文件
     * @throws DocumentGeneratingException
     * @throws FileNotFoundException
     */
    public void generate(String templateName, DocumentVo documentVo,
                         String outputFilePath) throws DocumentGeneratingException, FileNotFoundException {
        File outputFile = new File(outputFilePath);
        this.generate(templateName, documentVo, outputFile);
    }

    /**
     * 生成pdf
     *
     * @param templateName 模板名(带后缀)
     * @param documentVo   数据对象
     * @param outputFile   输出流
     * @throws DocumentGeneratingException
     * @throws FileNotFoundException
     */
    public void generate(String templateName, DocumentVo documentVo,
                         File outputFile) throws DocumentGeneratingException, FileNotFoundException {
        generate(templateName, documentVo, new FileOutputStream(outputFile));
    }

    /**
     * 生成pdf
     *
     * @param templateName           模板名(带后缀)
     * @param documentVo             数据对象
     * @param outputFileOutputStream 输出流
     * @throws DocumentGeneratingException
     */
    public void generate(String templateName, DocumentVo documentVo,
                         FileOutputStream outputFileOutputStream) throws DocumentGeneratingException {
        try {
            Map<String, Object> dataMap = documentVo.getDataMap();
            String htmlContent = this.parseTemplate(templateName, dataMap);
            this.generate(htmlContent, outputFileOutputStream);
        } catch (Exception e) {
            String error = "pdf文档生成失败!";
            log.error(error);
            throw new DocumentGeneratingException(error, e);
        } finally {
            if (outputFileOutputStream != null) {
                try {
                    outputFileOutputStream.close();
                } catch (IOException e) {
                    //ig
                }
            }
        }
    }

    /**
     * 生成pdf
     *
     * @param templateName 模板名(带后缀)
     * @param params       模板参数
     * @param outputFile   输出文件
     * @throws DocumentGeneratingException
     */
    public void generate(String templateName, Map<String, Object> params,
                         File outputFile) throws DocumentGeneratingException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(outputFile);
            String htmlContent = this.parseTemplate(templateName, params);
            this.generate(htmlContent, os);
        } catch (Exception e) {
            String error = "pdf文档生成失败!";
            log.error(error);
            throw new DocumentGeneratingException(error, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    //ig
                }
            }
        }
    }

    /**
     * 生成pdf
     *
     * @param templateName 模板名(带后缀)
     * @param params       模板参数
     * @param outputStream 输出流
     * @throws DocumentGeneratingException
     */
    public void generate(String templateName, Map<String, Object> params,
                         OutputStream outputStream) throws DocumentGeneratingException {
        try {
            String htmlContent = this.parseTemplate(templateName, params);
            this.generate(htmlContent, outputStream);
        } catch (Exception e) {
            String error = "pdf文档生成失败!";
            log.error(error);
            throw new DocumentGeneratingException(error, e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //ig
                }
            }
        }
    }

    /**
     * 生成pdf
     *
     * @param htmlContent html文本
     * @param outputFile  输出文件
     * @throws DocumentGeneratingException
     * @throws FileNotFoundException
     */
    public void generate(String htmlContent, File outputFile) throws DocumentGeneratingException,
        FileNotFoundException {
        OutputStream os = null;
        try {
            if (outputFile != null && !outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdir();
            }
            os = new FileOutputStream(outputFile);
            this.generate(htmlContent, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    //ig
                }
            }
        }

    }

    /**
     * 生成pdf
     *
     * @param htmlContent  html文本
     * @param outputStream 输出流
     * @throws DocumentGeneratingException
     */
    public void generate(String htmlContent, OutputStream outputStream) throws DocumentGeneratingException {
        OutputStream out = outputStream;
        ITextRenderer iTextRenderer = null;
        GenericObjectPool<ITextRenderer> objectPool = ITextRendererObjectFactory.getObjectPool(pdfProperties);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(htmlContent.getBytes("UTF-8")));
            //获取对象池中对象
            iTextRenderer = objectPool.borrowObject();
            try {
                iTextRenderer.setDocument(doc, iTextRenderer.getSharedContext().getBaseURL());
                iTextRenderer.layout();
                iTextRenderer.createPDF(out);
            } catch (Exception e) {
                objectPool.invalidateObject(iTextRenderer);
                iTextRenderer = null;
                throw new DocumentGeneratingException(e);
            }
            out.flush();
            log.info("pdf文档生成成功!");
        } catch (Exception e) {
            log.error("pdf文档生成失败!", e);
        } finally {
            if (iTextRenderer != null) {
                try {
                    objectPool.returnObject(iTextRenderer);
                } catch (Exception ex) {
                    log.error("ITextRenderer对象池回收对象失败.", ex);
                }
            }
        }
    }


    /**
     * 添加水印
     *
     * @param src     需要加水印pdf路径,替换原来文件
     * @param markStr 水印内容
     * @throws IOException
     * @throws DocumentException
     */
    public void addWatermark(String src, String markStr) throws IOException, DocumentException {
        this.addWatermark(new File(src), markStr);
    }

    /**
     * 添加水印
     *
     * @param src     需要加水印pdf,替换原来文件
     * @param markStr 水印内容
     * @throws IOException
     * @throws DocumentException
     */
    public void addWatermark(File src, String markStr) throws IOException, DocumentException {
        InputStream is = null;
        OutputStream os = null;
        try {
            File tmpFile = File.createTempFile("pdfWatermarkTmp", ".pdf");
            is = new FileInputStream(src);
            os = new FileOutputStream(tmpFile);
            this.addWatermark(is, os, markStr);
            FileUtils.copyFile(tmpFile, src);
            FileUtils.deleteQuietly(tmpFile);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }

    }

    /**
     * 添加水印
     *
     * @param src     需要加水印pdf
     * @param dest    加好水印的pdf
     * @param markStr 水印内容
     * @throws IOException
     * @throws DocumentException
     */
    public void addWatermark(File src, File dest, String markStr) throws IOException, DocumentException {
        InputStream is = null;
        OutputStream os = null;
        try {
            os = new FileOutputStream(dest);
            is = new FileInputStream(src);
            this.addWatermark(is, os, markStr);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }

    }

    /**
     * 添加水印
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param markStr      水印内容
     * @throws IOException
     * @throws DocumentException
     */
    public void addWatermark(InputStream inputStream, OutputStream outputStream, String markStr) throws IOException, DocumentException {
        PdfReader reader = null;
        PdfStamper stamper = null;
        BaseFont base = getBaseFontByCache("sourceHanSerifSC-Regular");
        PdfContentByte content;
        try {
            reader = new PdfReader(inputStream);
            stamper = new PdfStamper(reader, outputStream);
            if (!StringUtils.isEmpty(markStr)) {
                int pageNum = reader.getNumberOfPages();
                for (int i = 1; i <= pageNum; i++) {
                    reader.getPageSize(i);
                    float x = reader.getPageSize(i).getWidth() / 2;
                    float y = reader.getPageSize(i).getHeight() / 2;
                    content = stamper.getUnderContent(i);
                    content.saveState();
                    PdfGState gs = new PdfGState();
                    gs.setFillOpacity(0.10f);
                    content.setGState(gs);
                    content.beginText();
                    content.setColorFill(Color.GRAY);
                    content.setFontAndSize(base, 60);
                    content.showTextAligned(Element.ALIGN_CENTER, markStr, x, y, 30);
                    content.endText();
                    content.fill();
                    content.restoreState();
                }
            }
            stamper.close();
            outputStream.close();
        } catch (Exception e) {
            throw new DocumentGeneratingException("pdf加水印失败:", e);
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {
                    //ig
                }
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private BaseFont getBaseFontByCache(String fontName) throws IOException, DocumentException {
        BaseFont baseFont = null;
        if (baseFontMap.containsKey(fontName)) {
            baseFont = (BaseFont) baseFontMap.get(fontName);
        } else {
            Resource fontsResource = pdfProperties.getResourceLoader().getResource("classpath:META-INF/fonts/SourceHanSerifSC-Regular.otf");
            if (fontsResource.exists()) {
                File fontsFile = fontsResource.getFile();
                if (!fontsFile.exists()) {
                    throw new DocumentGeneratingException("pdf加水印失败，默认字体不存在!");
                }
                baseFont = BaseFont.createFont(fontsFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            }
            baseFontMap.put(fontName, baseFont);
        }
        return baseFont;
    }


    /**
     * 解析freeMarker模板
     */
    protected String parseTemplate(String tempateName, Map<String, Object> data) {
        String template = getTemplatesString(tempateName);
        return FreeMarkers.rendereString(template, data);
    }

    /**
     * 缓存模板
     *
     * @param key 模板名
     * @return 模板内容
     */
    private String getTemplatesString(String key) {
        if (Apps.isDevMode()) {
            pdfTemplates.clear();
        }
        String t = pdfTemplates.get(key);
        if (Strings.isNullOrEmpty(t)) {
            String ptp = pdfProperties.getTemplatePath() + key;
            Resource resource = pdfProperties.getResourceLoader().getResource(ptp);
            if (resource.exists()) {
                try {
                    t = Resources.toString(resource.getURL(), Charsets.UTF_8);
                    pdfTemplates.put(key, t);
                } catch (IOException e) {
                    throw new AppConfigException("pdf模板不存在:" + ptp, e);
                }
            } else {
                throw new AppConfigException("pdf模板不存在:" + ptp);
            }
        }
        return t;
    }

}