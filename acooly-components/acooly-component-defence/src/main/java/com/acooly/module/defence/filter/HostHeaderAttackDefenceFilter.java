/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-23 17:22
 */
package com.acooly.module.defence.filter;

import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Strings;
import com.acooly.integration.web.AcoolyFilter;
import com.acooly.module.defence.DefenceProperties;
import com.acooly.module.defence.exception.AccessDeniedHandler;
import com.acooly.module.defence.exception.DefenceErrorCodes;
import com.acooly.module.defence.exception.HhaDefenceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 * Host头攻击防御filter
 * <p>
 * 逻辑：
 * 1、采用配置方式，要求部署的系统配置服务对应的host（域名或IP），可配置多个。
 * 2、如果不匹配配置的host，则返回403:域名非法。
 *
 * @author zhangpu
 * @date 2019-11-23 17:22
 */
@Slf4j
public class HostHeaderAttackDefenceFilter extends AcoolyFilter {

    @Getter
    @Setter
    private AccessDeniedHandler accessDeniedHandler;
    @Getter
    @Setter
    private DefenceProperties defenceProperties;

    @Override
    protected void doFilterDetermine(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (matchHostAttack(httpRequest)) {
            accessDeniedHandler.handle(httpRequest, httpResponse,
                    new HhaDefenceException(DefenceErrorCodes.HHA_FORBIDDEN, "Host头非法访问"));
            return;
        }
        if (matchRefererAttack(httpRequest)) {
            accessDeniedHandler.handle(httpRequest, httpResponse,
                    new HhaDefenceException(DefenceErrorCodes.HHA_FORBIDDEN, "Referer头非法访问"));
            return;
        }
        filterChain.doFilter(request, response);
    }

    protected boolean matchHostAttack(HttpServletRequest httpRequest) {
        String requestHost = httpRequest.getRemoteHost();
        String xForwardedHost = httpRequest.getHeader("X-Forwarded-Host");

        if (!defenceProperties.getHha().getHosts().contains(requestHost)) {
            log.warn("Defence [Host-Header-Attack] hit. Host: {}, URL: {}", requestHost, httpRequest.getRequestURI());
            return true;
        }
        if (Strings.isNotBlank(xForwardedHost) && !defenceProperties.getHha().getHosts().contains(xForwardedHost)) {
            log.warn("Defence [Host-Header-Attack] hit. X-Forwarded-Host: {}, URL: {}", xForwardedHost, httpRequest.getRequestURI());
            return true;
        }
        return false;
    }


    protected boolean matchRefererAttack(HttpServletRequest httpRequest) {
        String referer = httpRequest.getHeader(HttpHeaders.REFERER);
        if (Strings.isBlank(referer) || !Strings.isHttpUrl(referer)) {
            return false;
        }
        String refererHost = getRefererHost(referer);
        if (Strings.isNoneBlank(refererHost) && !defenceProperties.getHha().getHosts().contains(refererHost)) {
            log.warn("Defence [Host-Header-Attack] hit. Referer: {}, URL: {}", referer, httpRequest.getRequestURI());
            return true;
        }
        return false;
    }

    private String getRefererHost(String url) {
        try {
            Asserts.notEmpty(url);
            URI uri = URI.create(url);
            StringBuilder sb = new StringBuilder();
            sb.append(uri.getHost());
            if (uri.getPort() != -1) {
                sb.append(":").append(uri.getPort());
            }
            return sb.toString();
        } catch (Exception e) {
            log.warn("获取Referer的Host部分失败：{}", e.getMessage());
        }
        return null;

    }


}
