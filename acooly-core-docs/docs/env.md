<!-- title: 开发环境  -->
<!-- type: core -->
<!-- author: zhangpu -->

Acooly框架建议开发人员统一环境和工具，以提高效率和协作便利性。请开发人员根据各自的操作系统，进行对应的环境配置。

## 1. jdk

使用oracle jdk8以上，建议JDK8最新版本。

## 2. maven

使用maven最新版本（3.2以上即可）

请下载并统一使用: [maven\_setting\_acooly.xml](res/maven/maven-settings-acooly.xml) 替换`maven`默认setting文件，IDE中的maven插件也请配置选择该配置文件

## 3. IDE

推荐使用`IDEA`，并导入配置：[idea\_settings\_acooly.jar](res/ide/idea/idea_settings_acooly.jar)

>Eclipse建议使用STS插件，但我们最近几年使用的少，帮不了你。

IDEA推荐插件:

* lombok: 框架大量使用了lombok，建议项目开发中也使用，减少代码量。
* Alibaba Java Coding Guidelines: 阿里巴巴代码规范插件，统一代码格式和风格（format）

>直接IDEA的插件安装页搜索名称即可找到，然后安装。

## 4. 数据库

使用mysql 5.7最新版本,开发过程中，请尽量在本机安装数据库，方便操作。

