package com.acooly.module.security.web;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.module.security.domain.Resource;
import com.acooly.module.security.domain.Role;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.dto.ResourceNode;
import com.acooly.module.security.service.ResourceService;
import com.acooly.module.security.service.RoleService;

@Controller
@RequestMapping(value = "/manage/system/role")
public class RoleController extends AbstractJQueryEntityController<Role, RoleService> {

	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;

	@RequestMapping(value = { "getAllResourceNode" })
	@ResponseBody
	public JsonListResult<ResourceNode> getAllResourceNode(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<ResourceNode> result = new JsonListResult<ResourceNode>();
		String roleId = request.getParameter("roleId");
		try {
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			result.appendData(referenceData(request));
			List<ResourceNode> resources = null;

			if (user.getUserType() == User.USER_TYPE_ADMIN) {
				// 如果是超级管理员，则显示全部可选资源
				resources = resourceService.getAllResourceNode(Long.valueOf(roleId));
			} else {
				// 当前用户有权限的资源
				resources = resourceService.getAuthorizedResourceNode(user.getId(), Long.valueOf(roleId));
			}

			result.setTotal(Long.valueOf(resources.size()));
			result.setRows(resources);
		} catch (Exception e) {
			handleException(result, "查询所有数据", e);
		}
		return result;
	}

	@RequestMapping(value = { "listJson1" })
	@ResponseBody
	public String listJson1(HttpServletRequest request, HttpServletResponse response) {
		JsonListResult<Role> result = new JsonListResult<Role>();
		try {
			result.appendData(referenceData(request));
			PageInfo<Role> pageInfo = doList(request, response);
			result.setTotal(pageInfo.getTotalCount());
			result.setRows(pageInfo.getPageResults());
		} catch (Exception e) {
			handleException(result, "分页查询", e);
		}
		String resultBody = JsonMapper.nonDefaultMapper().toJson(result);
		return resultBody;
	}

	@Override
	protected Role onSave(HttpServletRequest request, HttpServletResponse response, Model model, Role entity,
			boolean isCreate) throws Exception {
		String resourceIds = request.getParameter("resourceIds");
		Set<Resource> resources = null;
		if (StringUtils.isNotBlank(resourceIds)) {
			String[] rids = StringUtils.split(resourceIds, ",");
			if (rids != null & rids.length > 0) {
				resources = new HashSet<Resource>();
				for (String resourceId : rids) {
					resources.add(resourceService.get(Long.valueOf(resourceId)));
				}
				entity.setRescs(resources);
			}
		}
		entity.setRescs(resources);
		return entity;
	}

	/**
	 * 删除前逻辑检查
	 * 
	 * 删除前检查是否已分配用户，如果已分配则不允许删除
	 */
	@Override
	protected void onRemove(HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
			throws Exception {
		Role role = null;
		for (Serializable id : ids) {
			role = getEntityService().get(id);
			if (role != null && Collections3.isNotEmpty(role.getUsers())) {
				throw new RuntimeException("角色已被用户使用，不能直接删除，请先解除该角色对应用户的关系");
			}
		}
	}

	@Override
	protected void referenceData(HttpServletRequest request, Map<String, Object> model) {
	}

}
