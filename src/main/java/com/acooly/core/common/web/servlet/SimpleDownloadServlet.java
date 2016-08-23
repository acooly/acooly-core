package com.acooly.core.common.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDownloadServlet extends HttpServlet {

	/** UID */
	private static final long serialVersionUID = 3564889414796099465L;
	/** 日志 */
	private static final Logger logger = LoggerFactory
			.getLogger(SimpleDownloadServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String downloadFile = request.getParameter("file");
		if (StringUtils.isBlank(downloadFile)) {
			logger.warn("请求下载的文件参数[file]不存在");
			response.sendError(404, "请求下载的文件参数[file]不存在");
			return;
		}
		File file = new File(downloadFile);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = response.getOutputStream();
			IOUtils.copy(in, out);
			out.flush();
		} catch (Exception e) {
			logger.warn("请求下载文件失败", e);
			response.sendError(500, "请求下载文件失败");
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}

	}

}
