## 1. 组件介绍
   定制tomcat，配置线程、日志、自定义错误页等
   
## 2. 使用说明

配置组件参数,如下：

   * `acooly.tomcat.port=1234` 可选，通过配置文件自定义端口号
   * `acooly.tomcat.error401Page=/401.html` 可选，未授权页面401，页面路径`src/main/resources/static`
   * `acooly.tomcat.error404Page=/404.html` 可选，未找到页面404
   * `acooly.tomcat.error500Page=/500.html` 可选，错误页面500
    