/**
 * create by zhangpu
 * date:2015年9月28日
 */
package com.acooly.core.common.web.support;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import freemarker.cache.TemplateLoader;

/**
 * Spring resource templateLoader
 * 
 * @author zhangpu
 * @date 2015年9月28日
 */
public class SpringResourceTemplateLoader implements TemplateLoader, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(SpringResourceTemplateLoader.class);

	private String templateLoaderPath;

	private ApplicationContext applicationContext;

	public Object findTemplateSource(String name) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for FreeMarker template with name [" + name + "]");
		}
		Resource resource = this.applicationContext.getResource(this.templateLoaderPath + name);
		return (resource.exists() ? resource : null);
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		Resource resource = (Resource) templateSource;
		try {
			return new InputStreamReader(resource.getInputStream(), encoding);
		} catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not find FreeMarker template: " + resource);
			}
			throw ex;
		}
	}

	public long getLastModified(Object templateSource) {
		Resource resource = (Resource) templateSource;
		try {
			return resource.lastModified();
		} catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not obtain last-modified timestamp for FreeMarker template in " + resource + ": "
						+ ex);
			}
			return -1;
		}
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

    public void setTemplateLoaderPath(String templateLoaderPath) {
        String temLoaderPath = templateLoaderPath;
        if (!templateLoaderPath.endsWith("/")) {
            temLoaderPath = templateLoaderPath + "/";
        }
        this.templateLoaderPath = temLoaderPath;
        this.templateLoaderPath = temLoaderPath;
    }

}
