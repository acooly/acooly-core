#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * ${rootArtifactId}
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-04 22:03
 */
package ${package}.platform.common.portal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 引导页面控制器
 *
 * @author ${author}
 * @date 2020-01-04 22:03
 */
@Slf4j
@Controller
public class GuidePoralController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        return "index";
    }

}
