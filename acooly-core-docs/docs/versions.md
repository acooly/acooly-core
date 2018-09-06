## 版本说明

### v4.x

* 基于jdk8、spring4、spring-boot重构框架
* 提供统一的jpa、mybatis支持
* 提供分布式session、分布式cache能力
* 引入dubbo组件支持
* 引入开发者模式，提供开发效率

### v3.2.2

* 2016-11-02 - 修改PageInfo支持序列化；增加isNext() 和 isPrevious()两个帮助方法 - [zhangpu] 32f5003
* 2016-09-07 - 增加mybatis的配置文件打包规则 - [zhangpu] ff7b567

### v3.2.1

* 2016-09-06 修改DynamicListQueryDao接口的查询方法，从query修改为list

### v3.2.0

>特别注意：该版本新增Mybatis特性后，原有的EntityDao.query(Map<String,Object>,...) rename 为 list，如果有直接调用DAO层自动封装代码query(非分页的重载方法，MyBatis不支持接口定义方法重载)，则不兼容，需要重构下代码。新版本上线后，框架服务层及以上全兼容~

* 2016-08-23 01:35:25  zhangpu  升级基础框架的dao层，支持Mybatis作为持久化方案，并对Mybatis的使用进行统一封装，分页支持和统一配置
* 2016-08-23 01:34:07  zhangpu  Dao基础框架中的EntityDao接口重构query方法为list，目的是兼容MyBatis不支持DAO的mapper不支持方法重载的特性
* 2016-08-23 00:31:57  zhangpu  优化Strings.maskReverse方法，支持制定mask字符的长度


### v3.1.16

* 2016-08-08 11:26:56  zhangpu  [mid] Money对象在构造传入null或空字符串时，直接作为0处理
* 2016-08-08 11:17:53  zhangpu  [fix] 所有自定义的jsr303validator都支持自定义消息，包括Money,CertNo,MobileNo,HttpUrl等；修复Money的最小值消息配置错误，无法正确显示配置消息问题

### v3.1.15

* 2016-07-11 14:18:31  zhangpu  修改jdbcTemplate封装中的getDbtype为protected，子类可用。

### v3.1.14

* 2016-06-18 19:02:37  zhangpu  新增控制层自动封装方法的安全控制，为兼容原版本，默认开启所有mapping，可在之类定义覆盖打开的映射，主要针对前台portal的权限泄露问题~
* 2016-06-15 23:53:33  zhangpu  修改打印请求头和响应头的日志级别默认为debug
* 2016-06-15 23:49:47  zhangpu  解决公共工具包间的依赖版本冲突

### v3.1.13

* 2016-06-14 23:29:01  zhangpu  增加两个通过field获取对象值和设置对象值的工具方法
* 2016-06-09 01:29:04  zhangpu  增加客户端连接池的状态信息
* 2016-06-09 00:09:26  zhangpu  调整请求响应日志

### v3.1.12
* 2016-06-02 16:57:00  zhangpu  增加zip文件解压工具~

### v3.1.11

* 2016-05-28 18:28:35  zhangpu  zip压缩支持zip内建目录
* 2016-05-16 13:54:03  zhangpu  增加RSA的性能测试代码和非对称加密的Cipher线程cache，可选在需要的时候采用。


### v3.1.10

* 2016-05-13 17:57:36  zhangpu  增加几种公私钥加载方式。从证书文件加载公钥，从pfx文件加载私钥和公钥等
* 2016-05-13 17:54:29  zhangpu  Fix:Logback整合listener不支持配置文件在jar包中的加载问题~
* 2016-05-13 17:47:39  zhangpu  Paths工具类增加获取系统临时文件目录方法
* 2016-05-13 16:45:29  zhangpu  整理pom.xml的发布配置，忽略点组件工程的本地测试配置文件
* 2016-05-02 16:22:56  zhangpu  修复流式写HttpServletResponse的默认contentType逻辑判断错误
* 2016-05-02 16:01:25  zhangpu  为converUtils增加泛型方法convert
* 2016-05-02 01:14:39  zhangpu  FIX：根据spring环境感知生产配置文件名是丢掉“.”的BUG


### v3.1.9
* 2016-04-30 02:45:50  zhangpu  增加RSA算法工具了到新的security包，同时迁移Cryptos工具到security包
* 2016-04-27 23:13:41  zhangpu  修复Strings的isBankDefault工具类的空字符串判断错误的BUG
* 2016-04-26 14:38:11  zhangpu  优化ConfigurableConstants类，支持profie（initWithProfile方法），同时增加getProperty(key,class<T>, T defailtValue)工具方法，方便配置参数转型
* 2016-04-01 00:29:26  zhangpu  增加工具方法：getFields(Class<?> clazz):根据类型返回类型所有的属性Field，支持继承向上搜索
* 2016-03-30 14:03:26  zhangpu  删除废弃单元测试
* 2016-03-30 14:02:53  zhangpu  新增gid和oid统一生成规则
* 2016-03-30 01:15:47  zhangpu  修改AbstractJdbcTemolateDao默认注入的jdbcTemplate为protected，之类可用；修改KetamaHashs的getPrimary方法为public

### v3.1.8
2016-03-30 00:41:38 增加katamahash通用算法和工具类；
2016-03-30 00:41:38 改写Ids的序号计数，采用AtomicLong


### v3.1.7
* 2016-03-29 01:44:54 废弃扩展，回归原生，使用聚合代替继承(com.acooly.core.common.dao.jdbc.AbstractJdbcTemplateDao)
* 2016-03-25 03:11:15 增加Paths简单工具
* 2016-03-21 20:08:46 修改OrderBase和ResultBase为普通类

### v3.1.6
* 2016-03-21 03:19:09 添加对自定义JSR303类型的测试
* 2016-03-21 03:18:29 新增Parameterize的LinkedHashMap抽象首先，重构内部服务参数和返回值基类名称
* 2016-03-20 03:03:30 增加yiji的反射和基础类型转换工具
* 2016-03-20 03:03:00 增加yiji的copier工具
* 2016-03-20 03:02:25 增加yiji的ToString工具
* 2016-03-20 00:24:32 rename interface and abstract class for order and result
* 2016-03-20 00:15:27 修改参数定义BUG
* 2016-03-19 23:47:08 增加参数化接口
* 2016-03-19 23:02:11 工具包中新增自包service，提供内部RPC通讯的入参和出参的接口和基类：OrderBase和ResultBase
* 2016-03-19 22:59:56 CST 作废EntityStatus和ResultEnum，使用新的Messageable和SimpleStatus代替
* 2016-03-19 22:57:31 增加基于code和message的通用枚举的接口定义Messageable代替原有的ResultCode接口，同时增加ResultStatus和SimpleStatus两个常见的状态枚举
* 2016-03-19 19:29:01 增加Validators对JSR303的groups支持
* 2016-03-19 18:57:00 增加jsr303验证框架，自定义消息和工具类：validators,代替原来的validates
* 2016-03-19 15:30:42 增加Jsr303验证框架自定义工厂和错误消息中文化，并添加默认的JSR303默认配置文件
* 2016-03-19 15:29:59 增加Jsr303验证框架自定义工厂和错误消息中文化
* 2016-03-19 15:10:43 增加根据profile对应的配置文件名称，规则：fileName.dev.extentionName

### v3.1.5

* 2016-03-17 增加：Spring Proxy Bean 基类，用于实现框架及业务开发中接口默认实现与动态注入实现的代理
* 2016-02-28 增加deploy.sh,支持linux和mac自动脚本发布
* 2016-02-28 增加IP工具，从yiji拷贝，曾Ids能力支持生产指定长度的唯一ID

### v3.1.4

* 2016-01-17 21:06:34 CST  zhangpu 增加Strings工具的maskReverse方法，提供按前置和后置多少位保留不mask的工具，同时提供用户名，手机号码，银行卡，身份证等常用mask工具
* 2016-01-10 20:33:23 CST  zhangpu 增加运行环境操作工具类Profiles，提供常用的环境相关的静态方法。如：判断当前是什么环境，设置环境参数等~

### v3.1.3
* 2015-11-29 23:55:07 CST  zhangpu 基础日期时间类增加季度和周相关的工具方法
* 2015-11-25 22:35:18 CST  zhangpu 修改http请求跳转日志为debug
* 2015-11-25 02:10:59 CST  zhangpu http support sessionKeeping and mult-cookies header. 
* 2015-11-19 01:47:35 CST  zhangpu 通过JPA封装API，在save和update时，自动设置创建和更新时间
* 2015-11-19 01:47:03 CST  zhangpu 删除spring事务单元测试基类的默认@ContextConfiguration配置
* 2015-11-19 01:44:52 CST  zhangpu 修复getRequestFullPath的queryString为null时的BUG
* 2015-11-13 20:25:15 CST  zhangpu 增加返回值枚举的公共接口定义；增加通用尸体状态枚举,提供常用的enable和disable状态
* 2015-11-09 14:18:44 CST  zhangpu 增加通用多环境发布脚本
* 2015-10-29 00:31:09 CST  zhangpu 增加方法：getHost提供获取当前请求的Host的能力
* 2015-10-23 17:11:40 CST  zhangpu acooly-core删除对taglibs-standard的支持，与jstl1.2冲突
* 2015-10-19 23:00:00 CST  zhangpu 修改统一日志级别为error

### v3.1.2
* 2015-10-15 20:19:17 CST  zhangpu 扩展springMvc的jstlView为ResourceCheckJstlView,实现判断视图对应的物理文件是否存在。应用于springMvc配置，实现优先选择jstl视图，如果不存在则查找对应的同名freemarker（jar包内）视图。
* 2015-10-15 12:14:19 CST  zhangpu 修改springMvc配置逻辑：视图解析优先JSP，然后再使用Freemarker,找不到最后使用全局静态文件访问转移给默认WEB服务器
* 2015-10-12 10:42:21 CST  zhangpu 修改freemarker刷新文件为3600秒

### v3.1.1
* 2015-10-12 10:13:27 CST  zhangpu 增加静态资源访问filter，实现自适配访问webapp和classpath下的资源
* 2015-10-02 19:00:06 CST  zhangpu 修改编译打包规则，包含freemarker默认配置properties


### v3.1.0
因基础核心在springMVC的封装部分兼容支持了freemarker,为不影响现有办法系统的正常使用，我们升级一个二级版本，该版本的代码与v3.0.5完全一致。

### v3.0.5

* [重要更新] 扩展springMVC基础配置，可以同时支持freemarker和JSP两种视图，默认优先解析freemarker模板，如果不存在则按JSP处理。freemarker主配置文件为：classpath:spring/acooly-freemarker.properties. 

	配置如下：
	
	    # 如果变量为null,转化为空字符串,比如做比较的时候按照空字符做比较
	    classic_compatible=true
	    # 去掉多余的空格
	    whitespace_stripping=true
	    # 模版更新时间(秒)。开发时，在目标工程替换为1秒，便于调试
	    template_update_delay=3600
	    # 本地locale,国际化时非常有用
			locale=zh_CN
	    default_encoding=utf-8
	    url_escaping_charset=utf-8
	    tag_syntax=auto_detect
	    datetime_format=yyyy-MM-dd HH:mm:ss
	    date_format=yyyy-MM-dd
	    time_format=HH:mm:ss
	    number_format=0.######;
	    boolean_format=true,false
	    object_wrapper=freemarker.ext.beans.BeansWrapper
	    # auto_import="/common/index.ftl" as ui
* freemarker从v2.3.14升级为v2.3.23,解决freemarker与springMvc集成时，两个问题：
	* 对JSPTaglib的声明支持使用namespace方式。不用从jar包中扣取出tld文件并配置到WEB-INF/taglib下。
	* freemarker模板中可以通过BeanWrapper增强，使用JAVA API方式迭代Map<Intger,?>。
	
		语法如下：
		
			<#list map.keySet as key>
				${key}:${map.get(key)}
			</#list>


### v3.0.4
* 修复Dates.parse()多模式自适应解析BUG。

### v3.0.3
* 2015-08-30 02:46:40 CST  zhangpu 整理源代码注释
* 2015-08-30 02:46:17 CST  zhangpu 添加apache-license
* 2015-08-30 02:26:15 CST  zhangpu 优化基础国际化错误异常类抽象类：1、如果没有资源文件，忽略处理；2、重载getMessage方法，默认为super.getMessage()，如果有errorCode，则获取和生成国际化错误消息
* 2015-08-07 23:18:44 CST  zhangpu 修改aes基础方法为public
* 2015-07-23 17:48:20 CST  zhangpu pom.xml升级parent版本为3.0.0-SNAPSHOT

### v3.0.2  [兼容] 2015-07-14
* 增加文件导出的通用marshal实现：onMarshalEntity,为保存兼容，修改doMarshalEntity方法名为marshalEntity
* 使用thumbnailator重构Images工具类的resize图片缩略图生成，提高存JAVA解决方案的缩率图效果，废弃ImageIo的原生写法

### v3.0.1 [兼容]
* 为Strings增加mask方法

### v3.0.0 [非兼容]
1、改造maven依赖，最新版本的依赖全部依赖于acooly-parent-3.0.0
2、新增util.net包编写和手机网络访问相关工具。重新封装Https工具类。
3、升级单元测试基类，满足新的单元测试方案（静态方式设置profiles）
4、新增动态查询关系：notin和neq，并增加Demo

### v2.2.4.final
2.x最后版本。

### v2.2.3.20141010
1、建立integration标，专用于整合其他组件进行的二次封装和增强。
2、迁移日志和系统属性的整合listner到integration包
3、新增logback异步处理append

### v2.2.2.20140824
优化 SystemPropertyIntergrationListener 对spring.profiles.active参数加载和设置

### v2.2.20140630
1、变更配置文件名称 applicationContext-jpa 为 acooly-database
2、变更spring的profile配置方式

### v2.1.0
1、变更日志整合方案为slf4j+logback,不再支持log4j.兼容性变更
2、修复AbstractJQueryEntityController中删除方法的重复调用BUG
