/**
 * create by zhangpu date:2015年2月27日
 */
package com.acooly.integration.web;

import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Servlets;
import com.acooly.core.utils.Strings;
import org.apache.commons.lang3.StringUtils;
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
 * Acooly Filter扩展
 *
 * <p>扩展支持AntPath方式配置忽略URL
 *
 * @author zhangpu
 * @date 2019-11-24
 */
public class AcoolyFilter extends OncePerRequestFilter {
    private PathMatcher pathMatcher = new AntPathMatcher();
    private List<String> exclusions = new ArrayList<String>();

    @Override
    protected void initFilterBean() throws ServletException {
        initExclusions();
        super.initFilterBean();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUrl = Servlets.getRequestPath((HttpServletRequest) request);
        if (matchExclutions(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 执行确定的filter
        doFilterDetermine(request, response, filterChain);
    }

    protected void doFilterDetermine(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    }

    /**
     * 检查是否匹配排除的URL
     *
     * @param requestUrl
     * @return
     */
    private boolean matchExclutions(String requestUrl) {
        if (exclusions.isEmpty()) {
            return false;
        }
        for (String ignoreUrl : exclusions) {
            if (pathMatcher.match(ignoreUrl, requestUrl)) {
                return true;
            }
        }
        return false;
    }

    private void initExclusions() {
        // 从initParameter获取exclusions参数（逗号分隔）
        String ignores = getFilterConfig().getInitParameter("exclusions");
        if (StringUtils.isNotBlank(ignores)) {
            String[] ignoreArray = Strings.split(ignores, ",");
            for (int i = 0; i < ignoreArray.length; i++) {
                if (StringUtils.isNotBlank(ignoreArray[i])) {
                    exclusions.add(ignoreArray[i]);
                }
            }
        }
        Collections.sort(exclusions);
        Collections.reverse(exclusions);
    }

    public List<String> getExclusions() {
        return exclusions;
    }

    public void addExclusions(List<String> urls) {
        if (Collections3.isEmpty(urls)) {
            return;
        }
        for (String url : urls) {
            addExclusion(url);
        }
    }

    public void addExclusion(String url) {
        if (!exclusions.contains(url)) {
            this.exclusions.add(url);
        }
    }
}
