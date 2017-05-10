## 1. 组件介绍

此组件提供多系统单点登录，一个boss系统管理认证和权限，其他子系统集成boss作为登录和权限管理，集成的时候需要添加此组件

## 2. 使用说明

配置组件参数,如下：

   1) `acooly.sso.ssoServerUrl=http://boss.acooly.com:8070/manage/login.html` 登录服务端地址，url全路径
   2) `acooly.sso.ssoExcludeUrl=/manage/logout.html,/manage/error/**` 不需要登录认证的地址，ant路径匹配规则， 用,分割[Ant-style path patterns](https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/test/java/org/springframework/util/AntPathMatcherTests.java)

## 4. 集成单点登录

1. 业务页面修改
   1. freemarker 的需要在第一行引入`<#include "*/include.ftl">`
   2. jsp 的需要引入 `<%@ include file="/WEB-INF/jsp/manage/common/ssoInclude.jsp"%>` 
2. 资源菜单 加载方式由之前的 ajax 改为 IFrame
3. 资源菜单 资源串 需要改为全路径，如：`http://lottery.acooly.com:8080/manage/module/lottery/lottery/index.html`
4. 关掉应用系统认证授权，设置`acooly.security.shiro.auth.enable=false`
   
## 4. 注意事项
   
1. 域名支持二级域名，如登录服务器地址`acooly.sso.ssoServerUrl=http://boss.acooly.com/manage/login.html` 那么只有 .acooly.com 子域名才支持 sso 登录
2. 只有子系统才需要添加此组件，主boss不用添加