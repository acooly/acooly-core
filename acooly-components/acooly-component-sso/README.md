## 1. 组件介绍

此组件提供多系统单点登录，一个boss系统管理认证和权限，其他子系统集成boss作为登录和权限管理，集成的时候需要添加此组件

## 2. 使用说明

配置组件参数,如下：

   1) `acooly.sso.ssoServerUrl=http://www.acooly.com/manage/login.html` 登录服务端地址，此系统需要为`.acooly.com`的子域名,如`api.acooly.com`
   2) `acooly.sso.ssoExcludeUrl=/manage/logout.html,/manage/error/**` 不需要登录认证的地址，Ant-style path patterns
   3) `acooly.sso.unauthorizedUrl=http://www.acooly.com/unauthorizedUrl.html` 没有权限时跳转的链接