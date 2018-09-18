<!-- title: 防御组件 -->
<!-- type: infrastructure -->
<!-- author: qiubo -->

### 1. 怎么配置csrf排除路径

    acooly.security.csrf.exclusions.ofile[0]=/ofile/gateway/**
    acooly.security.csrf.exclusions.ofile[1]=/ofile/cs/**
    
### 2. url加解密

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

也可以使用`com.acooly.module.defence.url.UrlSecurityService`加解密参数