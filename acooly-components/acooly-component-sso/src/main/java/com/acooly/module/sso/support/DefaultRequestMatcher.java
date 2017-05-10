
package com.acooly.module.sso.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;

import com.google.common.base.Splitter;

/**
 * @author shuijing
 */
public class DefaultRequestMatcher implements RequestMatcher {

    private static final String DEFAULT_IGNORE_URI = "/security/config/index.html,/index.html,/manage/index.html,/manage/login.html,/manage/logout.html,/mgt/**,/**/*.js,/**/*.css,/**/*.ico,/**/*.jpg,/**/*.gif,/**/*.png,/**/*.map,/**/*.jsp,/**/*.woff2,/**/*.ttf";
    private Iterable<String> ignoreUrls;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    public DefaultRequestMatcher(String ignoreUrls) {
        ignoreUrls += "," + DEFAULT_IGNORE_URI;
        this.ignoreUrls = Splitter.on(',').trimResults().omitEmptyStrings().split(ignoreUrls);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        //1. 验证是否为忽略url
        String url = request.getRequestURI();
        for (String ignoreUrl : ignoreUrls) {
            if (antPathMatcher.match(ignoreUrl, url)) {
                return false;
            }
        }
        //2.验证是否登录
        /*if (isLogin(request)) {
            return false;
		}*/
        return true;
    }
}
