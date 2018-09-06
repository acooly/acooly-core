## 快速开始

开始之前，您可以把[acooly-showcase](http://gitlab.yiji/fintech/acooly-showcase)代码下载下来，在本地启动试试，
然后按照下面的步骤one by one...


### 1. 使用`archtype`生成项目

框架提供了`maven archtype`快速生成项目工程。在生成项目之前，我们需要确认如下参数：

* 项目groupId
* 项目artifactId
* 项目version
* web端口

这里举个例子，`groupId=com.acooly.demo`,`artifactId=acooly-demo`,`version=1.0`,`webport=8080`

在命令行中执行如下命令：

 	mvn archetype:generate  -DarchetypeGroupId=com.acooly  -DarchetypeArtifactId=acooly-archetype -DarchetypeVersion=4.2.0-SNAPSHOT -DarchetypeRepository=http://acooly.cn/nexus/content/groups/public/ -DgroupId=com.acooly.demo -DartifactId=acooly-demo -Dversion=1.0 -Dwebport=8080
 	
> 如果命令行在："Generating project in Interactive mode"卡住，请加入：-DarchetypeCatalog=local

 	
### 2. 项目结构介绍

`archtype`生成如下目录：

* acooly-demo-assemble

	主要放运行、打包、配置文件
	
* acooly-demo-core

	项目核心代码module
	
* acooly-demo-facade

	存放对外提供dubbo服务时，服务接口代码

* acooly-demo-test

	测试工程。默认有`com.acooly.demo.test.DemoTest`测试用例和代码生成工具`com.acooly.demo.test.AcoolyCoder`。
	
### 3. 启动项目

1. 修改配置文件

	配置文件`acooly-demo-assemble/src/main/resources/application.properties`
	
2. 启动项目

	运行主类:`com.acooly.demo.Main`,当项目在IDE中运行时，会判断redis是否启动，没有启动会启动内置redis
	
3. 确认
	
	启动完成后，在浏览器中打开`http://127.0.0.1:8080/hello`，输出`hello world`。
	
	也可以执行`com.acooly.demo.test.DemoTest`回归测试用例。
	
### 4. 添加`security`组件

`security`组件提供了安全相关的服务和应用管理后台。

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
	
### 5. 使用代码生成工具
		
测试工程中有`AcoolyCoder`，它读取`acoolycoder.properties`配置文件，生成代码。

### 6. 资源文件存放路径

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

### 7. 包扫描

1. `Main`类所在的包(basePackage)会被扫描到spring 容器
2. 应用实体扫描包：${basePackage}.**.entity
3. 组件实体扫描包: com.acooly.module.**.entity
4. 应用数据访问层包扫描路径：${basePackage}.**.dao
5. 组件数据访问层包扫描路径：com.acooly.module.**.dao
