## 开发环境初始化

在开始开发之前，我们需要统一环境：

### 1. jdk

使用oracle jdk8最新版本

### 2. maven

使用maven最新版本

### 3. nexus

* 开发环境统一使用nexus地址为：`http://192.168.45.35:8081/nexus`
* 驻场开发时，使用nexus地址为: `http://nexus.yijifu.net/nexus/`,此nexus不对外提供源代码访问。

请使用[maven setting](maven/settings.xml)替换`maven`默认setting文件

### 4. IDE

推荐使用`IDEA`、[sts](https://spring.io/tools/sts)

配置[codetemplates](ide/acooly-eclipse-codetemplates.xml),[formatter](ide/acooly-eclipse-formatter.xml).

不要使用eclipse！！！

### 5. lombok

少写无意义的代码会长寿！

### 6. 数据库

使用mysql 5.7最新版本

### 7. jenkins

192.168.46.18:8888

账号:admin 密码:admin123


### 8. sonar

192.168.46.18:9000

## 资源清单

* 开发服务器：

		
		

* 测试服务器：


		
* 开发数据库服务器:


