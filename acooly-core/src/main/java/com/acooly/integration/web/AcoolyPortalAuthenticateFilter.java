package com.acooly.integration.web;

import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * portal安全认证filter
 */
@Slf4j
public abstract class AcoolyPortalAuthenticateFilter extends OncePerRequestFilter {

    protected PathMatcher pathMatcher = new AntPathMatcher();
    protected List<String> excludes = new ArrayList<String>();
    protected List<String> includes = new ArrayList<String>();
    /**
     * 需要限制安全访问的目录
     */
    protected String authIncludes = "/**/*";

    /**
     * 在限制安全访问的目录里的排除目录
     */
    protected String authExcludes;

    protected String authFailureUrl;

    protected String authSessionKey;

    protected boolean enable = true;


    @Override
    protected void initFilterBean() throws ServletException {
        initPaths(true, this.authIncludes, this.includes);
        initPaths(false, this.authExcludes, this.excludes);
        super.initFilterBean();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // url忽略或已认证
        if (!this.enable || isIgnore(request) || !requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Object object = doAuthentication(request);
            setSessionKey(request, object);
        } catch (Exception e) {
            log.debug("访问认证 [失败] url:{}, {}", Servlets.getRequestPath(request), e.getMessage());
            handleException(request, response, e);
            return;
        }
        chain.doFilter(request, response);
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return getSessionKey(request) == null;
    }

    protected Object getSessionKey(HttpServletRequest request) {
        return request.getSession().getAttribute(this.authSessionKey);
    }

    protected void setSessionKey(HttpServletRequest request, Object object) {
        request.getSession().setAttribute(this.authSessionKey, object);
    }

    /**
     * 异常处理
     *
     * @param request
     * @param e
     */
    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        if (Strings.isNotBlank(this.authFailureUrl)) {
            response.sendRedirect(this.authFailureUrl);
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }

    }


    /**
     * 认证处理
     * 认证失败抛出异常则中断访问
     *
     * @param request
     * @return 需要放入session中的数据，也同时用于检测是否需要登录的标志
     */
    protected abstract Object doAuthentication(HttpServletRequest request);


    protected boolean needAuthRequest(HttpServletRequest request) {
        boolean needAuth = true;
        String requestUrl = Servlets.getRequestPath(request);
        if (!this.includes.isEmpty()) {
            for (String includeUrl : includes) {
                needAuth = pathMatcher.match(includeUrl, requestUrl);
                if (needAuth) {
                    break;
                }
            }
        }
        return needAuth;
    }


    protected boolean isIgnore(HttpServletRequest request) {

        // 不在认证URL范围，则直接忽略
        if (!needAuthRequest(request)) {
            return true;
        }

        boolean isIgnore = false;
        String requestUrl = Servlets.getRequestPath(request);
        if (!excludes.isEmpty()) {
            for (String ignoreUrl : excludes) {
                isIgnore = pathMatcher.match(ignoreUrl, requestUrl);
                if (isIgnore) {
                    break;
                }
            }
        }
        return isIgnore;
    }


    protected void initPaths(boolean includePath, String path, List<String> listPath) {
        listPath.clear();
        if (StringUtils.isNotBlank(path)) {
            String[] ignoreArray = path.split(",");
            for (int i = 0; i < ignoreArray.length; i++) {
                if (StringUtils.isNotBlank(ignoreArray[i])) {
                    listPath.add(ignoreArray[i]);
                }
            }
            Collections.sort(listPath);
            Collections.reverse(listPath);
        }
        log.info("初始化目录 [{}] : {}", includePath ? "认证" : "排除", listPath);
    }

    public void setAuthFailureUrl(String authFailureUrl) {
        this.authFailureUrl = authFailureUrl;
    }

    public void setAuthSessionKey(String authSessionKey) {
        this.authSessionKey = authSessionKey;
    }

    public void setAuthIncludes(String authIncludes) {
        this.authIncludes = authIncludes;
    }

    public void setAuthExcludes(String authExcludes) {
        this.authExcludes = authExcludes;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
