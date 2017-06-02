package com.acooly.module.point;

import com.acooly.core.common.dao.support.StandardDatabaseScriptIniter;
import com.acooly.module.security.config.SecurityAutoConfig;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.acooly.module.point.PointProperties.PREFIX;

@Configuration
@ConditionalOnProperty(value = PREFIX + ".enable", matchIfMissing = true)
@ComponentScan
@AutoConfigureAfter(SecurityAutoConfig.class)
public class PointAutoConfig {
  @Bean
  public StandardDatabaseScriptIniter pointScriptIniter() {
    return new StandardDatabaseScriptIniter() {
      @Override
      public String getEvaluateTable() {
        return "point_trade";
      }

      @Override
      public String getComponentName() {
        return "point";
      }

      @Override
      public List<String> getInitSqlFile() {
        return Lists.newArrayList("point", "point_urls");
      }
    };
  }
}
