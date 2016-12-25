package com.acooly.module.security.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.module.security.SecurityConstants;
import com.acooly.module.security.domain.Portallet;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.PortalletService;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/manage/system/portallet")
public class PortalletController extends AbstractJQueryEntityController<Portallet, PortalletService> {

	private static Map<Integer, String> allCollapsibles = Maps.newTreeMap();
	static {
		allCollapsibles.put(1, "true");
		allCollapsibles.put(0, "false");
	}

	@Autowired
	private PortalletService portalletService;

	@Override
	protected Map<String, Object> getSearchParams(HttpServletRequest request) {
		Map<String, Object> map = super.getSearchParams(request);
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (user.getUserType() != User.USER_TYPE_ADMIN) {
			map.put("EQ_userName", user.getUsername());
		}
		return map;
	}

	@Override
	protected Portallet onSave(HttpServletRequest request, HttpServletResponse response, Model model, Portallet entity,
			boolean isCreate) throws Exception {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (user.getUserType() != User.USER_TYPE_ADMIN) {
			entity.setUserName(user.getUsername());
		} else {
			entity.setUserName(null);
		}
		return entity;
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allCollapsibles", allCollapsibles);
		model.put("allLoadModes", SecurityConstants.LOAD_MODE_MAPPING);
		model.put("allShowModes", SecurityConstants.SHOW_MODE_MAPPING);
	}

}
