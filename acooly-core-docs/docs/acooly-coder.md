<!-- title: 代码生成  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2016-10-12 -->
acooly coder自动代码生成工具
====

## 1 简介
acooly coder是为acooly框架配套的专用代码生成工具，设计目的为跟进acooly框架封装的最佳代码实践，快速生成业务程序的骨架代码和基本功能，最大程度的减少程序员的重复劳动及规范统一代码风格和规范~

## 2 工具获取

### 2.1 cli工具

acooly coder的发布包采用maven方式发布，目前只提供cli工具。
仓库地址：http://acooly.cn/nexus/content/repositories/releases/

工具包maven坐标（请根据需要更新对应的版本,当前版本：4.2.0-SNAPSHOT)

### 2.2 工程内集成工具

v4.x坐标：

		<dependency>
	  		<groupId>com.acooly</groupId>
	  		<artifactId>acooly-coder</artifactId>
	  		<version>4.2.0-SNAPSHOT</version>
		</dependency>


使用说明：v4版本已经集成到项目的test中（Acoolycoder.java），可直接main方式运行。

## 3 使用手册

### 3.1 框架约定

acooly框架为了方便开发和设计，以开发经验为基础，对使用acooly进行了部分设计上的约定，通过约定降低设计和开发难度，提高效率。当然，这也符合流行的约定大于配置的设计理念。

**程序结构约定**

工程结构完全按maven j2ee工程骨架，这个可以通过acooly-archetype生成后就可以清楚展现，程序模块的设计开发有一下基本约定。

* 实体类（entity）必须继承AbstractEntity,并提供一个名称为id的属性作为实体的唯一标识，同时也映射到数据库的物理主键。
* 每个模块的程序分为domian（entity和领域domain合并），dao，service和controller四层。命名规则依次为:EntityDao,EntityService,EntityManageController(后台)和EntityPortalController
* 视图层命名约定为：${entityName/domainName}[List].ftl/jsp为列表或主界面, ${entityName/domainName}[Show].ftl/jsp为查看视图，${entityName/domainName}[Edit].ftl/jsp为创建和编辑公用视图。

**数据库设计**

为了方便自动生成结构性代码，对数据库表结构设计进行部分约定，但都符合常规习惯。

* 表名全部小写，不能以数字开头，必须添加备注，表名应该以模块或组件为前缀
* 每个表必须有以id命名的物理主键，且为数字类型，如：mysql为 `id  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID'`, oracle为number。
* 列名称全部小写，不能以数字开头；如果存在多个自然单词的组合，使用下划线分隔(\_)。如："user\_type"
* 列定义必须添加备注 （这个备注则为生成的页面及表单的label）
* 每个表必须添加`create_time`和`update_time`两个日期时间类型的字段

	mysql如下：
	
		create_time timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
		update_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'

* 如果有选项类型的字段，其选项值使用类json格式写入列备注字段，自动生成工具会自动为该列对应的属性和页面生成选项。如：表列为：`user_type` ,备注可以为：`用户类型 {normal:普通,vip:高级}`
* 特别注意，强烈要求选项类（自动生成枚举类的）字段项目全局唯一名称，否则会生成enum名称相同的枚举相互覆盖。
* 特别注意，MySQL在5.5.3之后增加了这个utf8mb4的编码，mb4就是most bytes 4的意思，专门用来兼容四字节的unicode,utf8mb4是utf8的超集。强烈建议表的字符集设置为utf8mb4，在MariaDB情况下utf8会出现字符集不兼容，强烈建议字符集设置为utf8mb4，如：
       
	```sql
	CREATE TABLE `cms_content_body` (
	  `id` bigint(20) NOT NULL COMMENT '主键',
	  `body` text NOT NULL COMMENT '内容主体',
	  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	  PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容主体';
	```


### 3.2 代码生成

自动代码生成的方案与目前流行的方式设计上是基本一致的。主要核心设计为：

* 通过读取数据库的元数据，获得实体对应的表的所有列，类型，长度，可选，备注等信息，通过结构化处理作为自动代码生成的元数据。
* 通过freemarker作为模板引擎，定义一套或多套框架业务代码的最佳实践模板。
* 工具化自动生成目标代码，作为业务开发的start...

目前的工具主要通过配置文件驱动工具行为，主配置文件为`acoolycoder.properties`.

下面以一个案例来简单说明这个开发过程。

#### 3.2.1 设计表

本案例中，我们以mysql为案例设计一个客户信息表，名称为:dm_customer, 表结构如下：

```sql
CREATE TABLE `dm_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `age` int(11) NOT NULL COMMENT '年龄',
  `birthday` datetime NOT NULL COMMENT '生日',
  `gender` int(11) DEFAULT NULL COMMENT '性别 {1:男,2:女,3:人妖}',
  `mail` varchar(64) DEFAULT NULL COMMENT '邮件',
  `mobile_no` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `real_name` varchar(16) DEFAULT NULL COMMENT '姓名',
  `idcard_no` varchar(18) DEFAULT NULL COMMENT '身份证号码',
  `subject` varchar(128) DEFAULT NULL COMMENT '摘要',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 {0:无效,1:有效}',
  `customer_type` varchar(16) NOT NULL COMMENT '客户类型 {normal:普通,vip:重要,sepc:特别}',
  create_time timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `comments` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
```

请将上面的表结构DLL在数据库中执行，生成本次演示的数据表。

> 本案例基于本系列的第一篇文章《acooly框架一:archetype》，假设你已生成了工程，名称为acooly-demo，并建立数据库acooly，然后成功运行的基础上.

#### 3.2.2 配置

打开工具包后，你会看到根目录下有`acoolycoder.properties`文件，请打开进行配置。

application.propertes

```ini
## database connection configurations
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/acooly?useUnicode=true&amp;characterEncoding=UTF-8
jdbc.username=root
jdbc.password=******

## Code generator configurations
# 生成的代码防止的目标java工程根路径
generator.workspace=/Users/zhangpu/workspace/eclipse/acooly-demo
# 生成的模块代码的根包
generator.rootPackage=com.acooly.demo
# 生成的模块的业务根路径
generator.pagePath=/manage/demo
# 表名转换为实体名称时，需要忽略的表的前缀。如:表明为dm_customer,配置该值为dm_ ,则生成的实体名称为Customer
generator.tableToEntityIgnorPrefix=dm_
# 模板方案路径，默认设置为easyui
generator.templatePath=classpath:/template/easyui

# 自定义数据类型映射,类JSON格式。格式{数据库数据类型1:java数据类型1（如果需要imports则需要完整包路径,...}
# 这里设置了映射后，会覆盖生成器默认的数据类型映射规则
generator.dataType.declare={decimal:java.math.BigDecimal}
# 持久化方案(可选: jpa,mybatis)
generator.persistent.solution=mybatis
# 代码作者
generator.code.author=acooly
# 代码版权声明主体
generator.code.copyright=acooly.cn
```

完成配置后，请保持退出，准备运行工具生成代码

#### 3.2.3 生成代码

运行环境基础要求JDK1.8，本工具支持同时生成多张表到同一个模块。

**工程内集成方式**

如果你是通过acooly-archetype或mvna命令方式创建的新工程，在你的工程test模块下有对应的AcoolyCoder.java或acoolycode.properties文件，你可以直接按上面的配置说明调整配置后，Main方式运行AcoolyCoder.java生成代码。

>注意：AcoolyCode.java中代码设置的参数会覆盖acoolycode.properties的配置。


```java
/**
 * 代码生成工具
 */
public class AcoolyCoder {
    public static void main(String[] args) {
        DefaultCodeGenerateService service = (DefaultCodeGenerateService) Generator.getGenerator();
        // 设置生成代码的工程或模块路径
        if (StringUtils.isBlank(service.getGenerateConfiguration().getWorkspace())) {
            String workspace = getProjectPath() + "your-project-module-name";
            service.getGenerateConfiguration().setWorkspace(workspace);
        }
        // 设置生成的代码的包路径
        if (StringUtils.isBlank(service.getGenerateConfiguration().getRootPackage())) {
            service.getGenerateConfiguration().setRootPackage(getRootPackage());
        }
        // 这里设置需要一次性生成到一个包下面的表名，可以参数数组。
        service.generateTable("dm_customer");
    }

    public static String getProjectPath() {
        String file = AcoolyCoder.class.getClassLoader().getResource(".").getFile();
        String testModulePath = file.substring(0, file.indexOf("/target/"));
        String projectPath = testModulePath.substring(0, testModulePath.lastIndexOf("/"));
        return projectPath + "/";
    }

	 // 返回本次代码生成的目标包路径
    private static String getRootPackage() {
        return "com.qiudot.dassets.portal";
    }
}

```



**命令行方式**

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

#### 3.2.4 运行查看效果

OK，如果上步成功，请回到你的IDE及对应的模块，你应该看到在原来空的 src/main/java目录下存在了新生成的代码，并编译通过。同时在src/main/resources/META-INF/resource/jsp/manage/demo（jsp模式）或src/main/resources/templates（freemarker）下存在了该模块的管理界面。


### 3.3 进阶

以上完成了一个acooly框架基础开发的案例，基本展示了acooly半自动代码生产开发模式。但在实际业务开发中这些远还不够，程序员根据实际业务的需求，还需要做很多工作，包括：多实体关联关系配置，业务逻辑服务编写和调用，页面的特殊定制等，但这些本应是程序员的职责，acooly框架提供的是把大量重复的工作自动化和工具化~，后续将逐步发布各种业务开发场景在前端和服务器端的开发说明，大量的组件能力说明~

具体对功能和界面的调整，请参考：

* [开发指南](acooly-guide.html)
* [管理后台开发指南](acooly-guide-boss.html)
* [业务前台开发指南](acooly-guide-portal.html)




