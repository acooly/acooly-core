package com.acooly.module.security.shiro.realm;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;

public class PathMatchPermissionResolver implements PermissionResolver {

	@Override
	public Permission resolvePermission(String permissionString) {
		return new PathMatchPermission(permissionString);
	}

}