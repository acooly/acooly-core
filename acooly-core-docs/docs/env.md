## 开发环境

在开始开发之前，我们需要统一环境。

### 1. jdk

使用oracle jdk8最新版本

### 2. maven

使用maven最新版本（3.2以上即可）

请下载并统一使用:[maven\_setting\_acooly.xml](res/maven/maven-settings-acooly.xml)替换`maven`默认setting文件，IDE中的maven插件也请配置选择该配置文件

### 3. IDE

推荐使用`IDEA`，并导入配置：[idea\_settings\_acooly.jar](res/ide/idea/idea_settings_acooly.jar)

>Eclipse建议使用STS插件，但我们最近几年使用的少，帮不了你。

IDEA插件:

* lombok: 框架大量使用了lombok，建议项目开发中也使用，减少代码量。

### 4. 数据库

使用mysql 5.7最新版本,开发过程中，请尽量在本机安装数据库，方便操作。

