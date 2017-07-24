package com.acooly.module.defence;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo@yiji.com
 */
@ConfigurationProperties(DefenceProperties.PREFIX)
@Data
public class DefenceProperties {
    public static final String PREFIX = "acooly.security";
    private CSRF csrf = new CSRF();
    private Xss xss = new Xss();

    @PostConstruct
    public void initXss() {
        this.xss.init();
    }
    @Getter
    @Setter
    public static class CSRF {
        private boolean enable = true;
        private Map<String, List<String>> exclusions = Maps.newHashMap();
        private String errorPage = "/error/csrf.htm";

        public CSRF() {
            List<String> list = Lists.newArrayList();
            list.add("/gateway.html");
            list.add("/ofile/upload.html");
            list.add("/mock/gateway/**");
            exclusions.put("security", list);
        }
    }

    @ToString
    public static class Xss {
        private final AntPathMatcher antPathMatcher = new AntPathMatcher();
        @Getter @Setter private boolean enable = true;
        /** 路径支持ant表达式 */
        @Getter @Setter private Map<String, List<String>> exclusions = Maps.newHashMap();

        private List<String> ex = Lists.newArrayList();

        public boolean matches(HttpServletRequest request) {
            if (ex != null) {
                String uri = request.getRequestURI();
                int idx = uri.indexOf(';');
                if (idx > -1) {
                    uri = uri.substring(0, idx);
                }
                for (String ignoreAntPathMatcherPattern : ex) {
                    if (antPathMatcher.match(ignoreAntPathMatcherPattern, uri)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public void init() {
            exclusions.values().forEach(v -> ex.addAll(v));
        }
    }
}
