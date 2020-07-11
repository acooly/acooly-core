<!-- title: 代码生成  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-04-27 -->

acooly coder自动代码生成工具
====

## 1 简介
Acooly Coder是为Acooly框架配套的专用代码生成工具，设计目的为跟进Acooly框架封装的最佳代码实践，快速生成业务程序的骨架代码和基本功能，最大程度的减少程序员的重复劳动及规范统一代码风格和规范。主要特性：

* 基于数据库表生成基于实体的端到端代码。
* 生成的代码包括：实体，DAO(MyBatis/JPA)，服务层，管理控制器，管理页面(多条件排序查询,添加，编辑，删除，上移，置顶，导入，导出等)
* 视图层的生成支持JSP和Freemarker两种模板界面
* 支持批量生成多个表到一个模块
* 支持去除表前缀和增加自定义实体前缀
* 支持一套自定义的数据库列备注规范，可生成下拉选择,表单类型验证和mask
* 可选：生成开放平台的API对应的message定义


## 2 工具获取

### 2.1 Idea插件

当前插件为发布到Intellij Idea的marketplace，请直接下载后，拖动到你的idea中即可完成安装。目前支持的版本：idea2018.1及以上版本。

#### 2.2.1 下线安装

<div>
<button style="width: 200px;height:30px;font-size:14px;" type="button" onclick="window.open('https://plugins.jetbrains.com/embeddable/card/14462')">IntelliJ插件首页</button>
</div>

点击这里下载IDEA的AcoolyCoder插件: [acooly-coder-plugin-1.0.2-release.zip](http://acooly.cn/nexus/service/local/repositories/releases/content/cn/acooly/acooly-coder-plugin/1.0.2/acooly-coder-plugin-1.0.2-release.zip)

安装并重新启动IDEA后，在你工程任何需要生成代码的包(package)上，右键菜单底部：Acooly -> AcoolyCoder


#### 2.2.2 插件截图
<div>
<div style="display:inline;"><img width="45%" src="/docs/res/coder/2.conn.jpg"></div>
<div style="display:inline;"><img width="45%" src="/docs/res/coder/3.db.jpg"></div>
<div style="display:inline;"><img width="45%" src="/docs/res/coder/4.table.png"></div>
<div style="display:inline;"><img width="45%" src="/docs/res/coder/5.generate.png"></div>
</div>


### cli工具

acooly coder的发布包采用maven方式发布，目前只提供cli工具。
仓库地址：http://${host}/nexus/content/repositories/releases/

工具包maven坐标（请根据需要更新对应的版本,当前版本：4.0.0-SNAPSHOT）：

```xml
<dependency>
  <groupId>com.acooly</groupId>
  <artifactId>acooly-coder</artifactId>
  <version>${acooly.coder.version}</version>
  <classifier>distribution</classifier>
  <type>zip</type>
</dependency>
```

>使用说明：拉取发布包后直接解压，application.properties为配置文件，请跟进生成的需求配置，然后运行start.sh/start.bat生成代码。

## 3 设计手册

### 3.1 框架约定

acooly框架为了方便开发和设计，以开发经验为基础，对使用acooly进行了部分设计上的约定，通过约定降低设计和开发难度，提高效率。当然，这也符合流行的约定大于配置的设计理念。

#### 3.1.1 程序结构约定

工程结构完全按maven j2ee工程骨架，这个可以通过acooly-archetype生成后就可以清楚展现，程序模块的设计开发有一下基本约定。

* 实体类（entity）必须继承AbstractEntity,并提供一个名称为id的属性作为实体的唯一标识，同时也映射到数据库的物理主键。
* 每个模块的程序分为domian（entity和领域domain合并），dao，service和controller四层。命名规则依次为:EntityDao,EntityService,EntityManageController(后台)和EntityPortalController
* 视图层命名约定为：${entityName/domainName}[List].ftl/jsp为列表或主界面, ${entityName/domainName}[Show].ftl/jsp为查看视图，${entityName/domainName}[Edit].ftl/jsp为创建和编辑公用视图。

#### 3.1.2 数据库设计约定

为了方便自动生成结构性代码，对数据库表结构设计进行部分约定，但都符合常规习惯。

* **表定义：**表名全部小写，不能以数字开头，必须添加备注，表名应该以模块或组件为前缀
* **物理ID：**每个表必须有以id命名的物理主键，且为数字类型，如：mysql为 `id  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID'`, oracle为number。
* **列定义：**列名称全部小写，不能以数字开头；如果存在多个自然单词的组合，使用下划线分隔(\_)。如："user\_type"，列定义必须添加备注 （备注规范请参考下一节详细介绍）
* **固定字段：**每个表必须添加`create_time`和`update_time`两个日期时间类型的字段
	mysql如下：
	
	```sql
	create_time timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
	update_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
	```	

* 如果有选项类型的字段，其选项值使用类json格式写入列备注字段，自动生成工具会自动为该列对应的属性和页面生成选项。如：表列为：`user_type` ,备注可以为：`用户类型 {normal:普通,vip:高级}`
* **列名唯一：**强烈要求选项类（自动生成枚举类的）字段项目全局唯一名称，否则会生成enum名称相同的枚举相互覆盖。
* **字符集：**，表的字符集选择：`utf8mb4/utf8mb4_general_ci`。
	>MySQL在5.5.3之后增加了这个utf8mb4的编码，mb4就是most bytes 4的意思，专门用来兼容四字节的unicode,utf8mb4是utf8的超集。强烈建议表的字符集设置为utf8mb4，在MariaDB情况下utf8会出现字符集不兼容，强烈建议字符集设置为utf8mb4，如：
       
	```sql
	CREATE TABLE `cms_content_body` (
	  `id` bigint(20) NOT NULL COMMENT '主键',
	  `body` text NOT NULL COMMENT '内容主体',
	  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容主体';
	```
	
### 3.2 列备注增强设计

默认情况下，备注只是用于生成的视图界面的label显示，但我们约定备注可设置类JSON格式，用于定于复杂的视图界面行为。

#### 3.2.1 语法

类JSON备注语法：

```json
{
	title: '中文label名称',
	type: '自定义数据类型，包括：money,percent,mobile...',
	alias: '业务类型别名，空间已定义好的枚举。gender，whether，animal等'
	// 当type="option"时，自定义的选项值
	options: {
		key1:'val1',
		key2:'val2'
	}
}
```
> 注意：
> 1. 备注JSON中的key可以没有单引号，但值必须有单引号，在有些数据库客户端软件中，无法输入英文单引号的情况，可输入中文单引号（工具兼容）
> 2. 只有title是必选的。但是如果只有title,请直接写入备注，不用JSON格式。
> 3. 类JSON结构备注不用换号。

以下是关键属性说明。

#### 3.2.2 自定义类型

type：自定义的数据类型，用于定义前端BOSS生成时的界面显示和表单验证对应处理。取值和效果情况如下：

|取值			|实体类型         |列表显示	          |表单         |备注
|------------|----------------|----------------|------------|-----------
| text       |String			|			       | 文本框     | 默认情况
| option     |Enum	          |key对应的中文名           |下拉选择     |需要同时设置options的map结构
| integer    |Integer		   |整数数字          |文本框：范围验证 |
| money|Money|两位小数的财务货币格式（20,000.00）|文本框/mask/范围验证|注意：数据库类型定义为BIGINT，单位：分
| percent|Integer|百分数|文本框/mask/范围0-100.00|数据库类型为int，存的是百分数。23.33表示23.33%
| mobile|String|手机号码|文本框/mask/长度11字|
|email|String|电邮|文本框/mask/格式和长度验证|
|idcard|String|身份证号码|文本框/mask/格式和长度验证|
|bankcard|String|银行卡号|文本框/mask/格式长度验证|
|url|String|链接|文本框/mask/格式验证|
|chinese|String|全中文内容|文本框/格式验证|
|account|String|用户账户|文本框/mask/格式长度验证|字母开头，由字母，数字和下划线组成的字符串



#### 3.2.3 内置枚举类型

alisa：内置的Enum类型，常用的Enum类型框架已提供，为防止重复定义，可使用别名方式代替：`type:'option',options:{}`的自定义定义模式。如果定义了alias，则表示`type=option`具体取值如下。

|取值			|枚举         	|名称	     |取值
|------------|----------------|-----------|-----
|whether	   |WhetherStatus	|是否开关	  |yes/no
|simple		|SimpleStatus	   |简单状态     |enable/pause/disable
|able        |AbleStatus      |开关状态     |enable/disable
|gender	   |Gender			|性别	      |male/female
|animal		|AnimalSign		|生效		   |十二生肖
|star			|StarSign			|星座		   |星座
|channel		|ChannelEnum		|渠道		   |WECHAT,WEB,ADNROID...

#### 3.2.4 自定义选项

自定义选项是通过列备注定义key/val结构的mapping，用于自动生成对应的枚举(或常量),已实现数据与label的分类和视图下拉选择。

首先，兼容老版本的定义模式。即：直接在备注中定义label+类JSON的选项值模式。例如：

类型 {normal:普通,vip:高级}		--> 自动生成枚举
状态 {1:OK,2:禁用}				--> 生成常量列表

>注意：老版语法中，类JSON的定义中值部分没有[单]引号

最新版本的自定义选项遵循签名的语法定义，采用类JSON格式。

* title：必选，label中文名
* type：取值必须是option
* options参数的值必选，是自定义mapping的定义（参考老版本定义），但值需要使用单引号(中英文都可以)

例如：

```json
{title:'类型',type:'option',options:{normal:'普通',vip:’高级‘}}
{title:'状态',type:'option',options:{1:'OK',2:'禁用'}}
```


## 4 代码生成

自动代码生成的方案与目前流行的方式设计上是基本一致的。主要核心设计为：

* 通过读取数据库的元数据，获得实体对应的表的所有列，类型，长度，可选，备注等信息，通过结构化处理作为自动代码生成的元数据。
* 通过freemarker作为模板引擎，定义一套或多套框架业务代码的最佳实践模板。
* 工具化自动生成目标代码，作为业务开发的start...

目前的工具主要通过配置文件驱动工具行为，主配置文件为`acoolycoder.properties`.

下面以一个案例来简单说明这个开发过程。

### 4.1 设计表


本案例中，我们以mysql为案例设计一个客户信息表，名称为:acooly_coder_customer, 表结构如下：

```sql
CREATE TABLE `acooly_coder_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(32) NOT NULL COMMENT '{title:’用户名’,type:’account’}',
  `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
  `birthday` date NOT NULL COMMENT '生日',
  `gender` varchar(16) NOT NULL COMMENT '{title:''性别’,alias: ‘gender’}',
  `animal` varchar(16) DEFAULT NULL COMMENT '{title:’生肖’, alias: ‘animal’}',
  `real_name` varchar(16) NOT NULL COMMENT '{title:’姓名’,type:’chinese’}',
  `idcard_type` varchar(18) NOT NULL COMMENT '{title:’证件类型’, type:’option’,options:{cert:’身份证‘,pass:’护照‘,other:’其他‘}}',
  `idcard_no` varchar(48) NOT NULL COMMENT '{title:’身份证号码’,type:’idcard’}',
  `mobile_no` varchar(11) DEFAULT NULL COMMENT '{title:’手机号码’,type:’mobile’}',
  `mail` varchar(64) DEFAULT NULL COMMENT '{title:’邮件’,type:’email’}',
  `customer_type` varchar(16) DEFAULT NULL COMMENT '{title:’客户类型’, type:’option’,options:{normal:’普通‘,vip:’重要‘,sepc:’特别‘}}',
  `subject` varchar(128) DEFAULT NULL COMMENT '摘要',
  `content` text COMMENT '详情',
  `done_ratio` int(11) DEFAULT NULL COMMENT '{title:’完成度‘,type:’percent’}',
  `salary` int(11) DEFAULT NULL COMMENT '{title:’薪水’,type:’money’}',
  `registry_channel` varchar(16) DEFAULT NULL COMMENT '{title:’注册渠道’, alias: ‘channel’}',
  `push_adv` varchar(16) DEFAULT NULL COMMENT '{title:’推送广告’, alias:’whether’}',
  `num_status` tinyint(4) DEFAULT NULL COMMENT '数字类型{1:A,2:B,3:C类型}',
  `status` varchar(16) NOT NULL DEFAULT '1' COMMENT '{title:’状态’, alias:’simple’}',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  `website` varchar(128) DEFAULT NULL COMMENT '{title:’网址’,type:’url’}',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='acoolycoder测试';
```


请将上面的表结构DLL在数据库中执行，生成本次演示的数据表。

> 本案例基于本系列的第一篇文章《acooly框架一:archetype》，假设你已生成了工程，名称为acooly-demo，并建立数据库acooly，然后成功运行的基础上.

### 4.2 配置

打开工具包后，你会看到根目录下有`acoolycoder.properties`文件，请打开进行配置。

application.propertes

#### 4.2.1 常用配置

```ini
# 数据库连接配置
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://127.0.0.1:3306/acooly-coder
jdbc.username=root
jdbc.password=123456
# 配置表明转换为实体名时，需要忽略的表前缀。例如配置：p_ 则表示p_customer(表名) -> Customer(实体类名)
generator.tableToEntityIgnorPrefix=a_acooly_
# 视图类型：ftlEasyboot/freemarker(默认)/jsp
generator.viewType=ftlEasyboot
# 管理界面的根目录
generator.manage.path=/manage/demo
# 代码作者
generator.code.author=acooly
# 代码版权
generator.code.copyright=acooly.cn
```

>推荐使用常用配置

#### 4.2.2 完整配置

```ini
#[database connection configurations]
## [必填][mysql]
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/acooly
jdbc.username=root
jdbc.password=123456

## Code generator configurations
# [必选]生成的代码防止的目标java工程根路径
generator.workspace=/Users/zhangpu/workspace/acooly4/acooly-coder/test
# [必选]生成的模块代码的根包
generator.rootPackage=com.ac.zhangpu
# [可选]视图方案：freemarker（默认）,jsp
generator.viewType=ftlEasyboot
# [可选]需要生成的可选模块(entity,dao和service是默认的) 可选: manage,portal,facade,api. 目前portal,facade和openapi在开发中不可用，多个模块采用逗号分隔。如：manage,facade
generator.modules=manage
# [可选]持久化方案(可选: jpa,mybatis，默认jpa)
generator.persistent.solution=mybatis
# [可选]表名转换为实体名称时，需要忽略的表的前缀。如:表明为dm_customer,配置该值为dm_ ,则生成的实体名称为Customer
generator.tableToEntityIgnorPrefix=dm_
# [可选] 是否生成枚举，默认为true
generator.enum.enable=true
# [可选]枚举名称采用组合Entity名称+属性名称
generator.enumName.assemble=false
# [可选]生成的模块的业务根路径，如果不填写，且配置了生成manage模块，则默认为/manage
generator.manage.path=/manage/zhangpu
# [可选]生成的模块的前端页面根路径(依赖generator.modules中有portal)，默认/portal
generator.portal.path=/portal/demo
# [可选]代码作者，默认:acooly
generator.code.author=acooly
# [可选]代码版权声明主体,默认:acooly.cn
generator.code.copyright=acooly.cn
```

完成配置后，请保持退出，准备运行工具生成代码

### 4.3 生成代码

运行环境基础要求JDK1.8，本工具支持同时生成多张表到同一个模块。

#### 4.3.1 工程内Main方法

如果你是通过acooly-archetype或mvna命令方式创建的新工程，在你的工程test模块下有对应的AcoolyCoder.java或acoolycode.properties文件，你可以直接按上面的配置说明调整配置后，Main方式运行AcoolyCoder.java生成代码。

>注意：AcoolyCode.java中代码设置的参数会覆盖acoolycode.properties的配置。你可以通过代码中的常量调整常用参数，代码中的设置优先级高于配置文件。


```java
public class AcoolyCoder {
    // 生成代码的目标模块
    static String MODULE_NAME = "acooly-coder-test";
    // 生成代码的根包
    static String ROOT_PACKAGE = "com.acooly.coder.test";
    // 生成代码的管理视图相对路径
    static String MANAGE_VIEW_PATH = "/manage/coder/";
    // 配置表名转换为实体名时，需要忽略的表前缀。例如配置：p_ 则表示p_customer(表名) -> Customer(实体类名)
    static String TABLE_IGNOR_PREFIX = "acooly_coder_";
    // 生成代码的表
    static String[] TABLES = {"acooly_coder_customer"};

    /**
     * 代码方式配置关键参数
     * <p>
     * 代码方式参数优先级高于配置文件
     *
     * @param args
     */
    public static void main(String[] args) {
        CodeGenerateService service = Generator.getGenerator();
        GenerateConfig config = GenerateConfig.INSTANCE();
        config.setWorkspace(getProjectPath() + MODULE_NAME);
        config.setManagePath(MANAGE_VIEW_PATH);
        config.setTableToEntityIgnorPrefix(TABLE_IGNOR_PREFIX);
        config.setRootPackage(ROOT_PACKAGE);
        service.generateTable(config, TABLES);
    }

    public static String getProjectPath() {
        String file = AcoolyCoder.class.getClassLoader().getResource(".").getFile();
        String testModulePath = file.substring(0, file.indexOf("/target/"));
        String projectPath = testModulePath.substring(0, testModulePath.lastIndexOf("/"));
        return projectPath + "/";
    }
}
```

直接运行main方式就OK。

#### 4.3.2 命令行方式

>该模式兼容V4版本，V5版本已废弃，请直接使用通过`acooly-type-*`骨架生成的工程的test模块中的Main方法方式运行。

解压开自动生成工具，根目录下存在start.bat,start.sh等启动文件，如果你的windows，则可以直接cmd进入到当前目录，运行start.bat，根据提示输入需要自动生成的表名称，回车即可。

* 如果你在一个模块中需要生成多个表，则多个表使用空格分隔
* 在使用命令前，请检测application.properties的配置文件正确配置

运行情况如下（我这里是mac环境，使用start.sh，windows环境类似）：

```bash
请输入需要生成的表名(多个表使用空格分隔):dm_customer
2016-06-07 23:28:31,934 DEBUG - Load metadata success--> dm_customer
2016-06-07 23:28:31,943 DEBUG - Configurations:
com.acooly.module.coder.generate.GenerateConfiguration@38294acb[
  workspace=/Users/zhangpu/workspace/eclipse/acooly-demo
  rootPackage=com.acooly.demo
  pagePath=/manage/demo
  templatePath=classpath:/template/easyui
  tableToEntityIgnorPrefix=dm_
]
2016-06-07 23:28:32,260 INFO  - Generate from [dm_customer] success.
```

生成成功后，一般情况，只需重新启动服务器，从controller中找到访问的URL，配置到权限管理系统则可以直接访问，完成开发的第一步。

### 4.4 运行查看效果

OK，如果上步成功，请回到你的IDE及对应的模块，你应该看到在原来空的 src/main/java目录下存在了新生成的代码，并编译通过。同时在src/main/resources/META-INF/resource/jsp/manage/demo（jsp模式）或src/main/resources/templates（freemarker）下存在了该模块的管理界面。


## 5 进阶

以上完成了一个acooly框架基础开发的案例，基本展示了acooly半自动代码生产开发模式。但在实际业务开发中这些远还不够，程序员根据实际业务的需求，还需要做很多工作，包括：多实体关联关系配置，业务逻辑服务编写和调用，页面的特殊定制等，但这些本应是程序员的职责，acooly框架提供的是把大量重复的工作自动化和工具化~，后续将逐步发布各种业务开发场景在前端和服务器端的开发说明，大量的组件能力说明~

具体对功能和界面的调整，请参考：

* [开发指南](acooly-guide.html)
* [管理后台开发指南](acooly-guide-boss.html)
* [业务前台开发指南](acooly-guide-portal.html)

## 6 更新说明

### 5.0.0-SNAPSHOT(2020-07-10)

* 2020-07-10 - 优化日志输出格式精简，同时设置系统变量，切换freemarker的日志为JUL，与工具整体一致（依赖最少） - [zhangpu] 50f2ca7
* 2020-07-10 - 新增generate.sso.enable参数，控制是否生成sso支持；修正list页面生成时initPage的参数未动态生成的问题。 - [zhangpu] 7c14678
* 2020-06-11 - 增加IDEA插件的文档说明 - [zhangpu] a4362c4
* 2020-06-10 - 特性增加：增加entityPrefix参数，用于配置生成的实体增加统一前缀 - [zhangpu] a7a873c
* 2020-06-10 - 新增特性：代码生成过程事件处理（AcoolyCoderEvent和AcoolyCoderListener），用于支持界面端生成进度条的有效展示。 - [zhangpu] 3d213de
* 2020-06-07 - 清理acooly-coder的冗余依赖，精简部署包大小 - [zhangpu] 4d6846a
* 2020-05-09 - 修正DateTime和Date两种数据类型应对java的Date对象，但也能区分格式化输出。 - [zhangpu] ebc803a
* 2020-05-08 - 调整Money类型的列表格式化为moneyFormatter - [zhangpu] 2192d9e

### 5.0.0-SNAPSHOT(2020-05-03)

* 2020-06-10 - 特性增加：增加entityPrefix参数，用于配置生成的实体增加统一前缀 - [zhangpu] a7a873c
* 2020-06-10 - 新增特性：代码生成过程事件处理（AcoolyCoderEvent和AcoolyCoderListener），用于支持界面端生成进度条的有效展示。 - [zhangpu] 3d213de
* 2020-06-07 - 清理acooly-coder的冗余依赖，精简部署包大小 - [zhangpu] 4d6846a

### 5.0.0-SNAPSHOT(2020-05-02)

* 2020-05-02 - 完成新版（bootstrap）的自动代码生成升级 - [zhangpu] 9a8f505

### 4.2.0-SNAPSHOT

* 支持freemarker和jsp两种视图，通过generator.viewType参数控制。

### 4.0.0-SNAPSHOT

* 2017-04-29 - 新增特性：支持可配置是否生成枚举（# [可选] 是否生成枚举，默认为true，generator.enum.enable=true），满足测试框架的需求。 - [张浦] 6becfbc
* 2017-04-15 - 优化实体生成，实体属性增加jsr303的生成 - [张浦] 1372352
* 2017-04-11 - 优化entity和enum的格式；entity不在生成创建时间和修改时间;上传界面说明支持Excel的高版本xlsx - [张浦] 16b63a6
* 2017-03-13 zhangpu 增加配置参数generator.enumName.assemble，默认为false，如果为true时，生成的enum类名称为：entityClassName+propertypeName
* 支持acooly4.x版本支持，要求JDK1.8，增加mybatis的自动代码生成功能。
* 调整JDK日志为单行自动以日志
* 重构为模块化生成，目前支持service(默认)和manage两种模块，后续补充facade,portal和openapi的自动生成。
* 可以直接集成到archetype的test模块，直接调用生成。


### v1.2.3

* 2016-09-23 - 提交mysql和oracle的测试表dm_customer的ddl,增加主配置文件oracle的配置 - [zhangpu] f988688
* 2016-09-23 - 新增ORACLE自动代码生成支持 - [zhangpu] bb2bf4a
* 2016-09-21 - 添加oracle的驱动依赖 - [zhangpu] 9b73dca


### v1.2.2

* 2016-08-15 16:54:29  cuifuq  [add] 列表页面更新：bigint 类型数据自动添加统计求和功能，使用方式 sum="true"   
* 统计功能：sum:求和；avg:平均；max：最大值；min：最小值

> 使用统计功能需acooly-module-security版本升级为3.4.4以上


### v1.2.1

* 2016-07-10 20:37:29  zhangpu  [add] 更新图标和样式为最新的acooly样式,同时支持awesom-font图标和easyui图标，默认采用橙色的awesome-font图标。（需acooly-module-security-3.4.6支持）
* 2016-07-08 02:47:32  zhangpu  [fix] 一系列的试图层模板优化，生成的代码更准确和人性化
* 2016-07-08 01:09:40  zhangpu  [add] 增强选项类型的自动生成规则，同时支持基于枚举(key为字符串)和静态常量(key为数字)的选项数据生成。
* 2016-07-08 01:09:40  zhangpu  [add] 重构数据类型映射，支持配置方式增加数据库与JAVA类型的映射,不如，你想让数据库的decimal类型生成为Money或BigDecimal。
* 2016-07-03 16:13:41  zhangpu  [del] 暂时删除UI(swt)界面,计划后面开发插件。
* 2016-07-03 16:13:41  zhangpu  [add] 精简工具依赖（只依赖freemarker,mysql-jdbc驱动和apache-commons-lang），整理新的发布包,通过nexus发布，完成工具和文档整理。

> 建议使用新版生成器后，升级acooly-module-security版本为3.4.6为最佳效果（向下兼容）

### v1.2.1之前
请按原有的发布包继续使用！~

