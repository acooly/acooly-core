package com.acooly.module.security.shiro.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PathMatchPermission implements Permission {

	private static final Logger logger = LoggerFactory.getLogger(PathMatchPermission.class);

	protected static final String PART_DIVIDER_TOKEN = ":";
	protected static final boolean DEFAULT_CASE_SENSITIVE = false;

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	private String stringPermission;
	private List<String> parts = new ArrayList<String>();

	public PathMatchPermission(String stringPermission) {
		super();
		this.stringPermission = stringPermission;
		setParts(stringPermission, DEFAULT_CASE_SENSITIVE);
	}

	protected void setParts(String stringPermission, boolean caseSensitive) {
		if (stringPermission == null || stringPermission.trim().length() == 0) {
			throw new IllegalArgumentException("Wildcard string cannot be null or empty. Make sure permission strings are properly formatted.");
		}
		stringPermission = stringPermission.trim();
		List<String> parts = CollectionUtils.asList(stringPermission.split(PART_DIVIDER_TOKEN));
		if (!caseSensitive) {
			for (String part : parts) {
				this.parts.add(part.toLowerCase());
			}
		}
		if (this.parts.isEmpty()) {
			throw new IllegalArgumentException("PathMatch string cannot contain only dividers. Make sure permission strings are properly formatted.");
		}
	}

	@Override
	public boolean implies(Permission p) {

		if (!(p instanceof PathMatchPermission)) {
			logger.debug("PathMatchPermission does not support other permission implements...");
			return false;
		}

		PathMatchPermission pmp = (PathMatchPermission) p;
		List<String> otherParts = pmp.getParts();
		if (this.parts.size() != otherParts.size()) {
			logger.debug("Two Permissions structure does not match.");
			return false;
		}

		for (int i = 0; i < this.parts.size(); i++) {
			String thisPart = this.parts.get(i);
			String otherPart = otherParts.get(i);
			if (!antPathMatcher.match(thisPart, otherPart)) {
				logger.trace("Permission does not match. permission: " + getStringPermission() + ", otherPermission: " + pmp.getStringPermission());
				return false;
			}
		}
		return true;
	}

	public String getStringPermission() {
		return stringPermission;
	}

	public List<String> getParts() {
		return parts;
	}

}