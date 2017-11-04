/** create by zhangpu date:2016年1月10日 */
package com.acooly.module.appopenapi.support.login;

import com.acooly.module.appopenapi.message.LoginRequest;
import com.acooly.module.appopenapi.support.AppApiLoginService;
import com.acooly.module.appopenapi.support.LoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zhangpu
 * @date 2016年1月10日
 */
public class AnonymousAppApiLoginService implements AppApiLoginService {

  private static final Logger logger = LoggerFactory.getLogger(AnonymousAppApiLoginService.class);

  @Override
  public LoginDto login(String userName, String password, Map<String, Object> context) {
    logger.info("登录认证回调扩展信息:{}", context);
    LoginDto loginDto = new LoginDto();
    loginDto.setAccessKey(userName);
    loginDto.setCustomerId("0");
    LoginRequest request = (LoginRequest) context.get("request");
    loginDto.setExtJson(request.getExtJson());
    return loginDto;
  }
}
