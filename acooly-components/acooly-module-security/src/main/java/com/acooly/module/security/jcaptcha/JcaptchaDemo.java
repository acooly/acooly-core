/**
 * create by zhangpu
 * date:2015年2月24日
 */
package com.acooly.module.security.jcaptcha;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.acooly.core.utils.Ids;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * @author zhangpu
 *
 */
public class JcaptchaDemo {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/acooly/module/security/applicationContext-jcaptcha.xml");
		ImageCaptchaService imageCaptchaService = (ImageCaptchaService) context.getBean("imageCaptchaService");
		BufferedImage challenge = imageCaptchaService.getImageChallengeForID(Ids.getDid());

		FileOutputStream out = new FileOutputStream("D:\\temp\\jcapcha.png");
		ImageIO.write(challenge, "png", out);
		out.flush();
		out.close();
	}

}
