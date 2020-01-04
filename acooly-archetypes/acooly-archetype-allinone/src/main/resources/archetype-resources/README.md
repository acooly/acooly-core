acooly-allinone 项目说明
====

## 简介

通过acooly-archetype-allinone骨架生成的工程，一般用于新的孵化项目。孵化前期需进行完成的系统分析设计（特别是内部逻辑结构设计和模块划分），以系分设计为基础，在构建孵化项目，以此定义platform模块组。platform模块组下的所有子模块都应根据系分的内部模块划分为基础，让孵化项目具备可微服务化的升缩性。

## 工程规划说明

### 结构

```java
+ assemble                  整合打包和配置
+ common                    公共结构和工具
+ facade                    微服务接口定义
- openapi                   对外Api接口
    + openapi-message       Api报文定义
    + openapi-service       Api服务开发
    + openapi-test          Api服务测试
- platform                  业务平台
    + platform-common       公共配置和业务
    + platform-facade-impl  微服务接口实现
    + platform-core         核心业务（一般其他业务都依赖它，比如会员）
    + platform-module1      业务模块1
    + platform-module2      业务模块2
    + ...
    + platform-moduleN      业务模块N
+ test                      单元测试
```

>各模块详细说明，请参考模块内的README.md