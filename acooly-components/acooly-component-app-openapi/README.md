## 1. 组件介绍

提供app访问的openapi接口


## 2. 配置

1. 需要配置匿名账户相关信息，用于app匿名访问登录或其他服务
2. 应用需要实现`com.acooly.module.appopenapi.support.AppApiLoginService`,提供app登录认证需求

## 3. 使用

客户端需要设置请求参数中`appClient`为true
