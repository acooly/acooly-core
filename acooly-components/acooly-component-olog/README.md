## 1. 组件介绍

此组件提供后台管理系统审计日志收集，展示功能.

此组件结构如下：

1. collector

    负责日志收集，通过`com.acooly.module.olog.collector.OlogForwarder`发送日志到存储端

2. storage

    负责日志存储、展示、归档等能力。

## 2. 使用

### 2.1 sso应用部署

当启用sso boss应用场景时，需要把各个子系统的审计日志发送到boss系统。

需要在各子系统中禁用storage.

    acooly.olog.storage.enable=false

如果启用了`acooly.security.enableSSOAuthzService=true`，可以不配置此值。

### 2.2 日志元数据配置

1. 通过在Controller类上标注`@OlogModule`配置模块名称,也可通过`acooly.olog.collector.moduleNameMapping`
2. 通过在Controller类或者方法上标注`@Olog.Ignore`忽略日志收集
3. 通过在Controller方法上标注`@OlogAction`配置操作名称，也可以通过配置`acooly.olog.collector.actionNameMapping`

### 2.3 审计日志生效条件

1. 请求路径`/manage/**.html`

