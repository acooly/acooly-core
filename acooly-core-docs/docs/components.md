## 组件介绍

框架按照`高内聚、低耦合`的原则，对应用需要的能力进行了封装。组件提供相关内容，组件之间可以依赖。

所有组件的maven坐标为`com.acooly:acooly-component-*`，当依赖了组件时，应用就有具有相关的能力。在`spring`的世界里，能力由`Bean`提供，所以组件就是一系列能力相关`Bean`的集合。

对于业务型组件，我们还提供更多能力：数据库初始化能力、页面、boss管理。

### 1. 相关概念介绍：

* src/main/resources/META-INF/spring.factories

    spring boot 通过查找此配置文件，来加载配置类等其他扩展

* 组件配置类

    `组件配置类`用于把配置转换为POJO,并提供IDE提示功能。比如`com.acooly.module.tomcat.TomcatProperties`,在IDE(IntelliJ IDEA/STS)中输入`acooly.tomcat`,IDE会提示`tomcat`组件有哪些具体的配置项


* 组件初始化器

    `组件初始化器`用于在spring容器初始化之前控制、改变组件的默认行为。

    比如`com.acooly.module.tomcat.TomcatComponentInitializer`配置反向代理相关参数。

    比如`CacheComponentInitializer`配置redis连接池、开发模式启动内置redis。

    `spring.factories`中配置key为`com.acooly.core.common.boot.component.ComponentInitializer`

* 组件日志初始化器

    组件可以定义组件相关的日志，通过继承`AbstractLogInitializer`。比如`com.acooly.module.ds.DruidLogInitializer`定义慢查询日志。

    `spring.factories`中配置key为`com.acooly.core.common.boot.log.initializer.LogInitializer`

* 组件自动配置

    `组件自动配置`用于创建spring beans。比如`com.acooly.module.tomcat.TomcatAutoConfig`扩展spring-boot tomcat。

    `spring.factories`中配置key为`org.springframework.boot.autoconfigure.EnableAutoConfiguration`

* 组件数据库脚本初始化

    当组件使用到数据库时，需要配置数据库脚本初始化器，当开发同学使用到组件时，会自动执行数据库脚本，初始化数据库。

    以sms组件为例，`com.acooly.module.sms.SmsAutoConfig#smsScriptIniter`

    注意：脚本必须放在`src/main/resources/META-INF/database/${componentName}/${dbType}/xxx.sql`



* 组件mvc命名空间

    如果组件包含页面，controller，最好在路径中包含命名空间，防止冲突。


* 组件依赖组件

    组件会依赖其他组件来提供能力。比如`acooly-component-web`依赖`acooly-component-tomcat`提供web服务。依赖`acooly-component-cache`提供分布式session能力。


### 2. 使用组件


 使用组件只需要在maven中配置依赖。比如需要使用dubbo组件，maven中增加如下依赖:

		 <dependency>
	        <groupId>com.acooly</groupId>
	        <artifactId>acooly-component-dubbo</artifactId>
	    </dependency>


当依赖了组件jar包时，也可以通过`enable`参数关闭组件。比如关闭dubbo组件`acooly.dubbo.enable=false`。大多数组件都提供`enable`配置开关。