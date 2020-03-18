<!-- title: 多租户组件 -->
<!-- type: infrastructure -->
<!-- author: aleishus -->
<!-- date: 2020-3-14 -->

## 1. 组件介绍

此组件实现整个框架的多租户服务的能力，采用无入侵式，业务无需修改任何代码即可实现多租户能力，不引用此依赖对现有框架完全没有任何影响，本组件对日志、dubbo、event、ofile、scheduler、
security、datasource、cache、threadpool 进行了多租户能力增强。

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-tenantsupport</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。



    
### 2.1 使用配置
使用需要注意一下几点：

* 多租户数据源配置

启用了多租户组件必须配置多租户数据源
```
#datasource 所有数据源的公共配置
acooly.ds.enable=true
acooly.ds.username=root
acooly.ds.password=123456
acooly.ds.slowSqlThreshold=0

acooly.ds.tenant.tenant1.url=jdbc:mysql://127.0.0.1:3306/t1
acooly.ds.tenant.tenant1.maxActive=10
acooly.ds.tenant.tenant1.alias=阿里    # 别名为必须配置项目，没有配置会造成启动失败
acooly.ds.tenant.tenant2.url=jdbc:mysql://127.0.0.1:3306/t2
acooly.ds.tenant.tenant2.min-idle=5
acooly.ds.tenant.tenant1.alias=腾讯

# 其中tenant1和tenant2 为约定的租户号（从openapi 透传过来的租户Id），每个租户的数据源亦可根据自身需要覆盖数据源的公共配置
```

* 业务入口设置租户Id
业务入口定义为所有请求最初发起方，如openapi，boss 后台登录

1、对于openapi可是用dubbo 设置orderBase 调用进行透传

```java
com.acooly.core.common.facade.OrderBase.tenantId
```
2、对boss 后台登录 需要在登录页面的Form表单中增加一个`tenantId`的字段

3、对其他可能涉及入口可通过显示调用`com.acooly.module.tenant.core.TenantContext.set`方法设置


* 对于oflie 需要事先建立好好租户的文件路径如 `/mnt/media/tenant1`,`/mnt/media/tenant2`,




## 3. FAQ
### 可参考如下demo

http://gitlab.cnvex.cn/zhouxi/tenantDemo.git



