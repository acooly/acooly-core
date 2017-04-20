package com.acooly.module.pdf;

import java.io.*;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.acooly.core.common.boot.ApplicationContextHolder;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;


/**
 * pdf 生成器
 *
 * @author shuijing
 */
@Slf4j
public class PdfDocumentGenerator {

    @Autowired
    private PdfProperties pdfProperties;

    @Autowired
    private TaskExecutor taskExecutor;

    private Map<String, String> pdfTemplates = Maps.newConcurrentMap();

    /**
     * 生成pdf
     *
     * @param templateName   模板名(带后缀)
     * @param documentVo     数据对象
     * @param outputFilePath 输出流
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
    public void generate(String htmlContent, File outputFile) throws DocumentGeneratingException, FileNotFoundException {
        if (outputFile != null && !outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdir();
        }
        this.generate(htmlContent, new FileOutputStream(outputFile));
    }

    /**
     * 生成pdf
     *
     * @param htmlContent            html文本
     * @param outputFileOutputStream 输出流
     * @throws DocumentGeneratingException
     */
    public void generate(String htmlContent, FileOutputStream outputFileOutputStream) throws DocumentGeneratingException {
        taskExecutor.execute(() -> {
            OutputStream out = outputFileOutputStream;
            ITextRenderer iTextRenderer = null;
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(htmlContent.getBytes("UTF-8")));
                //获取对象池中对象
                iTextRenderer = ITextRendererObjectFactory.getObjectPool().borrowObject();
                try {
                    iTextRenderer.setDocument(doc, iTextRenderer.getSharedContext().getBaseURL());
                    iTextRenderer.layout();
                    iTextRenderer.createPDF(outputFileOutputStream);
                } catch (Exception e) {
                    ITextRendererObjectFactory.getObjectPool().invalidateObject(iTextRenderer);
                    iTextRenderer = null;
                    throw new DocumentGeneratingException(e);
                }
                out.flush();
                log.info("pdf文档生成成功!");
            } catch (Exception e) {
                log.error("pdf文档生成失败!", e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        //ig
                    }
                }
                if (iTextRenderer != null) {
                    try {
                        ITextRendererObjectFactory.getObjectPool().returnObject(iTextRenderer);
                    } catch (Exception ex) {
                        log.error("ITextRenderer对象池回收对象失败.", ex);
                    }
                }
            }
        });
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
            Resource resource = ApplicationContextHolder.get().getResource(ptp);
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