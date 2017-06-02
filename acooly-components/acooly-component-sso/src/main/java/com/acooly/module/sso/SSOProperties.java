package com.acooly.module.sso;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author shuijing */
@Data
@ConfigurationProperties(SSOProperties.PREFIX)
public class SSOProperties {

  public static final String PREFIX = "acooly.sso";

  private boolean enable = true;

  /** 主boss登录地址 */
  @NotEmpty private String ssoServerUrl;

  /**
   * 不需要拦截处理的静态资源(ant匹配规则 @link
   * https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/test/java/org/springframework/util/AntPathMatcherTests.java)
   */
  private String ssoExcludeUrl;
}
