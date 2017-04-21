package com.acooly.module.pdf;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.File;
import java.util.Map;

@Slf4j
public class PdfGeneratorTest {
	
	private PDFService pdfService;
	
	@Before
	public void setUp() throws Exception {
		PdfProperties pdfProperties = new PdfProperties();
        pdfProperties.setResourceLoader(new DefaultResourceLoader());
		pdfService = new PDFService(pdfProperties,new SimpleAsyncTaskExecutor());
	}
	
	@Test
	public void test() throws Exception {
		
		// 模板对象,须继承AbstractDocumentVo
		PdfDemoVo pdfDemoVo = new PdfDemoVo();
		pdfDemoVo.setPolicyNo("0000000000000000000000000");
		pdfDemoVo.setHolderName("张三123abc");
		pdfDemoVo.setInsuredName("李四123abc");
		pdfDemoVo.setBeneficiaryName("测试受益人姓名");
		pdfDemoVo.setBranchName("北京");
		pdfDemoVo.setCompanyName("微软公司");
		pdfDemoVo.setDestination("英国,俄罗斯,冰岛,日内瓦,威尼斯小镇");
		pdfDemoVo.setHolderAdress("重庆市北部新区黄山大道中段9号木星科技大厦一区6楼");
		pdfDemoVo.setHolderPostCode("123456");
		pdfDemoVo.setInsuredBirthday("2013-11-10");
		pdfDemoVo.setInsuredIDNo("123456789012345678");
		pdfDemoVo.setInsuredName("爱新觉罗启星");
		pdfDemoVo.setInsuredPassportNo("测试护照号码123456789");
		pdfDemoVo.setInsuredPhone("13112345678");
		pdfDemoVo.setInsuredPingyinName("abcdefg");
		pdfDemoVo.setInsuredSex("女");
		pdfDemoVo.setIssueDate("2017-04-19");
		pdfDemoVo.setPeriod("十一年");
		pdfDemoVo.setPremium("1009.00");
		pdfDemoVo.setRelation("子女");
		pdfDemoVo.setRemarks("仅为测试");
		pdfDemoVo.setAccidentalSumInsured("150000");
		pdfDemoVo.setEmergencySumInsured("500000");
		pdfDemoVo.setMedicalSumInsured("220000");
		Map<String, String> names = Maps.newHashMap();
		names.put("shuijing", "水镜");
		pdfDemoVo.setNames(names);
		//模板名
		String template = "pdftest.html";
		// 生成pdf路径
		File outputFile = File.createTempFile("PdfGeneratorTest", "pdf");
		// 生成pdf
        pdfService.generate(template, pdfDemoVo, outputFile);
		log.info("生成pdf路径:{}", outputFile.getAbsolutePath());
		Thread.sleep(10000);
	}
	
	@Test
	public void testApp() {
		
	}
}