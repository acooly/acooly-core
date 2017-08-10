## 1. 组件介绍

提供各种验证码的生成和验证，管理验证码的生命周期,可用于生成验证token

## 2. 使用说明

### 2.1 配置

此组件依赖cache,相关redis配置

可选配置：

`acooly.captcha.captchaLength=16` 设置验证长度，默认长度4

`acooly.captcha.expireSeconds=320` 设置验证码过期时间，单位秒S，默认300S（5分钟）

`acooly.captcha.generatorType=default_unique_generator` 设置验证码生产方式,默认random_number（随机数字），此组件用于token方式建议：default_unique_generator

`acooly.captcha.handlerType=defualt_answer_handler`  设置验证码验证方式，默认字符串不区分大小写

### 2.2 接口使用

```
 @Autowired private CaptchaService captchaService;

  public void getCaptcha() {
    Captcha captcha = captchaService.getCaptcha();
    captchaService.validateCaptcha(captcha.getId(), (String) captcha.getValue());
  }

```
详见 `com.acooly.module.captcha.CaptchaService`

### 2.3 测试用例

`com.acooly.core.test.web.CaptchaTestController`