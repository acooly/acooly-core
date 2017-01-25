Acooly framework
====

## 简介

acooly框架主要设计目的为快速，规范的构建业务层系统和平台，并设计为很好的支持高伸缩性，既可以支持单进程独立系统的构建和开发，也可以支持基于SOA的服务体系开发和整合。在基础技术和安全性方面，解放开发人员研究成本，由框架提供所有基础技术的选项，封装，常规安全防御的的处理等。

核心特性：

1. 基于开源主流基础技术栈技术，主流开发人员学习成本几乎为零。
2. 核心最佳代码实践封装+组件能力扩展模式，在规范统一的同时，也为开发人员提供学习进阶的参考。
3. 自动代码生成，规范，快速，解放重复劳动，提高业务开发效率70%
4. 通过框架提供类金融行业的应用级安全统一处理。

## 文档
* [初始化环境](env.md)
* [快速开始](quick-start.md)
* [代码生成指南](acooly-coder.md)
* [框架组件介绍](components.md)
* [Acooly框架开发指南](acooly-guide.md)
* [视图层开发指南](acooly-guide-boss.md)

## 版本

### v4.x

* 基于jdk8、spring4、spring-boot重构框架
* 提供统一的jpa、mybatis支持
* 提供分布式session、分布式cache能力
* 引入dubbo组件支持
* 引入开发者模式，提供开发效率

### v3.x
基于spring3.x+jdk1.6/1.7, 采用spring import和package scan结合方案；持久化方案支持：jpa，mybatis和jdbc；视图层支持jstl和freemarker，boss采用easyui。

封装时间：2012年

### v2.x
基于spring2.5+jdk1.5/1.6，采用纯spring xml方式配置。

时间：2009年

### v1.x
