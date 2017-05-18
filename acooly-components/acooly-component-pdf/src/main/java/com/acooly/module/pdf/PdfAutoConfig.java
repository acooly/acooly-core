package com.acooly.module.pdf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import static com.acooly.module.pdf.PdfProperties.PREFIX;


/**
 * @author shuijing
 */
@Configuration
@EnableConfigurationProperties({PdfProperties.class})
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
public class PdfAutoConfig {

    @Bean
    public PDFService pdfService(PdfProperties pdfProperties){
        return new PDFService(pdfProperties);
    }
}