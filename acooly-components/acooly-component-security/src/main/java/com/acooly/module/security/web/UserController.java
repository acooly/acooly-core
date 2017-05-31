package com.acooly.module.security.web;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acooly.module.security.config.FrameworkPropertiesHolder;
import com.acooly.module.security.config.SecurityProperties;
import com.acooly.module.security.domain.Org;
import com.acooly.module.security.service.OrgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.module.security.domain.Role;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.RoleService;
import com.acooly.module.security.service.UserService;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping(value = "/manage/system/user")
@ConditionalOnProperty(value  = SecurityProperties.PREFIX + ".shiro.auth.enable",matchIfMissing = true)
public class UserController extends AbstractJQueryEntityController<User, UserService> {

	private static Map<Integer, String> allUserTypes = FrameworkPropertiesHolder.get().getUserTypes();
	private static Map<Integer, String> allStatus = FrameworkPropertiesHolder.get().getUserStatus();

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
    @Autowired
    private OrgService orgService;

	@RequestMapping(value = "alreadyExists")
	@ResponseBody
	public String alreadyExists(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		try {
			User user = getEntityService().getAndCheckUser(username);
			if (user != null) {
				return "true";
			} else {
				return "false";
			}
		} catch (Exception e) {
			return "true";
		}
	}

	@RequestMapping(value = "showChangePassword")
	public String showChangePassword(@ModelAttribute("loadEntity") User entity, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			model.addAttribute(getEntityName(), loadEntity(request));
		} catch (Exception e) {
			handleException("修改密码界面", e, request);
		}

		String url = getRequestMapperValue() + "Password";
		return url;
	}

	@RequestMapping(value = "changePassword")
	@ResponseBody
	public JsonResult changePassword(HttpServletRequest request, HttpServletResponse response, Model model) {
		String newPassword = request.getParameter("newPassword");
		JsonResult result = new JsonResult();
		try {
			User entity = loadEntity(request);
			getEntityService().changePassword(entity, newPassword);
			result.setMessage("密码修改成功");
		} catch (Exception e) {
			handleException(result, "修改用户密码", e);
		}
		return result;
	}

	@Override
	protected User doSave(HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate)
			throws Exception {
		User entity = loadEntity(request);
		if (entity == null) {
			entity = getEntityClass().newInstance();
		}
		doDataBinding(request, entity);
		onSave(request, response, model, entity, isCreate);
		if (isCreate) {
			getEntityService().createUser(entity);
		} else {
			getEntityService().updateUser(entity);
		}

		return entity;
	}

	@Override
	protected User onSave(HttpServletRequest request, HttpServletResponse response, Model model, User entity,
			boolean isCreate) throws Exception {
		entity.setRoles(loadRoleFormRequest(request));
		entity.setLastModifyTime(new Date());
        if (entity.getOrgId() != null) {
            Org organize = orgService.get(entity.getOrgId());
            entity.setOrgName(organize.getName());
        }
		return entity;
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
		model.put("allStatus", allStatus);
		model.put("allUserTypes", allUserTypes);
		List<Role> list = roleService.getAll();
		model.put("allRoles", list);
		model.put("PASSWORD_REGEX", FrameworkPropertiesHolder.get().getPasswordRegex());
		model.put("PASSWORD_ERROR", FrameworkPropertiesHolder.get().getPasswordError());
	}

	private Set<Role> loadRoleFormRequest(HttpServletRequest request) {
		String roleStr = request.getParameter("role");
		if (StringUtils.endsWith(roleStr, "|")) {
			roleStr = roleStr.substring(0, roleStr.length() - 1);
		}
		String[] roleArray = roleStr.split("\\|");
		List<String> rolelist = new ArrayList<String>();
		for (int i = 0; i < roleArray.length; i++) {
			rolelist.add(roleArray[i]);
		}
		Set<Role> roles = new HashSet<Role>();
		for (String roleid : rolelist) {
			Role role = roleService.get(Long.valueOf(roleid));
			roles.add(role);
		}
		return roles;
	}

	/**
	 * 不自动绑定对象中的roleList属性，另行处理。
	 */
	@InitBinder
	@Override
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("roles");
		super.initBinder(binder);
	}

}
