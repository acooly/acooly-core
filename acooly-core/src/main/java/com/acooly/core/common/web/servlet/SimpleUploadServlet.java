package com.acooly.core.common.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acooly.core.utils.Dates;

/**
 * 文件上传管理 Servlet
 * 
 * @author zhangpu
 * 
 */
public class SimpleUploadServlet extends HttpServlet {

	/** UID */
	private static final long serialVersionUID = -7580295765889900418L;

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleUploadServlet.class);

	/**
	 * 文件存储的服务器端根目录 存储路径规则: root/{getPathPrefix()}/yyyyMMdd/yyyyMMddHHmmdd.*
	 */
	private String root;
	/** 上传文件访问的URL虚拟路径前缀，可以是其他的媒体服务器地址（如果文件是写入媒体服务器的话） */
	private String accessRoot = "/";

	/** 允许的上传文件扩展名 */
	private String allowedExtensions = "txt,png,gif,jpg,jpeg,flv";
	private String uploadFieldName = "file";
	private boolean needRename = true;

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init...");
		if (StringUtils.isNotBlank(config.getInitParameter("root"))) {
			root = config.getInitParameter("root");
		}
		if (StringUtils.isNotBlank(config.getInitParameter("accessRoot"))) {
			accessRoot = config.getInitParameter("accessRoot");
		}
		if (StringUtils
				.isNotBlank(config.getInitParameter("allowedExtensions"))) {
			allowedExtensions = config.getInitParameter("allowedExtensions");
		}
		if (StringUtils.isNotBlank(config.getInitParameter("uploadFieldName"))) {
			uploadFieldName = config.getInitParameter("uploadFieldName");
		}
		if (StringUtils.isNotBlank(config.getInitParameter("needRename"))) {
			needRename = StringUtils.equalsIgnoreCase(
					config.getInitParameter("needRename"), "true") ? true
					: false;
		}
		logger.info("Init parameters [root:" + root + " ,accessRoot:"
				+ accessRoot + ", allowedExtensions:" + allowedExtensions
				+ ", uploadFieldName:" + uploadFieldName + " ,needRename="
				+ needRename + "]");
		super.init(config);
	}

	/**
	 * POST 上传文件
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("doPost...");
		try {
			File file = doUpload(request, response);
			postUpload(request, response, file);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
		}

	}

	protected File doUpload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String fileName = null;
		String filePath = getFilePath(request);
		// 使用Apache Common组件中的fileupload进行文件上传
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			@SuppressWarnings("rawtypes")
			List items = upload.parseRequest(request);
			Map<String, Object> fields = new HashMap<String, Object>();
			for (Object it : items) {
				FileItem item = (FileItem) it;
				if (item.isFormField()) {
					fields.put(item.getFieldName(), item.getString());
				} else {
					fields.put(item.getFieldName(), item);
				}
			}
			logger.info("request fileds --> " + fields);

			FileItem uploadFileItem = (FileItem) fields.get("upload");
			String requestFileName = uploadFileItem.getName();

			validateExtension(requestFileName);

			logger.info("requestFileName --> " + requestFileName);
			if (needRename) {
				fileName = getFileRename() + "."
						+ getFileExtension(requestFileName);
			} else {
				fileName = getFileName(requestFileName);
			}
			File pathFile = new File(filePath);
			if (!pathFile.exists()) {
				pathFile.mkdirs();
			}
			File pathToSave = new File(pathFile, fileName);
			uploadFileItem.write(pathToSave);
			return pathToSave;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 文件上传后的后置处理
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void postUpload(HttpServletRequest request,
			HttpServletResponse response, File uoloadFile) throws IOException {
	}

	protected String getFilePath(HttpServletRequest request) {
		String filePath = getStorageRoot(request);
		if (!StringUtils.endsWith(filePath, "/")) {
			filePath += "/";
		}
		filePath += getPathPrefix(request);
		if (!StringUtils.endsWith(filePath, "/")) {
			filePath += "/";
		}
		Date currentDate = new Date();
		filePath += Dates.format(currentDate, "yyyyMMdd");
		return filePath;
	}

	/**
	 * 获取文件上传存储的相对逻辑的前缀，该前缀是在物理根（root）和日期时间戳目录中间部分，用于根据业务定义子目录分割，默认为空.
	 * 
	 * @param request
	 * @return
	 */
	protected String getPathPrefix(HttpServletRequest request) {
		return "";
	}

	protected String getFileRename() {
		return Dates.format(Dates.DATETIME_NOT_SEPARATOR)
				+ RandomStringUtils.randomNumeric(4);
	}

	protected String getFileName(String requestFileName) {
		return StringUtils.substringAfterLast(requestFileName, "/");
	}

	/**
	 * 获取上传文件扩展名
	 * 
	 * @param request
	 * @return
	 */
	protected String getFileExtension(String requestFileName) {
		return StringUtils.substringAfterLast(requestFileName, ".");
	}

	protected void validateExtension(String requestFileName) {
		String extName = getFileExtension(requestFileName);
		if (!StringUtils.containsIgnoreCase(allowedExtensions, extName)) {
			logger.warn("Extension is not allowed : [" + extName
					+ "], we can support the following extensions:["
					+ allowedExtensions + "]");
			throw new RuntimeException("支持扩展名:" + extName + ", 目前支持扩展名："
					+ allowedExtensions);
		}
	}

	protected String getAccessUrl(HttpServletRequest request, File uploadFile) {
		String fileAccessUrl = getAccessRoot() + request.getContextPath();
		String localFilePath = uploadFile.getPath();
		String relativePath = StringUtils.substringAfter(localFilePath,
				getStorageRoot(request));
		relativePath = StringUtils.replace(relativePath, "\\", "/");
		return fileAccessUrl + relativePath;

	}

	private String getStorageRoot(HttpServletRequest request) {
		String filePath = root;
		if (StringUtils.startsWith(filePath, "contextPath:")) {
			filePath = StringUtils.substringAfter(filePath, "contextPath:");
			filePath = request.getSession().getServletContext()
					.getRealPath(filePath);
		}
		return filePath;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getAccessRoot() {
		return accessRoot;
	}

	public void setAccessRoot(String accessRoot) {
		this.accessRoot = accessRoot;
	}

	public String getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(String allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}

	public String getUploadFieldName() {
		return uploadFieldName;
	}

	public void setUploadFieldName(String uploadFieldName) {
		this.uploadFieldName = uploadFieldName;
	}

	public boolean isNeedRename() {
		return needRename;
	}

	public void setNeedRename(boolean needRename) {
		this.needRename = needRename;
	}

}
