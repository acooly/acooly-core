package com.acooly.module.appopenapi.message;

import com.google.common.collect.Lists;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.message.ApiResponse;

import java.util.List;

/**
 * 欢迎信息 响应报文
 * 
 * @note 包括欢迎页面logo上的广告图和一组轮播欢迎导航图片
 * @author zhangpu
 *
 */
public class WelcomeInfoResponse extends ApiResponse {

	@OpenApiField(desc = "启动界面", constraint = "全URL，可直接访问. 返回空着不显示")
	private String welcome;

	@OpenApiField(desc = "导航图组", constraint = "全URL，可直接访问. 返回空着不显示")
	private List<String> guides = Lists.newArrayList();

	public void append(String url) {
		guides.add(url);
	}

	public List<String> getGuides() {
		return guides;
	}

	public void setGuides(List<String> guides) {
		this.guides = guides;
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

}