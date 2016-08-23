/**
 * create by zhangpu
 * date:2015年2月26日
 */
package com.acooly.core.main;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author zhangpu
 *
 */
public class JarFileOpt {

	public static void main(String[] args) throws Exception {
		String classpath = "template/sample";
		String targetPath = "d:\\temp\\apidoc";
		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = patternResolver.getResources("classpath:" + classpath + "/**/*");

		if (resources != null && resources.length > 0) {
			for (Resource resource : resources) {
				String path = resource.getURL().getPath();
				String targetFile = targetPath + StringUtils.substringAfterLast(path, classpath);
				System.out.println(targetFile);
			}

		}
	}

}
