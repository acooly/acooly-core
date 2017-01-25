## 快速开始

### 1. 使用`archtype`生成项目

框架提供了`maven archtype`快速生成项目工程。在生成项目之前，我们需要确认如下参数：

* 项目groupId
* 项目artifactId
* 项目version
* web端口

这里举个例子，`groupId=com.acooly.demo`,`artifactId=acooly-demo`,`version=1.0`,`webport=8080`

在命令行中执行如下命令：

 	mvn archetype:generate  -DarchetypeGroupId=com.acooly  -DarchetypeArtifactId=acooly-archetype -DarchetypeVersion=4.0.0-SNAPSHOT -DarchetypeRepository=http://acooly.cn/nexus/content/groups/public/ -DgroupId=com.acooly.demo -DartifactId=acooly-demo -Dversion=1.0 -Dwebport=8080
 	
### 2. 项目结构介绍

`archtype`生成如下目录：

* acooly-demo-assemble

	主要放运行、打包、配置文件
	
* acooly-demo-core

	项目核心代码module
	
* acooly-demo-facade

	存放对外提供dubbo服务时，服务接口代码

* acooly-demo-test

	测试工程。默认有`com.acooly.demo.test.DemoTest`测试用例。
	
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
	
		