package com.acooly.module.defence;

import com.acooly.core.utils.Strings;
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
 * @author zhangpu
 */
@ConfigurationProperties(DefenceProperties.PREFIX)
@Data
public class DefenceProperties {
    public static final String PREFIX = "acooly.security";
    private CSRF csrf = new CSRF();
    private Xss xss = new Xss();
    private Url url = new Url();
    private Hha hha = new Hha();

    @PostConstruct
    public void initXss() {
        this.xss.init();
    }

    @Getter
    @Setter
    public static class CSRF {
        private boolean enable = true;
        private Map<String, List<String>> exclusions = Maps.newHashMap();
        private String errorPage;

        public CSRF() {
            // 已知的组建安全忽略
            List<String> list = Lists.newArrayList();
            list.add("/gateway*");
            list.add("/ofile/upload.html");
            list.add("/baidu/**");
            list.add("/ofile/obs/**");
            list.add("/mock/gateway/**");
            exclusions.put("security", list);
        }
    }

    @Data
    public static class Url {
        private final String key = "hbxENKbfoQ3g";
        private boolean enable = true;

        public String paddingKey() {
            return Strings.leftPad(key, 16, '0');
        }
    }

    @ToString
    public static class Xss {
        private final AntPathMatcher antPathMatcher = new AntPathMatcher();
        @Getter
        @Setter
        private boolean enable = true;
        /**
         * 路径支持ant表达式
         */
        @Getter
        @Setter
        private Map<String, List<String>> exclusions = Maps.newHashMap();

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

    /**
     * Http Header Attack defence
     * 开启后：
     * 1、请求host必须与匹配配置的hosts列表
     * 2、请求的refer必须匹配的配置hosts列表
     */
    @Data
    public static class Hha {
        private boolean enable = false;
        private Map<String, List<String>> exclusions = Maps.newHashMap();
        private String errorPage;
        /**
         * 允许的安全host，多个使用英文逗号分隔
         */
        private String hosts = "127.0.0.1";
    }
}
