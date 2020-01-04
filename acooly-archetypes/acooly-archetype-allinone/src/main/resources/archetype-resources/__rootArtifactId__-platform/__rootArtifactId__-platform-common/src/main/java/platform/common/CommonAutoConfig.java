#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * ${rootArtifactId}
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author ${author}
 * @date 2020-01-02 11:51
 */
package ${package}.platform.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 项目自动配置
 *
 * @author ${author}
 * @date 2020-01-02 11:51
 */
@Slf4j
@ConditionalOnProperty(value = CommonProperties.PREFIX + ".enable", matchIfMissing = true)
public class CommonAutoConfig {

}
