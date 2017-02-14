/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-02-04 14:53 创建
 */

import com.acooly.coder.Generator;
import com.acooly.coder.generate.impl.DefaultCodeGenerateService;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author qiubo@yiji.com
 */
public class Coder {
	public static void main(String[] args) {
		DefaultCodeGenerateService service = (DefaultCodeGenerateService) Generator.getGenerator();
		//set workspace if possible
		if (StringUtils.isBlank(service.getGenerateConfiguration().getWorkspace())) {
			String workspace=getProjectPath() + "acooly-core-test";
			service.getGenerateConfiguration().setWorkspace(workspace);
		}
		//set root pacakge if possible
		if (StringUtils.isBlank(service.getGenerateConfiguration().getRootPackage())) {
			service.getGenerateConfiguration().setRootPackage(getRootPackage());
		}
		service.generateTable("dm_customer");
	}

	public static String getProjectPath() {
		String file = Coder.class.getClassLoader().getResource(".").getFile();
		String testModulePath = file.substring(0, file.indexOf(File.separatorChar + "target" + File.separatorChar));
		String projectPath = testModulePath.substring(0, testModulePath.lastIndexOf(File.separatorChar));
		return projectPath+File.separatorChar;
	}
	public static String getRootPackage() {
		return "com.acooly.core.test";
	}
}