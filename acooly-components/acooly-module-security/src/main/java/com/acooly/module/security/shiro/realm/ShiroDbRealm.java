package com.acooly.module.security.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acooly.core.utils.Encodes;
import com.acooly.module.security.SecurityConstants;
import com.acooly.module.security.domain.Resource;
import com.acooly.module.security.domain.Role;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import org.springframework.transaction.annotation.Transactional;

public class ShiroDbRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	protected UserService userService;

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	@Transactional
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken captchaToken = (UsernamePasswordToken) token;
		User user = userService.getAndCheckUser(captchaToken.getUsername());
		if (user != null) {
			byte[] salt = Encodes.decodeHex(user.getSalt());
			logger.debug("load user info from database. token --> " + token);
			return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	@Transactional
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User sessionUser = (User) principals.getPrimaryPrincipal();
		User user = null;
		try {
			user = userService.findUserByUsername(sessionUser.getUsername());
		} catch (Exception e) {
			logger.info("根据登录名[" + sessionUser.getUsername() + "]获取用户失败:" + e.getMessage());
			return null;
		}

		String contextPath = "";
		try {
			WebDelegatingSubject wdSubject = (WebDelegatingSubject) SecurityUtils.getSubject();
			contextPath = ((HttpServletRequest) wdSubject.getServletRequest()).getContextPath();
		} catch (Exception e) {
			// ig
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Role role : user.getRoles()) {
			// 基于Role的权限信息
			info.addRole(role.getName());
			// 基于Permission的权限信息
			Set<Resource> resources = role.getRescs();
			Set<String> urls = new HashSet<String>();
			for (Resource resource : resources) {
				if (resource.getType().equalsIgnoreCase(SecurityConstants.RESOURCE_TYPE_URL)) {
					urls.add("*:" + getCanonicalResource(contextPath, resource.getValue()));
				} else if (resource.getType().equalsIgnoreCase(SecurityConstants.RESOURCE_TYPE_FUNCTION)) {
					urls.add(resource.getValue());
				}
			}
			info.addStringPermissions(urls);
		}
		return info;
	}

	private String getCanonicalResource(String contextPath, String resource) {
		return contextPath + StringUtils.left(resource, resource.lastIndexOf("/") + 1) + "*";
	}
}
