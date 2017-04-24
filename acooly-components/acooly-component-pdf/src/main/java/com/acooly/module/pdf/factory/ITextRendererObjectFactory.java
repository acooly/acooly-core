package com.acooly.module.pdf.factory;

import com.acooly.module.pdf.PdfProperties;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.IOUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.core.io.Resource;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.zip.GZIPInputStream;


/**
 * ITextRenderer对象工厂,因为增加默认中文字体集(一个20M)，所以增加对象池
 *
 * @author shuijing
 */
public class ITextRendererObjectFactory extends BasePooledObjectFactory<ITextRenderer> {
    private static GenericObjectPool itextRendererObjectPool = null;
    private PdfProperties pdfProperties;

    public ITextRendererObjectFactory(PdfProperties pdfProperties) {
        this.pdfProperties = pdfProperties;
    }

    @Override
    public ITextRenderer create() throws Exception {
        ITextRenderer renderer = createTextRenderer();
        return renderer;
    }

    @Override
    public PooledObject wrap(ITextRenderer obj) {
        return new DefaultPooledObject<>(obj);
    }

    /**
     * 获取对象池,配置对象工厂
     */
    public static GenericObjectPool<ITextRenderer> getObjectPool(PdfProperties pdfProperties) {
        synchronized (ITextRendererObjectFactory.class) {
            if (itextRendererObjectPool == null) {
                itextRendererObjectPool = new GenericObjectPool<>(
                    new ITextRendererObjectFactory(pdfProperties));
                GenericObjectPoolConfig config = new GenericObjectPoolConfig();
                config.setLifo(false);
                config.setMaxTotal(15);
                config.setMaxIdle(15);
                config.setMinIdle(1);
                itextRendererObjectPool.setConfig(config);
            }
        }
        return itextRendererObjectPool;
    }

    /**
     * 初始化ITextRenderer对象
     */
    public synchronized ITextRenderer createTextRenderer()
        throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        setSharedContext(renderer, pdfProperties);
        addFonts(renderer, pdfProperties);
        return renderer;
    }

    private void setSharedContext(ITextRenderer iTextRenderer, PdfProperties pdfProperties) throws IOException {
        Resource imageResource = pdfProperties.getResourceLoader().getResource(pdfProperties.getImagePath());
        if (imageResource.exists()) {
            iTextRenderer.getSharedContext().setBaseURL(imageResource.getFile().toURI().toURL().toExternalForm());
        }
    }

    /**
     * 添加字体
     */
    public ITextFontResolver addFonts(ITextRenderer iTextRenderer, PdfProperties pdfProperties)
        throws DocumentException, IOException {
        //添加默认中文字体
        ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
        //Resource fontsResource = pdfProperties.getResourceLoader().getResource("classpath:META-INF/fonts");
        //在jar中的文件不能获取绝对路径，拷贝到临时文件夹中加载
        InputStream is = pdfProperties.getResourceLoader().getClassLoader().getResourceAsStream("META-INF/fonts/");
        BufferedInputStream bis = new BufferedInputStream(is);
        File pdfFontsTmp = File.createTempFile("pdfFontsTmp", "");
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(pdfFontsTmp));
        IOUtils.copy(bis, writer);

        //File fontsDir = fontsResource.getFile();
        addFonts(pdfFontsTmp, fontResolver);

        //添加自定义字体
        Resource customFontsResource = pdfProperties.getResourceLoader().getResource(pdfProperties.getFontsPath());
        if (customFontsResource.exists()) {
            addFonts(customFontsResource.getFile(), fontResolver);
        }
        return fontResolver;
    }

    private void addFonts(File fontsDir, ITextFontResolver fontResolver) throws IOException, DocumentException {
        if (fontsDir != null && fontsDir.isDirectory()) {
            File[] files = fontsDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f == null || f.isDirectory()) {
                    break;
                }
                fontResolver.addFont(f.getAbsolutePath(), BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED);
            }
        }
    }
}
