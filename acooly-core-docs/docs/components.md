## 组件介绍

框架按照`高内聚、低耦合`的原则，对应用需要的能力进行了封装。组件提供相关内容，组件之间可以依赖。

所有组件的maven坐标为`com.acooly:acooly-component-*`，当依赖了组件时，应用就有具有相关的能力。在`spring`的世界里，能力由`Bean`提供，所以组件就是一系列能力相关`Bean`的集合。

对于业务型组件，我们还提供更多能力：数据库初始化能力、页面、boss管理。

### 1. 相关概念介绍：


* 组件配置类

 `组件配置类`用于把配置转换为POJO,并提供IDE提示功能。比如`com.acooly.module.tomcat.TomcatProperties`,在IDE(IntelliJ IDEA/STS)中输入`acooly.tomcat`,IDE会提示`tomcat`组件有哪些具体的配置项

* 组件初始化器

 `组件初始化器`用于在spring容器初始化之前控制、改变组件的默认行为。比如`com.acooly.module.tomcat.TomcatComponentInitializer`配置反向代理相关参数。

* 组件自动配置

 `组件自动配置`用于创建spring beans。比如`com.acooly.module.tomcat.TomcatAutoConfig`扩展spring-boot tomcat。

* 使用组件

 使用组件只需要在maven中配置依赖。比如需要使用dubbo组件，maven中增加如下依赖:

		 <dependency>
	        <groupId>com.acooly</groupId>
	        <artifactId>acooly-component-dubbo</artifactId>
	    </dependency>
	    
	当依赖了组件jar包时，也可以通过`enable`参数关闭组件。比如关闭dubbo组件`acooly.dubbo.enable=false`。大多数组件都提供`enable`配置开关。

* 组件依赖组件

 组件会依赖其他组件来提供能力。比如`acooly-component-web`依赖`acooly-component-tomcat`提供web服务。依赖`acooly-component-cache`提供分布式session能力。

### 2. 组件介绍

#### 2.1. `acooly-component-tomcat`

此组件嵌入了tomcat服务器。`组件配置类`见`com.acooly.module.tomcat.TomcatProperties`，通过`acooly.tomcat`前缀修改默认配置，默认不需要配置

#### 2.2 `acooly-component-web`

此组件集成了spring-mvc、freemarker、分布式session。`组件配置类`见`com.acooly.module.web.WebProperties`，通过`acooly.web`前缀修改默认配置，默认不需要配置

#### 2.3 `acooly-component-cache`

此组件提供spring声明式cache、	`RedisTemplate`.`组件配置类`见`com.acooly.module.cache.CacheProperties`，通过`acooly.cache`前缀修改默认配置，默认不需要配置.`spring.redis.pool.maxActive`配置最大连接数，默认为100，`spring.redis.pool.maxWait`配置缓存连接时间,默认为5s。

#### 2.4 `acooly-component-jpa`

此组件集成spring-data-jpa，并提供`com.acooly.module.jpa.EntityJpaDao`.`组件配置类`见`com.acooly.module.jpa.JPAProperties`，通过`acooly.jpa`前缀修改默认配置，默认不需要配置.

所有基于jpa实现的dao应该继承`EntityJpaDao`.


#### 2.5 `acooly-component-mybatis`
	
此组件集成mybatis，并提供`com.acooly.module.mybatis.EntityMybatisDao`.`组件配置类`见`com.acooly.module.mybatis.MybatisProperties`，通过`acooly.mybatis`前缀修改默认配置，默认不需要配置.

所有基于mybatis实现的dao应该继承`EntityMybatisDao`.它提供单表的增删改查能力，不用写一行sql代码。

#### 2.6 `acooly-component-security`
	
此组件提供安全相关的能力(shiro\csrf\xss\验证码)，并提供管理后台.`组件配置类`见`com.acooly.module.security.config.SecurityProperties`，通过`acooly.security`前缀修改默认配置，默认不需要配置.

#### 2.6 `acooly-component-dubbo`
	
此组件提供集成dubbo服务的能力.`组件配置类`见`com.acooly.module.dubbo.DubboProperties`，通过`acooly.dubbo`前缀修改默认配置。

必填参数：

* `acooly.dubbo.owner`dubbo应用开发者
* `acooly.dubbo.provider.port`dubbo服务提供者端口(为了保证端口唯一，请在项目创建阶段向运维工程师申请端口)

#### 2.6 `acooly-component-test`

此组件提供常用测试工具。