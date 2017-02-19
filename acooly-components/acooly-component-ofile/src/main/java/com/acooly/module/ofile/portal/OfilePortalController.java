/**
 * create by zhangpu
 * date:2015年5月16日
 */
package com.acooly.module.ofile.portal;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Images;
import com.acooly.core.utils.Strings;
import com.acooly.module.ofile.OFileProperties;
import com.acooly.module.ofile.auth.OFileUploadAuthenticate;
import com.acooly.module.ofile.domain.OnlineFile;
import com.acooly.module.ofile.enums.OFileType;
import com.acooly.module.ofile.service.OnlineFileService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * @author zhangpu
 *
 */
@Controller
@RequestMapping("/ofile")
public class OfilePortalController extends AbstractJQueryEntityController<OnlineFile, OnlineFileService> {
	private static final Logger logger = LoggerFactory.getLogger(OfilePortalController.class);
	private static final Pattern ABSOLUTE_URL = Pattern.compile("\\A[a-z0-9.+-]+://.*", 2);
	@Resource
	private OnlineFileService onlineFileService;
	@Autowired
	private OFileProperties oFileProperties;

	@Resource(name = "ofileUploadAuthenticateSpringProxy")
	private OFileUploadAuthenticate ofileUploadAuthenticate;

	@RequestMapping("upload")
	@ResponseBody
	public JsonListResult<OnlineFile> upload(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<OnlineFile> result = new JsonListResult<OnlineFile>();
		try {
			ofileUploadAuthenticate.authenticate(request);
			// 上传文件格式
			getUploadConfig().setAllowExtentions(oFileProperties.getAllowExtentions());
			getUploadConfig().setMaxSize(oFileProperties.getMaxSize());
			getUploadConfig().setStorageRoot(getStorageRoot());
			getUploadConfig().setUseMemery(false);
			Map<String, UploadResult> uploadResults = doUpload(request);
			UploadResult uploadResult = null;
			List<OnlineFile> onlineFiles = Lists.newArrayList();
			for (Map.Entry<String, UploadResult> entry : uploadResults.entrySet()) {
				uploadResult = entry.getValue();
				if (uploadResult == null || uploadResult.getSize() <= 0) {
					continue;
				}
				onlineFiles.add(fillOnlineFile(request, uploadResult));
			}
			if (onlineFiles.isEmpty()) {
				throw new RuntimeException("请求中没有可上传的文件");
			}
			onlineFileService.saves(onlineFiles);
			result.setRows(onlineFiles);
			result.setTotal((long) onlineFiles.size());
			result.appendData("serverRoot", oFileProperties.getServerRoot());
			logger.info("ofile文件上传成功。files:{}", onlineFiles.toString());
		} catch (Exception e) {
			handleException(result, "上传文件", e);
		}
		return result;
	}

	@RequestMapping("kindEditor")
	@ResponseBody
	public Map<String, Object> kindEditor(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = Maps.newHashMap();
		try {
			JsonListResult<OnlineFile> onlineFiles = upload(request, response);
			result.put("error", 0);
			result.put("url", oFileProperties.getServerRoot()+"/"
					+ Collections3.getFirst(onlineFiles.getRows()).getFilePath());
		} catch (Exception e) {
			result.put("error", 1);
			result.put("message", "文件上传失败:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 访问文件
	 */
	@RequestMapping("download/{id}")
	@ResponseBody
	public Object download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OnlineFile onlineFile = getEntityService().get(Long.valueOf(id));
		return doDownload(request, response, onlineFile.getOriginalName(), getStorageRoot() + onlineFile.getFilePath(),
				onlineFile.getFileType());
	}

	/**
	 * 访问图片
	 */
	@RequestMapping("image/{id}")
	@ResponseBody
	public Object image(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OnlineFile onlineFile = getEntityService().get(Long.valueOf(id));
		return doDownload(request, response, onlineFile.getOriginalName(), getStorageRoot() + onlineFile.getFilePath(),
				onlineFile.getFileType());
	}

	/**
	 * 访问缩略图
	 */
	@RequestMapping("thumb/{id}")
	@ResponseBody
	public Object thumbnail(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		OnlineFile onlineFile = getEntityService().get(Long.valueOf(id));
		return doDownload(request, response, onlineFile.getOriginalName(),
				getStorageRoot() + onlineFile.getThumbnail(), onlineFile.getFileType());
	}

	/**
	 * 通用下载（根据传入URL）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("download")
	@ResponseBody
	public Object download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("path");
		if (isAbsoluteUrl(path)) {
			path = new URL(path).getPath();
		}
		@SuppressWarnings("deprecation")
		// 兼容servlet2.4环境
		String filePath = request.getRealPath(path);
		File file = new File(filePath);
		if (!file.exists()) {
			file = new File(oFileProperties.getStorageRoot() + path);
		}
		if (!file.exists()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		return doDownload(request, response, file.getName(), file.getPath(),
				OFileType.with(FilenameUtils.getExtension(file.getName())));
	}
	private static boolean isAbsoluteUrl(String url) {
		return ABSOLUTE_URL.matcher(url).matches();
	}
	protected Object doDownload(HttpServletRequest request, HttpServletResponse response, String fileName,
			String filePath, OFileType fileType) {
		OutputStream out = null;
		InputStream in = null;
		try {
			checkReferer(request);
			doHeader(fileName, response, fileType);
			File file = new File(filePath);
			in = FileUtils.openInputStream(file);
			out = response.getOutputStream();
			IOUtils.copyLarge(in, out);
			out.flush();
			return null;
		} catch (Exception e) {
			JsonResult result = new JsonResult();
			handleException(result, "访问文件", e);
			return result;
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}

	protected OnlineFile fillOnlineFile(HttpServletRequest request, UploadResult uploadResult) {
		OnlineFile onlineFile = new OnlineFile();
		File file = uploadResult.getFile();
		onlineFile.setInputName(uploadResult.getParameterName());
		onlineFile.setFileExt(FilenameUtils.getExtension(file.getName()));
		onlineFile.setFileName(file.getName());
		onlineFile.setFilePath(getFilePath(request, file));
		onlineFile.setFileSize(file.length());
		onlineFile.setFileType(OFileType.with(onlineFile.getFileExt()));
		onlineFile.setModule(getModule(request));
		onlineFile.setMetadatas(getMetadata(request));
		onlineFile.setOriginalName(uploadResult.getName());
		onlineFile.setObjectId(DigestUtils.sha1Hex(UUID.randomUUID().toString()));
		onlineFile.setUserName(getSessionUser(request));
		if (onlineFile.getFileType() == OFileType.picture) {
			File thumbnailFile = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + "_thum."
					+ FilenameUtils.getExtension(file.getName()));
			Images.resize(file.getPath(), thumbnailFile.getPath(), oFileProperties.getThumbnailSize(),
					oFileProperties.getThumbnailSize(), false);
			onlineFile.setThumbnail(getFilePath(request, thumbnailFile));
		}
		return onlineFile;
	}

	protected void doHeader(String fileName, HttpServletResponse response, OFileType ofileType) {
		if (ofileType == OFileType.picture) {
			response.setContentType("image/jpeg");
		} else if (ofileType == OFileType.app) {
			response.setContentType("application/vnd.android.package-archive");
		} else {
			response.setContentType("application/octet-stream");
		}
		response.setHeader("Content-Disposition", "attachment");
		response.setHeader("Content-Disposition", "filename=\"" + Encodes.urlEncode(fileName, "UTF-8") + "\"");
	}

	protected String getSessionUser(HttpServletRequest request) {
		String userName = request.getParameter("userName");
		if (Strings.isNotBlank(userName)) {
			return userName;
		}
		String sessionKeys = oFileProperties.getCheckSessionKey();
		if (Strings.isBlank(sessionKeys)) {
			return null;
		}

		Object user = null;
		for (String key : sessionKeys.split(",")) {
			user = request.getSession().getAttribute(key);
			if (user != null) {
				return user.toString();
			}
		}
		return null;
	}

	protected String getMetadata(HttpServletRequest request) {
		String module = request.getParameter("metadata");
		return Strings.trimToNull(module);
	}

	protected String getModule(HttpServletRequest request) {
		String module = request.getParameter("module");
		return Strings.trimToNull(module);
	}

	protected String getFilePath(HttpServletRequest request, File file) {
		String filePath = file.getPath();
		filePath = filePath.replaceAll("\\\\", "/");
		return StringUtils.substringAfter(filePath, getStorageRoot());
	}

	protected String getStorageRoot() {
		return oFileProperties.getStorageRoot().replaceAll("\\\\", "/");
	}

	protected void checkReferer(HttpServletRequest request) {
		if (!oFileProperties.isCheckReferer()) {
			return;
		}
		try {
			String referer = request.getHeader(HttpHeaders.REFERER);
			if (Strings.isNotBlank(referer)) {
				String refererHost = new URL(referer).getHost();
				String serverHost = new URL(request.getRequestURL().toString()).getHost();
				if (!Strings.equals(refererHost, serverHost)) {
					throw new RuntimeException("非法访问");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}
}
