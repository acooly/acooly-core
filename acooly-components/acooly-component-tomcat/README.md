<!-- title: tomcat组件 -->
<!-- type: infrastructure -->
<!-- author: qiubo -->
<!-- date: 2019-12-25 -->
## 1. 组件介绍

   定制tomcat，配置线程、日志、自定义错误页等
   
## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-tomcat</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

### 2.1 参数配置

配置组件参数,如下：

   * `acooly.tomcat.port=1234` 可选，通过配置文件自定义端口号
   * `acooly.tomcat.error40XPage=/40X.html` 可选，定制40X状态返回的页面，页面路径`src/main/resources/static`
   * `acooly.tomcat.error50XPage=/50X.html` 可选，定制50X状态返回的页面
    