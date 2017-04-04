## 1. 组件介绍

此组件提供多系统单点登录，此组件以Acooly作为登录入口

## 2. 使用说明

配置组件参数,如下：
   1) acooly.sso.ssoServerUrl=http://www.acooly.com/manage/login.html 登录服务端地址，只改变'www.acooly.com'即可
   2) acooly.sso.ssoExcludeUrl=/manage/logout.html,/manage/error/** 不需要登录认证的地址，Ant-style path patterns
 