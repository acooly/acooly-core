Acooly框架组件介绍
====

## 1 简介
组件库是acooly框架在历史应用中，总结和收归的可复用场景的仓库，主要解决高质量的复用问题，以快速应用到业务中，提高项目的整体质量和效率。按使用场景和范围我们把组件库分为：

* **基础组件库**：面向系统的基础能力的组件（系统都需要）
包括：容器，连接池，jpa，mybatis，文件(分布式)存储和访问，自动化测试，web框架扩展，安全体系，日志组件等。
* **应用组件库**：面向中间件和应用能力的组件。包括：信息发送（短信/邮件/push），验证码，消息（MQ），调度（定时），事件（分布式）处理，PDF/EXCEL/CSV,dubbo封装，可控线程池，流程，缓存（分布式）等
* **业务组件库**：面向业务的可完成服务的单元组件。包括：会员组件，账务组件，积分组件，营销活动组件，实名/人认证，抽奖组件，仓库组件等。
* **门户组件库**：面向对外提供服务的窗口服务组件。包括：网关服务组件，网关管理组件，自动文档生成组件，App基础功能组件，App接入及管理组件，后台管理组件，业务审计组件，门户网站组件库，CMS组件。


## 2 组件应用

使用组件只需要在maven中配置依赖。比如需要使用dubbo组件，maven中增加如下依赖:

```xml
  <dependency>
    <groupId>com.acooly</groupId>
    <artifactId>acooly-component-${name}</artifactId>
  </dependency>
```


当依赖了组件jar包时，也可以通过`enable`参数关闭组件。比如关闭dubbo组件`acooly.dubbo.enable=false`。大多数组件都提供`enable`配置开关。

## 3 组件设计

### 3.1 设计原则

框架按照`高内聚、低耦合`的原则，对应用需要的能力进行了封装。组件提供相关内容，组件之间可以依赖。

所有组件的maven坐标为`com.acooly:acooly-component-*`，当依赖了组件时，应用就有具有相关的能力。在`spring`的世界里，能力由`Bean`提供，所以组件就是一系列能力相关`Bean`的集合。


### 3.2 设计说明

* src/main/resources/META-INF/spring.factories

    spring boot 通过查找此配置文件，来加载配置类等其他扩展

* 组件配置类

    `组件配置类`用于把配置转换为POJO,并提供IDE提示功能。比如`com.acooly.module.tomcat.TomcatProperties`,在IDE(IntelliJ IDEA/STS)中输入`acooly.tomcat`,IDE会提示`tomcat`组件有哪些具体的配置项


* 组件初始化器

    `组件初始化器`用于在spring容器初始化之前控制、改变组件的默认行为。

    比如`com.acooly.module.tomcat.TomcatComponentInitializer`配置反向代理相关参数。

    比如`CacheComponentInitializer`配置redis连接池、开发模式启动内置redis。

    `spring.factories`中配置key为`com.acooly.core.common.boot.component.ComponentInitializer`

* 组件日志初始化器

    组件可以定义组件相关的日志，通过继承`AbstractLogInitializer`。比如`com.acooly.module.ds.DruidLogInitializer`定义慢查询日志。

    `spring.factories`中配置key为`com.acooly.core.common.boot.log.initializer.LogInitializer`

* 组件自动配置

    `组件自动配置`用于创建spring beans。比如`com.acooly.module.tomcat.TomcatAutoConfig`扩展spring-boot tomcat。

    `spring.factories`中配置key为`org.springframework.boot.autoconfigure.EnableAutoConfiguration`

* 组件数据库脚本初始化

    当组件使用到数据库时，需要配置数据库脚本初始化器，当开发同学使用到组件时，会自动执行数据库脚本，初始化数据库。

    以sms组件为例，`com.acooly.module.sms.SmsAutoConfig#smsScriptIniter`

    注意：脚本必须放在`src/main/resources/META-INF/database/${componentName}/${dbType}/xxx.sql`



* 组件mvc命名空间

    如果组件包含页面，controller，最好在路径中包含命名空间，防止冲突。


* 组件依赖组件

    组件会依赖其他组件来提供能力。比如`acooly-component-web`依赖`acooly-component-tomcat`提供web服务。依赖`acooly-component-cache`提供分布式session能力。

* 组件扩展

    组件扩展分为两类：
    
    1. 外部使用方关心组件处理的结果：使用event组件来扩展，处理完后发布事件。鉴于外部处理的时长我们不清楚，使用异步发送。
    2. 外部使用方参与组件处理的中间过程：使用spring ioc机制，框架提供默认行为(通过@ConditionalOnMissingBean注册默认行为)，业务方提供扩展行为

## 4 组件库

### 基础组件库

| 组件           | 组件名称（坐标）     | 特性                                                                                                                                                                           | 场景                                             |
| ---------------- | ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------- |
| Tomcat组件     | acooly-component-tomcat      | tomcat8嵌入版；性能优化；参数化                                                                                                                                      | 所有需要web和服务的系统                  |
| Web组件        | acooly-component-web         | 对springMvc的扩展和增强：支持多模板语言（jsp/fm）；强制https；fm优化和扩展；                                                                          | 所有需要web交易或展示的场景            |
| JPA组件        | acooly-component-jap         | 统一实现EntityDao，提供场景的增删改查，分页查询等功能；自动注册自定义类型Money；自动加载应用数据初始化脚本；提供执行自定义数据库脚本功能 | 持久化用到jpa可使用此组件               |
| Mybatis组件    | acooly-component-mybatis     | 此组件提供mybatis相关能力,增加单表增删改查通用能力，不用写一行sql语句，单表的操作能力全覆盖。                                           | 持久化用到mybatis可使用此组件           |
| 对象存储组件  | acooly-component-obs         | 抽象封装存储，使用一套存储接口，可以选择不同的存储方式:阿里OSS/fastdfs。                                                                           | 适合各种网站、开发企业及开发者有对象存储需求的使用 |
| 文件上传组件  | acooly-component-ofile       | 此组件文件上传、下载、访问等能力                                                                                                                                 | 系统本地文件管理                           |
| 缓存组件     | acooly-component-cache       | 提供分布式缓存服务；并spring声明式缓存进行优化。                                                                                                            | 用到redis缓存场景                            |
| Dubbo组件      | acooly-component-dubbo       | 此组件提供dubbo相关能力                                                                                                                                                 | Rpc用到dubbo调用                               |
| 事件组件     | acooly-component-event       | 提供jvm内事件处理发布、处理机制支持事件处理器同步/异步，相对于spring的机制更灵活(spring要么都异步，要么都同步);性能更好;分离事件发布、业务事务，应用可以灵活选择事件和事务的关系 | 解耦逻辑，用此事件处理                  |
| FilterChain组件  | acooly-component-filterchain | 此组件提供FilterChain,应用可以此组建快速组装FilterChain                                                                                                          | 有多个filter需要执行顺序以及重入情况 |
| 线程池组件  | acooly-component- threadpool | 统一应用/组件，使用同一线程池；包裹任务并打印异常信息                                                                                                  | 应用需要异步线程池执行                  |
| Security组件   | acooly-component-security    | 此组件提供后台系统相关能力;提供后台管理系统;提供基于资源的权限管理;提供统一的后台界面骨架;提供后台用户的增删改查，登录功能;提供组织机构管理功能;支持一个用户多角色特性 | 后台管理、权限管理、用户、组织机构管理 |
| 防御组件     | acooly-component-defence     | 此组件常见的安全机制;提供xss防御功能;提供csrf防御功能;提供url加解密功能                                                                             | 大多web应用都需要，安全防范            |
| 数据库连接组件  | acooly-component-ds          | 此组件提供数据库连接池，检查数据库规范执行，对慢查询和大数据集sql打印日志到`sql-10dt.log`文件                                              | 所有需要数据库连接的地方               |

### 应用组件库

| 组件           | 组件名称（坐标）     | 特性                                                                                                                                                                           | 场景                                             |
| ---------------- | ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------- |
| 调度定时任务组件  | acooly-component-scheduler    | 分布式定时任务。可在后台页面动态添加、删除、修改定时任务。任务类型支持：本地任务、http任务、dubbo任务       | 定时调度，需要定时执行任务       |
| appservice组件  | acooly-component- appservice  | 提供服务器通用处理能力，减少代码量和统一处理规范，包括打印服务请求响应日志，请求对象检查，异常统一处理 | 需要请求对象检查、异常统一处理场景 |
| 短信发送组件  | acooly-component-sms          | 提供短信的发送，查询功能、提供模板短信、目前提供6-7个短信通道、提供mock短信、提供短信黑名单、提供短信发送记录归档 | 需要接入短信发送、短信模版、发送记录归档 |
| 邮件发送组件  | acooly-component-mail         | 此组件提供邮件服务能力，支持boss或者配置文件配置邮件freemarker模板；支持html邮件、附件、多个收件人等常用邮件功能；提供同步、异步发送邮 | 邮件服务                                  |
| 验证码组件   | acooly-component- captcha     | 提供各种验证码的生成、验证，管理验证码的生命周期,可用于生成验证token                                                       | 需要验证码生成管理、以及token生命周期管理场景 |
| 参数配置组件 | acooly-component- config      | 通过boss后台或者服务新增配置、配置读取时首先读取进程内缓存，然后读取redis，最后读取数据库、配置更新时，通知修改进程内缓存、redis缓存 | 在很多场景，配置需要动态新增、修改 |
| 日志收集组件  | acooly-component-olog         | 提供后台管理系统审计日志收集，展示等功能；提供annotation配置元数据；提供操作日志收集、存储、展示、归档等功能；SOA场景下，子系统审计日志自动发送到存储服务 | 后台审计日志收集                      |
| 单点登录组件  | acooly-component-sso          | 子系统按照单机开发模式开发boss后台页面，提供简单快速集成单点登录能力；在分布式场景下，sso通过iframe集成子系统页面；单点登录会对用户权限、资源权限认证 | 后台多个boss系统集成在一个应用、方面管理 |
| pdf组件         | acooly-component-pdf          | 提供html转换为pdf能力；转换流程dataVO + freemarker模板 -> iTextRender -> 输出pdf；提供pdf加水印功能；提供pdf自定义字体功能 | 在一些需要生成pdf、比如合同、需要加水印 |
| 序列号生成组件  | acooly-component-seq          | 此组件提供自增序号生成器；基于数据库实现自增需求，类似于oracle中的seq；提供批量序号获取，提高性能             | 高性能序列号自增                      |
| 测试组件      | acooly-component-test         | Spring-boot单元测试支持；参数化测试支；集成测试支持；Dubbo测试支持                                                               | 单元测试、dubbo测试、集成测试、参数化测试 |


### 业务组件库

| 组件       | 组件名称（坐标）  | 特性                                                                                                                                                                                 | 场景                                                  |
| ------------ | ------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------- |
| 会员组件 | acooly-component-member   | 1.注册/邮件或手机激活/登录；2.手机号码/邮箱绑定和修改；3.密码安全：修改/重置/安全问题；4.实名认证：企业和个人/解析；5.扩展信息：业务/联系/基本/资产等信息；6.完善的扩展体系：事件/拦截/配置 | 任何需要会员的业务系统。                    |
| 账务组件 | acooly-component-account  | 多账户/复式记账/单笔/批量/账务交易/冻结/解冻/进出账流水/账务分析/账务核对验证/交易码自定义和扩展                                            | 1、系统内需要建立虚账体系；2、可用于任何余额类账务记账 |
| 积分组件 | acooly-component-point    | 积分账务/单笔/批量交易/流水等                                                                                                                                             |                                                         |
| 库存组件 | acooly-component-store    | 1、仓库信息管理，品类信息管理服务；2、商品信息和库存基础管理服务                                                                                          | 简单仓库和商品管理                             |
| 抽奖组件 | acooly-component-lottery  | 活动最大人数，单人最大次数控制；支持多种类型的奖项定义；按时间控制奖项的最大出奖数；支持奖项的权重配置；支持作弊               | 任何需要抽奖形式的活动和业务场景。砸蛋，转盘等 |
| 营销活动组件 | acooly-component-activity | 1、活动定义和管理；2、活动策略和事件配置；3、活动奖励配置                                                                                                       | 任何需要营销活动的业务场景。              |

### 门户组件库


| 组件      | 组件名称（坐标）   | 特性                                                                                    | 场景                                |
| ----------- | -------------------------- | ----------------------------------------------------------------------------------------- | ------------------------------------- |
| 行为日志 | acooly-portlets-actionlog  | 1.多端统一访问日志收集；2.访问日志行为分析                             | 互联网平台多段访问日志和行为分析。 |
| 用户反馈 | acooly-portlets-feedback   | 1、多端收集反馈信息（建议/咨询/问题）；2、后端反馈流程处理     | 网站和App用户反馈功能        |
| 网站配置 | acooly-portlets-siteconfig | 客服热线/服务时间/服务邮箱/服务微博/服务QQ/服务QQ群/微信公众号…等参数化的配置和缓存处理。 | 所有网站的常规配置           |
| App组件   | acooly-component-app       | 1、App安全访问机制和体系；2、首屏欢迎界/广告滚动展示/banner管理；3、版本管理/崩溃报告 | 所有App的业务开发              |
| CMS组件   | acooly-component-cms       | 1、通用内容管理系统（多媒体/缓存）；2、网站banner管理；3、友情链接和合作伙伴管理 | 1、任务需要内容管理的场景；2、网站 |
| OpenAPI组件 | acooly-compoment-openapi   | 桥接OpenAPI-framework框架应用于进程内，并透明使用网关所有功能。    | 业务应用与OpenAPI服务单应用集成场景。 |
| Apidoc组件 | acooly-compoment-apidoc    | 为OpenApi提供自动化生成文档和开放平台网站的组件                       | OpenAPI和开放平台场景          |

 
### 支付组件库
 
| 组件               | 组件名称（坐标）         | 特性                                   | 场景                       |
| -------------------- | -------------------------------- | ---------------------------------------- | ---------------------------- |
| 上海银行存管支付组件 | openapi-client-provider-bosc     | 此组件提供上海银行存管相关能力SDK封装. | P2P银行存管              |
| 富滇银行存管支付组件 | openapi-client-provider-fudian   | 此组件提供富滇银行存管相关能力SDK封装. | P2P银行存管              |
| 网商银行支付组件 | openapi-client-provider-wsbank   | 此组件提供网商银行存管相关能力SDK封装. | 农村家电O2O服务        |
| 宝付支付组件   | openapi-client-provider-baofu    | 此组件提供宝付支付接口相关能力SDK封装. | 各种充值、提现应用场景 |
| 兴业威富通支付组件 | openapi-client-provider-wft      | 此组件提供威富通接口相关能力SDK封装. | 微信、支付宝各种收单场景 |
| 富友支付组件   | openapi-client-provider-fuyou    | 此组件提供富友支付接口相关能力SDK封装. | 各种充值、提现应用场景 |
| 翼支付组件      | openapi-client-provider-yipay    | 此组件提供翼支付接口相关能力SDK封装. | 各种风控和代扣场景  |
| 银盛支付组件   | openapi-client-provider-yinsheng | 此组件提供银盛支付接口相关能力SDK封装. | 各种充值、提现和收单应用场景 |
| 金运通支付组件 | openapi-client-provider-jyt      | 此组件提供金运通支付接口相关能力SDK封装. | 各种充值、提现应用场景 |
| 易宝支付组件   | openapi-client-provider-yibao    | 此组件提供易宝支付接口相关能力SDK封装. | 各种充值、提现应用场景 |
| 银联支付组件   | openapi-client-provider-yl       | 此组件提供银联支付接口相关能力SDK封装. | 各种充值、提现应用场景 |
| 新浪支付组件   | openapi-client-provider-sinapay  | 此组件提供新浪支付接口相关能力SDK封装. | P2P场景                    |
| 微众银行支付组件 | openapi-client-provider-webank   | 此组件提供微众银行接口相关能力SDK封装. | 各种充值、提现应用场景 |
 