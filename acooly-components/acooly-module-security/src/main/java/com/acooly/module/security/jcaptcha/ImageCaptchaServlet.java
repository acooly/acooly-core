package com.acooly.module.security.jcaptcha;

import java.awt.image.BufferedImage;
import java.io.IOException;


import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Servlet generates CAPTCHA jpeg images based on the JCAPTCHA package. It's
 * configured via spring, and requires a ImageCaptchaService bean with the
 * id=imageCaptchaService
 * 
 * @author Jason Thrasher
 */
public class ImageCaptchaServlet extends HttpServlet {
	/**
	 * 版本
	 */
	private static final long serialVersionUID = 3258417209566116145L;

	@Getter
	@Setter
	private ImageCaptchaService imageCaptchaService;

	/** 回调执行 */
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {

		try {
			// get the session id that will identify the generated captcha.
			// the same id must be used to validate the response, the session id
			// is a good candidate!
			String captchaId = httpServletRequest.getSession().getId();
			// call the ImageCaptchaService getChallenge method
			BufferedImage challenge = imageCaptchaService.getImageChallengeForID(captchaId,
					httpServletRequest.getLocale());

			// flush it in the response
			httpServletResponse.setHeader("Cache-Control", "no-store");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setDateHeader("Expires", 0);
			httpServletResponse.setContentType("image/jpeg");
			ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
			ImageIO.write(challenge, "png", responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} catch (CaptchaServiceException e) {
			e.printStackTrace();
			httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

	}
}
