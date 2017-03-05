/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-05 10:51 创建
 */
package com.acooly.module.sms.sender.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Ids;
import com.acooly.module.sms.SmsProperties;
import com.acooly.module.sms.sender.ShortMessageSendException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@Service("emayShortMessageSender")
public class EmayShortMessageSender extends AbstractShortMessageSender {
	private final String SEND_URL999 = "http://sdk999ws.eucp.b2m.cn:8080/sdkproxy/sendtimesms.action";
	@Autowired
	private SmsProperties properties;
	private List<String> tagNames = Arrays.asList("error", "message");
	protected static final int SUCCESS_CODE = 0;
	
	@PostConstruct
	public void init() {
	}
	
	@Override
	public String send(String mobileNo, String content) {
		logger.debug("mobileNo:{}", mobileNo);
		content = getContent(content);
		
		String sn = properties.getEmay().getSn();
		String passwd = properties.getEmay().getPassword();
		List<NameValuePair> formparams = Lists.newArrayList();
		formparams.add(new BasicNameValuePair("cdkey", sn));
		formparams.add(new BasicNameValuePair("password", passwd));
		formparams.add(new BasicNameValuePair("seqid", Ids.getDid()));
		formparams.add(new BasicNameValuePair("phone", mobileNo));
		formparams.add(new BasicNameValuePair("addserial", ""));
		try {
			formparams.add(new BasicNameValuePair("message", new String(content.getBytes(), "utf-8")));
		} catch (UnsupportedEncodingException e) {
			logger.error("短信内容编码不支持" + e.getMessage());
		}
		HttpClient httpclient = new DefaultHttpClient();
		// 请求超时
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout / 2);
		// 读取超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout / 2);
		
		UrlEncodedFormEntity entity;
		try {
			try {
				entity = new UrlEncodedFormEntity(formparams, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				throw e1;
			}
			HttpPost httpPost = new HttpPost(SEND_URL999);
			httpPost.setEntity(entity);
			HttpResponse response = httpclient.execute(httpPost);
			String result = unmashall(response);
			logger.info("发送短信完成 {mobile:{},content:{},result:{}}", mobileNo, content, result);
			return result;
		} catch (Exception e) {
			logger.warn("发送短信失败 {号码:" + mobileNo + ",内容:" + content + "}, 原因:" + e.getMessage());
			throw new ShortMessageSendException("-1000", "", e.getMessage());
		}
	}
	
	protected String unmashall(HttpResponse result) {
		StatusLine statusLine = result.getStatusLine();
		if (statusLine.getStatusCode() != 200) {
			throw new BusinessException("http StatusCode=" + statusLine.getStatusCode());
		} else {
			Map<String, String> response;
			try {
				response = convertXML(
					org.apache.commons.lang.StringUtils.trim(EntityUtils.toString(result.getEntity())), tagNames);
			} catch (IOException e) {
				throw new BusinessException(e.getMessage(), e);
			}
			if (response != null) {
				String errorCode = response.get("error");
				String message = response.get("message");
				if (SUCCESS_CODE == Integer.parseInt(errorCode)) {
					logger.info("短信投递通道 {} 成功");
					return errorCode;
				} else {
					throw new BusinessException("errorCode=" + errorCode + ", message=" + message);
				}
			} else {
				throw new BusinessException("response null");
			}
		}
	}
	
	private Map<String, String> convertXML(String xml, List<String> tagNames) {
		Map<String, String> map = Maps.newHashMap();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			for (String tagName : tagNames) {
				String tagText = doc.getElementsByTagName(tagName).item(0).getTextContent();
				map.put(tagName, tagText);
			}
		} catch (Exception e) {
			logger.error("解析XML{}失败{}", xml, e.getMessage());
		}
		return map;
	}
	
	@Override
	public String send(List<String> mobileNos, String content) {
		return null;
	}
	
	private String getContent(String content) {
		return StringUtils.trimToEmpty(prefix) + content + StringUtils.trimToEmpty(posfix);
	}
	
	@Override
	public String getProvider() {
		return "亿美";
	}
}
