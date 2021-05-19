acooly-core
==================
-------------------------

## 项目简介

acooly框架是基于spring-boot提供快速开发、最佳实践，组件化的框架。

您可以从[框架介绍](acooly-core-docs/README.md)开始。

测试gitlab hooks

## changelog

### 5.0.1-SNAPSHOT

* [兼容] 对于基础类型的定义，独立为最小化依赖的模块，最为ACOOLY所有组件和项目的最底层依赖（兼容），可用于OpenApi报文及外部SDK使用。
* [兼容] 从parent中去除默认的所有包依赖，包括：日志，apache和google的工具包，spring等，迁移到：acooly-common中
* [兼容] 合并acooly-common和acooly-common-facade
* [兼容] 新增acooly-common-type: 基础类型（枚举，标记，扩展类型，dto等）-> 可用于外部（比如SDK和Client工具），依赖关系： `acooly-common -> acooly-common-type`
* [兼容] acooly-common-type只依赖：validation-api 和 lombok  
    
    