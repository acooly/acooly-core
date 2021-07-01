<!-- title: Acooly框架核心  -->
<!-- name: acooly-core -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-11-14 -->
acooly-core
==================

## 项目简介

acooly框架是基于spring-boot提供快速开发、最佳实践，组件化的框架。

您可以从[框架介绍](acooly-core-docs/README.md)开始。

测试gitlab hooks

## changelog

### 5.0.0-SNAPSHOT.20210701

2021-07-01

* 新增：BigMoney数据类型的开发，以BigDecimal为基础提供可指定小数位数和舍取模式的金额数据类型，可用于高精度计算场景。并完成数据库（BigMoney <-> MySQL Decimal(20,8)）和Web/Json的映射集成。

### 5.0.0-SNAPSHOT 2021-06-01

2021-06-01

* fixed: 为BusinessException内部的Messageable匿名实现增加toString方法，便于日志打印。

### 5.0.0-SNAPSHOT 2021-05-31

2021-05-31

* 2021-05-31 - 为BusinessException增加toString()：（code:message:detail），便于日志打印。 - [zhangpu] 48d746d8
* 2021-05-31 - fixed: 合并森林对异常体系的调整。包括：BusinessException的废弃构造兼容新的三元模式；appservice组件的异常拦截器兼容老的构造产生的BusinessException。 - [zhangpu] 19af957f
* 2021-05-28 - fixed: 为Messageable接口增加Serializable继承，以解决匿名Messageable实现支持序列化，防止Dubbo远程接口序列化失败; 完善Messageable接口及实现的标准化javadoc - [zhangpu] 693e2732
* 2021-05-09 - 优化：文件操作基础控制器优化文件删除，采用Files.deleteSafety，保障不会应应用BUG或配置错误造成错误删除根或系统目录等；迁移FileUploadError枚举到acooly-common中为FileOperateErrorCodes,全局可用。 - [zhangpu] bd6b3887
* 2021-05-09 - 新增：Files工具类，扩展common-io的FileUtils，增加：isSave(File file):判断文件及路径是否安全（非FS的根，非操作系统系统路径），deleteSafely(File file): 安全静默删除文件（不安全则跳过，无异常）。 - [zhangpu] fcdc41af
* 2021-05-06 - 优化：acooly-archetype-simple的AcoolyCoder代码 - [zhangpu] 57e7a6d7
* 2021-04-09 - 优化：根据优化后的BusinessException和ResultBase的三元消息体系，优化AppService的自动异常处理逻辑 - [zhangpu] 79cad2fc
* 2021-04-09 - 优化：转换OrderCheckException为ResultBase的参数错误码(ResultCode.PARAMETER_ERROR)+错误详情(detail) - [zhangpu] 1d380f43
* 2021-04-09 - 优化：规范ResultBase的status和code/message/detail关系和用法。 1、status只标志成功/处理中/失败(其他)三类状态，成功/处理中/失败取值为ResultStatus，当为失败时，可以具体使用其他Messageable接口实现。 2、code/message/detail/表示
  结果的消息，常用取值可从ResultCode枚举获取，但也可以直接设置字符串或Messageable接口实现 3、当status=success时，code/message为ResultCode.SUCCESS,detail为空，表示结果为成功。 4、当status=processing时，code/message为ResultCode.PROCESSING,detail为空，表示结果为处理中。 5、status为其他值时，都表示错误。 - [zhangpu] b5aa1081
* 2021-04-07 - 优化: 文件上传的异常处理，采用枚举提供三元错误异常和日志 新增：doUpload(request,entity)方法，提供通过表单命名自动上传文件并绑定到对应的实体属性的特性 - [zhangpu] 7379e6e0
* 2021-04-06 - 文件上传默认允许的扩展名增加jpeg和pdf - [zhangpu] 1ebb7a3a

### 5.0.1-SNAPSHOT

* [兼容] 对于基础类型的定义，独立为最小化依赖的模块，最为ACOOLY所有组件和项目的最底层依赖（兼容），可用于OpenApi报文及外部SDK使用。
* [兼容] 从parent中去除默认的所有包依赖，包括：日志，apache和google的工具包，spring等，迁移到：acooly-common中
* [兼容] 合并acooly-common和acooly-common-facade
* [兼容] 新增acooly-common-type: 基础类型（枚举，标记，扩展类型，dto等）-> 可用于外部（比如SDK和Client工具），依赖关系： `acooly-common -> acooly-common-type`
* [兼容] acooly-common-type只依赖：validation-api 和 lombok  
    
    