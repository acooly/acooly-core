package com.acooly.module.security.defence.csrf;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Cross-site request forgery 跨站请求伪造 攻击防御
 * <p/>
 * 基于spring-web-security提供的实现扩展，主要原因是spring提供的实现不能完全满足需求，但其申明为final,无法扩展。
 * <p/>
 * <p>
 * <li>1、增加忽略URL，应对已有诸如签名认证的API接口访问，无需防御
 * <li>2、增加对表单文件上传的支持（spring的实现没考虑）
 * </p>
 *
 * @author zhangpu
 */
public class CsrfDefenseFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(CsrfDefenseFilter.class);
	private CsrfTokenRepository tokenRepository = new HttpSessionCsrfTokenRepository();
	private RequestMatcher requireCsrfProtectionMatcher = new CsrfRequestMatcher();
	private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

	public CsrfDefenseFilter() {
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		CsrfToken csrfToken = tokenRepository.loadToken(request);
		final boolean missingToken = csrfToken == null;
		if (missingToken) {
			csrfToken = generateAndSaveToken(request, response);
		}
		request.setAttribute(CsrfToken.class.getName(), csrfToken);
		request.setAttribute(csrfToken.getParameterName(), csrfToken);

		if (!requireCsrfProtectionMatcher.matches(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String requestToken = getRequestToken(request, csrfToken);
		if (!csrfToken.getToken().equals(requestToken)) {
			if (logger.isDebugEnabled()) {
				logger.debug("CSRF-Token无效: " + UrlUtils.buildFullRequestUrl(request));
			}
			if (missingToken) {
				accessDeniedHandler.handle(request, response, new MissingCsrfTokenException(requestToken));
			} else {
				accessDeniedHandler.handle(request, response, new InvalidCsrfTokenException(csrfToken, requestToken));
			}
			return;
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * 获取请求的token
	 *
	 * @param request
	 * @param csrfToken
	 * @return
	 */
	protected String getRequestToken(HttpServletRequest request, CsrfToken csrfToken) {
		String actualToken = request.getHeader(csrfToken.getHeaderName());
		if (actualToken == null) {
			actualToken = request.getParameter(csrfToken.getParameterName());
		}
		// if (StringUtils.isBlank(actualToken)) {
		// if (StringUtils.contains(request.getContentType(), "multipart")) {
		// DefaultMultipartHttpServletRequest multiRequest = new
		// DefaultMultipartHttpServletRequest(request);
		// actualToken =
		// multiRequest.getParameter(csrfToken.getParameterName());
		// }
		// }
		return actualToken;
	}

	/**
	 * 生成和保存csrfToken
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	protected synchronized CsrfToken generateAndSaveToken(HttpServletRequest request, HttpServletResponse response) {
		CsrfToken generatedToken = tokenRepository.generateToken(request);
		tokenRepository.saveToken(generatedToken, request, response);
		return generatedToken;
	}

	public void setTokenRepository(CsrfTokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	public void setRequireCsrfProtectionMatcher(RequestMatcher requireCsrfProtectionMatcher) {
		this.requireCsrfProtectionMatcher = requireCsrfProtectionMatcher;
	}

	public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
		this.accessDeniedHandler = accessDeniedHandler;
	}

}
