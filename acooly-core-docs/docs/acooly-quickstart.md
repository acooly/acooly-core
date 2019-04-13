<!-- title: 快速开始  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-04-29 -->

本文以step-by-step的方式，引导初次接触者快速体验acooly框架，为方便演示，本文档采用windows(大多开发环境)环境进行演示说明，mac用户一般比较资深，类通...

### 1. 准备

acooly框架的基础开发环境需要：

* jdk1.8
* maven3.x
* IDE（推荐IDEA）
* mysql

如果你的环境不满足以上基础条件，请先安装和配置。具体环境准备请参考："环境准备"


### 2. 工具包

acooly框架提供了一些方便快捷的开发工具脚本，请在开始前，下载工具包，并解压即可使用，为方便，你可以再你的操作系统设置对PATH应环境变量。

下载地址：[acooly-script](http://acooly.cn/nexus/service/local/repositories/releases/content/com/acooly/acooly-script/4.2.0/acooly-script-4.2.0-dev.tar.gz)

工具包为cli方式，下载后，直接解压及可用。工具包里面提供的工具请参考解压后根目录的README.md文件，或跟随下面的说明来熟悉。

```sh
# 如果你是windows，随便解压都可以，解压后目录为：acooly-script-4.2.0
wget http://acooly.cn/nexus/service/local/repositories/releases/content/com/acooly/acooly-script/4.2.0/acooly-script-4.2.0-dev.tar.gz
cd acooly-script-4.2.0
```

>最新版也可直接从：https://gitlab.acooly.cn/acoolys/acooly-script/ 拉取

我们假设你的解压后目录为：/Users/user/software/acooly-script-4.2.0 或则 D:\tools\acooly-script-4.2.0, 我们以变量${acooly_script}作为代替，你可以设置：${acooly_script}/dev/bin到你的PATH环境变量中，方便后续使用。

### 3. 快速体验

工具包为你直接提供了quickstart的命令工具，你可以免去groupid等参数的设置，直接创建默认quickstart工程，快速体验acooly框架。

假设你的worksapce为：d:\workspace, 你已完成上面第2部的工具包的安装和配置（环境变量），请启动cmd进行操作：

```bat
d:
cd d:\workspace
quickstart.bat
```

如果你已安装了JDK1.8,MAVEN3.X和mysql，根据提示完成操作后，基于SpringBoot的项目会成功启动，请在浏览器输入：http://localhost:8080 访问


### 4. 手动体验

手动体验是提供one by one的方式，从工程创建，配置，添加组件，运行，测试等步骤体验acooly框架的开发过程。

#### 4.1 创建工程

框架提供了`maven archtype`快速生成项目工程。在生成项目之前，我们需要确认如下参数：

     mvn archetype:generate  -DarchetypeGroupId=com.acooly  -DarchetypeArtifactId=acooly-archetype -DarchetypeVersion=4.2.0-SNAPSHOT -DarchetypeRepository=http://acooly.cn/nexus/content/groups/public/ -DgroupId=com.acooly.demo -DartifactId=acooly-demo -Dversion=1.0 -Dwebport=8080 -DmysqlHost=127.0.0.1 -DmysqUserName=root -DmysqlPassword=123456
    
* 项目groupId
* 项目artifactId
* 项目version
* web端口 (webport参数)
* mysql数据库host (mysqlHost参数)
* mysql用户名 (mysqUserName参数)
* mysql密码 (mysqlPassword参数)

为兼容不同IDE（IDEA和Eclipse），我们提供独立的cli脚本方式创建新工程。在前面我们下载了工具包。我们可以直接使用脚本工具mvna构建工程。

* mac: ${acooly_script}/dev/bin/mvna 
* win: ${acooly_script}/dev/bin/mvna.bat

如果你设置了环境变量，请直接在你的的workspace的路径下的命令行窗口直接运行mvna命令，命令会检查你的环境和引导你完成工程创建。

```bat
cd D:\workspace
mvna
请根据提示完成工程参数操作，成功完成后，会打印后续的操作说明
工程创建成功后：
1、确定你已经安装了mysql数据库
2、请创建一个空的mysql数据库（scheme，假定数据库名称为：acooly-quickstart）
3、修改配置文件的数据链接信息：acooly-quickstart/acooly-quickstart-assemble/src/main/resource/application.properties
   acooly.ds.url=jdbc:mysql://127.0.0.1:3306/acooly-quickstart
   acooly.ds.username={dbname}
   acooly.ds.password={dbpswd}
4、你可以使用IDE或mvn命令方式启动或打包运行...
```

 	
#### 4.1 工程结构介绍

`archtype`生成如下目录：

* acooly-demo-assemble

	主要放运行、打包、配置文件
	
* acooly-demo-core

	项目核心代码module
	
* acooly-demo-facade

	存放对外提供dubbo服务时，服务接口代码

* acooly-demo-test

	测试工程。默认有`com.acooly.demo.test.DemoTest`测试用例和代码生成工具`com.acooly.demo.test.AcoolyCoder`。
	
#### 4.3 启动项目

1. 修改配置文件

	配置文件`acooly-demo-assemble/src/main/resources/application.properties`
	
2. 启动项目

	运行主类:`com.acooly.demo.Main`,当项目在IDE中运行时，会判断redis是否启动，没有启动会启动内置redis
	
3. 确认
	
	启动完成后，在浏览器中打开`http://127.0.0.1:8080/hello`，输出`hello world`。
	
	也可以执行`com.acooly.demo.test.DemoTest`回归测试用例。
	
#### 4.4 添加组件

`security`组件提供了基于角色的权限管理框架和安全相关的服务。

1. 添加依赖

	在`acooly-demo-core`项目中`pom.xml`文件加入依赖：
	
		 <dependency>
            <groupId>com.acooly</groupId>
            <artifactId>acooly-component-security</artifactId>
        </dependency>
        
2. maven重新导入
3. 重启项目
4. 访问

	浏览器打开`http://127.0.0.1:8080`，输入用户名`admin`,密码`111111`,就可以登录后台主界面。
	
>如需添加其他组件，请参考组件列表	
	
### 4.5 生成代码
		
测试工程中有`AcoolyCoder`，它读取`acoolycoder.properties`配置文件，生成代码。

>请参考：[acooly coder自动代码生成工具](acooly-coder.html)

### 4.6 配置和资源文件

1. 配置文件

    主配置文件

        src/main/resources/application.properties


    环境配置文件：

        src/main/resources/application-${env}.properties

    公共属性可以放在主配置文件中，仅环境相关的配置放在环境配置文件中。

2. 静态资源文件存放路径

        src/main/resources/static

    静态资源文件必须存放在上面的路径

3. 模板文件存放路径

        src/main/resources/templates

    框架提供freemarker集成，使用freemarker模板放在此路径

4. jsp存放路径

        src/main/resources/META-INF/resources/WEB-INF/jsp

   框架提供jsp集成，使用jsp放在此路径

5. 数据库脚本初始化

    对于组件开发者，请实现`StandardDatabaseScriptIniter`,参考代码`com.acooly.module.sms.SmsAutoConfig#smsScriptIniter`，脚本路径如下：

        src/main/resources/META-INF/database/${componentName}/${dbType}

    对于应用开发者，框架支持自动执行初始化脚本，脚本路径如下：

        src/main/resources/META-INF/database/${dbType}/dml.sql
        src/main/resources/META-INF/database/${dbType}/dml_*.sql
        src/main/resources/META-INF/database/${dbType}/ddl.sql
        src/main/resources/META-INF/database/${dbType}/ddl_*.sql



    框架发现数据库文件存在，会依次执行这两个文件。(此逻辑在jpa初始化完成后才执行，所以我们可以依赖jpa`@Entity`创建表，然后初始化数据)。

### 4.7 包扫描规则

1. `Main`类所在的包(basePackage)会被扫描到spring 容器
2. 应用实体扫描包：${basePackage}.**.entity
3. 组件实体扫描包: com.acooly.module.**.entity
4. 应用数据访问层包扫描路径：${basePackage}.**.dao
5. 组件数据访问层包扫描路径：com.acooly.module.**.dao
