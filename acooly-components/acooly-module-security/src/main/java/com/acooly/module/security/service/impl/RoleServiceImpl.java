package com.acooly.module.security.service.impl;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.security.dao.RoleDao;
import com.acooly.module.security.domain.Role;
import com.acooly.module.security.service.RoleService;
import com.acooly.module.security.shiro.realm.ShiroDbRealm;
import org.springframework.transaction.annotation.Transactional;


@Service("roleService")
@Transactional
public class RoleServiceImpl extends EntityServiceImpl<Role, RoleDao> implements RoleService {

	@Autowired
	private ShiroDbRealm shiroDbRealm;

	@Override
	public void save(Role o) throws BusinessException {
		super.save(o);
		try {
			shiroDbRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
		} catch (Exception e) {
			// ig
		}
	}

}
