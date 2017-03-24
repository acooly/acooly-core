
package com.acooly.module.sso.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;

import com.google.common.base.Splitter;

/**
 * @author shuijing
 */
public class DefaultRequestMatcher implements RequestMatcher {

    private static final String DEFAULT_IGNORE_URI = "/mgt/**,**.js,**.css,**.ico,**.jpg,**.gif,**.png";
    private Iterable<String> ignoreUrls;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private boolean open = true;

    //openFilter=false 表示关闭验证
    public DefaultRequestMatcher(String ignoreUrls, String openFilter) {
        //enableEnvs += "," + DEFAULT_ENABLE_ENV;
        ignoreUrls += "," + DEFAULT_IGNORE_URI;
        this.ignoreUrls = Splitter.on(',').trimResults().omitEmptyStrings().split(ignoreUrls);
        if (("false".equals(openFilter))) {
            open = false;
        }
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        //1. 验证开关
        if (!open) {
            return false;
        }
        //2. 验证是否为忽略url
        String url = request.getRequestURI();
        for (String ignoreUrl : ignoreUrls) {
            if (antPathMatcher.match(ignoreUrl, url)) {
                return false;
            }
        }
        //3.验证是否登录
        /*if (isLogin(request)) {
            return false;
		}*/
        return true;
    }
}
