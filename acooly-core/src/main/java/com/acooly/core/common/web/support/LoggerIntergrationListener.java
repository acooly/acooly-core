package com.acooly.core.common.web.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.util.Log4jConfigListener;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.FileNotFoundException;

/**
 * 扩展Spring的Log4jConfigListener，在容器启动的时候，桥接JDK14的输出到slf4j-logger
 *
 * @author zhangpu
 *     <p>已迁移到:com.acooly.core.common.web.integrator
 */
@Deprecated
public class LoggerIntergrationListener extends Log4jConfigListener {

  /** Parameter specifying the location of the log4j config file */
  public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";

  /** Parameter specifying the refresh interval for checking the log4j config file */
  public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";

  /** Parameter specifying whether to expose the web app root system property */
  public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";

  private static boolean exposeWebAppRoot(ServletContext servletContext) {
    String exposeWebAppRootParam = servletContext.getInitParameter(EXPOSE_WEB_APP_ROOT_PARAM);
    return (exposeWebAppRootParam == null || Boolean.valueOf(exposeWebAppRootParam));
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    installJulToSlf4jBridge();
    event.getServletContext().log("Install Jdk-util-logger to slf4j success.");
    initLogging(event.getServletContext());
  }

  private void installJulToSlf4jBridge() {
    //		SLF4JBridgeHandler.removeHandlersForRootLogger();
    //		SLF4JBridgeHandler.install();
  }

  private void initLogging(ServletContext servletContext) {
    // Expose the web app root system property.
    if (exposeWebAppRoot(servletContext)) {
      WebUtils.setWebAppRootSystemProperty(servletContext);
    }

    // Only perform custom log4j initialization in case of a config file.
    String locationConfig = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);

    String springActiveProfile =
        System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
    if (StringUtils.isBlank(springActiveProfile)) {
      springActiveProfile =
          servletContext.getInitParameter(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
    }
    String location = locationConfig;
    if (StringUtils.isNotBlank(springActiveProfile)) {
      location =
          StringUtils.replace(
              locationConfig, "log4j.properties", "log4j." + springActiveProfile + ".properties");
    }

    if (location != null) {
      // Perform actual log4j initialization; else rely on log4j's default
      // initialization.
      try {
        // Return a URL (e.g. "classpath:" or "file:") as-is;
        // consider a plain file path as relative to the web application
        // root directory.
        if (!ResourceUtils.isUrl(location)) {
          // Resolve system property placeholders before resolving
          // real path.
          location = SystemPropertyUtils.resolvePlaceholders(location);
          location = WebUtils.getRealPath(servletContext, location);
        }

        // Write log message to server log.
        servletContext.log("Initializing log4j from [" + location + "]");

        // Check whether refresh interval was specified.
        String intervalString = servletContext.getInitParameter(REFRESH_INTERVAL_PARAM);
        if (intervalString != null) {
          // Initialize with refresh interval, i.e. with log4j's
          // watchdog thread,
          // checking the file in the background.
          try {
            long refreshInterval = Long.parseLong(intervalString);
            Log4jConfigurer.initLogging(location, refreshInterval);
          } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                "Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
          }
        } else {
          // Initialize without refresh check, i.e. without log4j's
          // watchdog thread.
          Log4jConfigurer.initLogging(location);
        }
      } catch (FileNotFoundException ex) {
        throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex);
      }
    }
  }
}
