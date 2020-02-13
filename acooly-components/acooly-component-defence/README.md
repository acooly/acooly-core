<!-- title: 安全防御组件 -->
<!-- type: infrastructure -->
<!-- author: zhangpu,qiubo -->
<!-- date: 2019-12-14 -->
## 1. 组件介绍

提供应用常用的安全防御组件。

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-defence</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

### 2.1. CSRF防御
	
默认是开启的。通过建立session级的csrfToken，在POST请求中，确保csrfToken存在并未过期。
	
```ini
## 开关参数。默认：true
acooly.security.csrf.enable=true
## 命中后跳转的错误页面，默认未空，则直接返回403错误码
acooly.security.csrf.errorPage=/error/csrf.html
## 配置忽略的antPath模式URL-Patten
acooly.security.csrf.exclusions.gateway[0]=/gateway*
acooly.security.csrf.exclusions.simplel[0]=/test/httpmethod/*
acooly.security.csrf.exclusions.simplel[1]=/xxxx/**/*.html
```	
    
### 2.2. XSS防御 
默认是开启的。自动防御SQL注入风险。实现方式为：实现专用的：`XssHttpServletRequestWrapper`替换调原始的`HttpServletRequestWrapper`,重写在`getParameter`等获取参数的方法，对参数值进行SQL注入风险替换和过滤，整个实现方法不侵入应用和服务。filter顺序在编码转换filter之后

配置参数说明:

```ini
## 开关参数。默认：true
acooly.security.xss.enable=true
## 配置忽略的antPath模式URL-Patten
acooly.security.xss.exclusions.gateway[0]==/gateway*
```

### 2.3. Http-Header-Attack防御

默认为关闭，整个风险防御一般在基线安全测试中出现，并被标记为高。主要包括：

* Host攻击
* Referer的Url中Host变更攻击

实现方案：如果开启后，需要配置允许的安全Host，否则会403拒绝访问。配置参数如下：

```ini
## 开启Http-Header-Attack防御
acooly.security.hha.enable=true
## 命中后跳转的错误页面，如果没有配置，则直接返回403错误码
acooly.security.hha.errorPage=/error/hha.html
## 配置忽略的antPath模式URL-Patten
acooly.security.hha.exclusions.gateway[0]=/gateway*
## 允许的Host和Referer域,注意：这里的host包含端口。
acooly.security.hha.hosts=acooly.cn,www.baidu.com,cashier.techfin.com:8081
```

    
### 2.4. url加解密

本功能主要用于前端URL加密，防止攻击者通过URL及参数规律爬取数据。采用对称加密方案，秘钥的配置目前采用内置，无法外部配置。


案例如下：

```java
       /**
        * 请求：http://127.0.0.1:8081/url/param?id=58bb0e40e63678e780590e5986cbd804be7b5a11379de9f4f6a6238287bd5779&name=a
        */
       @RequestMapping(value = "/param", method = RequestMethod.GET)
       public String param(@SecurityParam String id, String name) {
         return id + name;
       }
       /**
        * 支持类型转换
        *
        * 请求：http://127.0.0.1:8081/url/param1?id=58bb0e40e63678e780590e5986cbd804be7b5a11379de9f4f6a6238287bd5779
        */
       @RequestMapping(value = "/param1", method = RequestMethod.GET)
       public Integer param1(@SecurityParam Integer id) {
         return id;
       }
       /**
        * 支持POJO
        *
        * 请求：http://127.0.0.1:8081/url/param2?id=58bb0e40e63678e780590e5986cbd804be7b5a11379de9f4f6a6238287bd5779&name=bohr&age=12
        */
       @RequestMapping(value = "/param2", method = RequestMethod.GET)
       public MyDto param2(@SecurityParam({"id"}) MyDto app) {
         return app;
       }

       /**
        * 支持响应加密
        *
        * 请求：http://127.0.0.1:8081/url/param3?id=58bb0e40e63678e780590e5986cbd804be7b5a11379de9f4f6a6238287bd5779&name=bohr&age=12
        * 响应：{"id":"58bb0e40e63678e780590e5986cbd804be7b5a11379de9f4f6a6238287bd5779","name":"bohr","age":1}
        */
       @RequestMapping(value = "/param3", method = RequestMethod.GET)
       public @SecurityParam({"id"}) MyDto param4(@SecurityParam({"id"}) MyDto app) {
         log.info("{}", app);
         app.setAge(1);
         return app;
       }

      @Data
      public class MyDto {
        private Long id;
        private String name;
        private int age;
      }

```      

也可以使用`com.acooly.module.defence.url.UrlSecurityService`加解密参数