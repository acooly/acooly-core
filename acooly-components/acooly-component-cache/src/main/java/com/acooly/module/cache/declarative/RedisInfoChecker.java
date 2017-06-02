package com.acooly.module.cache.declarative;

import com.acooly.core.common.boot.EnvironmentHolder;
import com.acooly.core.common.exception.AppConfigException;
import com.google.common.base.Throwables;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.ConnectException;
import java.util.Properties;

/** @author shuijing */
public class RedisInfoChecker {

  private RedisTemplate redisTemplate;

  public RedisInfoChecker(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void checkRedisVersion() {
    RedisProperties redisProperties = new RedisProperties();
    EnvironmentHolder.buildProperties(redisProperties);
    String host = redisProperties.getHost();
    //内置redis忽略检查
    if (host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1")) {
      if ("true".equals(System.getProperty("isInternalRedis"))) {
        return;
      }
    }
    Properties info = null;
    try {
      info = redisTemplate.getConnectionFactory().getConnection().info();
    } catch (Exception e) {
      if (Throwables.getRootCause(e) instanceof ConnectException) {
        return;
      }
    }
    if (info == null) {
      return;
    }
    String redis_version = (String) info.get("redis_version");
    if (!checkVersion(redis_version)) {
      throw new AppConfigException("为支持集群模式，redis版本必须为3.x.x !");
    }
  }

  private boolean checkVersion(String version) {
    //2.8.17
    String[] versionSplit = version.split("\\.");
    Integer bigVersion = Integer.valueOf(versionSplit[0]);
    if (bigVersion < 3) {
      return false;
    }
    return true;
  }
}
