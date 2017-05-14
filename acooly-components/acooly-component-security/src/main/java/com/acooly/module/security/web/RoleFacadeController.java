package com.acooly.module.security.web;

import com.acooly.module.security.config.SecurityProperties;
import com.acooly.module.security.domain.User;
import com.acooly.module.security.service.UserService;
import com.acooly.module.security.shiro.cache.ShiroCacheManager;
import com.acooly.module.security.shiro.realm.PathMatchPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * role facade
 *
 * @author shuijing
 */
@Slf4j
@Controller
@RequestMapping(value = "/role/facade")
@ConditionalOnProperty(value  = SecurityProperties.PREFIX + ".shiro.auth.enable",matchIfMissing = true)
public class RoleFacadeController {

    @Autowired
    private UserService userService;
    @Autowired
    private WebSecurityManager shiroSecurityManager;

    @RequestMapping("isPermitted")
    @ResponseBody
    public Boolean isPermitted(String uri, String username) {
        try {
            return permitted(uri, username);
        } catch (Exception e) {
            log.error("检查权限异常", e);
            return false;
        }
    }

    private boolean permitted(String uri, String username) throws Exception {
        boolean result = false;
        if (!StringUtils.isEmpty(username)) {
            User user = userService.getAndCheckUser(username);
            if (user != null) {
                ThreadContext.bind(shiroSecurityManager);
                SimplePrincipalCollection simplePrincipal = new SimplePrincipalCollection(user, ShiroCacheManager.KEY_AUTHC);
                Subject subject = new Subject.Builder().principals(simplePrincipal).authenticated(true).buildSubject();
                ThreadContext.bind(subject);
                String permission = "do"+ PathMatchPermission.PART_DIVIDER_TOKEN + uri;
                result = subject.isPermitted(permission);
            }
        }
        return result;
    }
}
