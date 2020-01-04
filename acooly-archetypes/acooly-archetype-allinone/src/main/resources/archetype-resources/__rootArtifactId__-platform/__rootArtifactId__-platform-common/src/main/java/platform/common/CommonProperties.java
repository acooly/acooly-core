#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $systemName = ${rootArtifactId} )
#if(${rootArtifactId.indexOf("-")}!=-1)
    #set( $cc = ${rootArtifactId.split("-")})
    #if($cc != $null && ${cc.size()} > 0)
        #set( $lastIndex = $cc.size() - 1 )
        #set( $systemName = $cc[$lastIndex] )
    #end
#end
/**
 * ${rootArtifactId}
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-01-02 11:44
 */
package ${package}.platform.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 项目配置
 *
 * @author zhangpu
 * @date 2020-01-02 11:44
 */
@ConfigurationProperties("${systemName}")
@Data
public class CommonProperties {

    /**
     * 配置前置
     */
    public static final String PREFIX = "${systemName}";

    /**
     * 配置开关
     */
    private boolean enable = false;
}
