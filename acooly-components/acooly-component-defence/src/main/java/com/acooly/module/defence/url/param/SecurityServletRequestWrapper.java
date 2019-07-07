package com.acooly.module.defence.url.param;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
public class SecurityServletRequestWrapper extends ServletRequestWrapper {
    private Map<String, String> maps;

    public SecurityServletRequestWrapper(ServletRequest request) {
        super(request);
    }

    public SecurityServletRequestWrapper(ServletRequest servletRequest, Map<String, String> maps) {
        super(servletRequest);
        this.maps = maps;
    }

    @Override
    public String[] getParameterValues(String name) {
        String value = maps.get(name);
        if (value != null) {
            return new String[]{value};
        }
        return super.getParameterValues(name);
    }
}
