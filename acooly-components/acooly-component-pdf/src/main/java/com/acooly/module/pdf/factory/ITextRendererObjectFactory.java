package com.acooly.module.pdf.factory;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.module.pdf.PdfProperties;
import com.acooly.module.pdf.exception.DocumentGeneratingException;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.core.io.Resource;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;


/**
 * ITextRenderer对象工厂,因为增加默认中文字体集(一个20M)，所以增加对象池
 *
 * @author shuijing
 */
public class ITextRendererObjectFactory extends BasePooledObjectFactory<ITextRenderer> {
    private static GenericObjectPool itextRendererObjectPool = null;
    private PdfProperties pdfProperties;
    private static final String fontsPath = System.getProperty("user.home")
        + File.separator + "appdata"
        + File.separator + "pdf"
        + File.separator + "fonts"
        + File.separator;
    private Map<String,Boolean> fontsCopy= Maps.newHashMap();

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
        //Resource imageResource = ApplicationContextHolder.get().getResource(pdfProperties.getImagePath());
        if (imageResource.exists()) {
            //iTextRenderer.getSharedContext().setBaseURL(imageResource.getFile().toURI().toURL().toExternalForm());
        }
    }

    /**
     * 添加字体
     */
    public ITextFontResolver addFonts(ITextRenderer iTextRenderer, PdfProperties pdfProperties)
        throws DocumentException, IOException {
        //添加默认中文字体
        ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
        //在jar中的文件不能获取绝对路径，拷贝到临时文件夹中加载
        addFonts("classpath:META-INF/fonts/SourceHanSerifSC-Regular.otf", fontResolver);
        addFonts("classpath:META-INF/fonts/SourceHanSerifSC-SemiBold.otf", fontResolver);
        //添加自定义字体
        Resource customFontsResource = pdfProperties.getResourceLoader().getResource(pdfProperties.getFontsPath());
        if (customFontsResource.exists()) {
            //addFonts(customFontsResource.getFile(), fontResolver);
        }
        return fontResolver;
    }

    private void addFonts(String jarFontsPath, ITextFontResolver fontResolver) throws IOException, DocumentException {
        InputStream is = null;
        BufferedOutputStream writer = null;
        FileOutputStream fos = null;
        try {
            is = pdfProperties.getResourceLoader().getResource(jarFontsPath).getInputStream();
            File fontsDataFile = getFontsDataFile(jarFontsPath.substring(jarFontsPath.lastIndexOf("/") + 1));
            Boolean isCopyExists = fontsCopy.get(jarFontsPath);
            if (isCopyExists == null || !isCopyExists) {
                fos = new FileOutputStream(fontsDataFile);
                writer = new BufferedOutputStream(fos);
                copyBytes(is, writer, 2048);
                fontsCopy.put(jarFontsPath, Boolean.TRUE);
            }
            fontResolver.addFont(fontsDataFile.getPath(), BaseFont.IDENTITY_H,
                BaseFont.NOT_EMBEDDED);
        } finally {
            if (is != null) {
                is.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }


    private void addFonts(File fontsDir, ITextFontResolver fontResolver) throws IOException, DocumentException {
        if (fontsDir != null && fontsDir.isDirectory()) {
            File[] files = fontsDir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f == null || f.isDirectory()) {
                    break;
                }
                fontResolver.addFont(f.getPath(), BaseFont.IDENTITY_H,
                    BaseFont.NOT_EMBEDDED);
            }
        }
    }

    private static File getFontsDataFile(String fontsName) {
        //目录不存在就创建目录
        final String s = fontsPath + fontsName;
        try {
            Files.createParentDirs(new File(s));
        } catch (IOException e) {
            throw new DocumentGeneratingException("创建字体文件夹失败", e);
        }
        return new File(s);
    }

    private void copyBytes(InputStream in, OutputStream out, int buffSize)
        throws IOException {
        PrintStream ps = out instanceof PrintStream ? (PrintStream) out : null;
        byte buf[] = new byte[buffSize];
        int bytesRead = in.read(buf);
        while (bytesRead >= 0) {
            out.write(buf, 0, bytesRead);
            if ((ps != null) && ps.checkError()) {
                throw new IOException("Unable to write to output stream.");
            }
            bytesRead = in.read(buf);
        }
        out.flush();
    }
}
