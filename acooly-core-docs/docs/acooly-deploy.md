<!-- title: 生产环境与部署  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-03-01 -->
Acooly生产环境与部署
====

本文介绍acooly开发的项目工程的环境准备和部署方案说明。可选的，我们可选择自建服务器或云服务器（阿里云为例）介绍不同环境的典型生产环境准备和部署方案。

>该方案采用服务器直接部署方案，但容器化部署同时也是支持的。

## 1.环境准备

### 1.1 环境规划

* **阿里云服务**：如果是阿里云的环境，关于负载均衡，故障转移，缓存服务，高可用数据库等服务，请直接购买阿里云产品解决。包括：ECS,SLB,REDIS,RDS等，相关的购买，使用和配置请参考阿里云文档或服务商。
* **自建服务器**：自建服务器包括完全自建机房或IDC租用等方式，目前该方案的应用除了大型企业或机构，一般在没有安全要求的情况下，基于成本已很少选择。如果是该方案，需自己完成负载均衡，故障转移，缓存服务，高可用数据库等的安装和配置。

本文以单服务应用，保障安全可靠性的情况下，最小化的环境规划和配置。

|服务类型|服务标志|建议规格|数量|备注
|------|-------|-------|---|---
|跳板机/堡垒机|ECS|1核4G|CentOS7.x|通过ssh方式连接跳板机转内部服务运维访问；或则使用jumpServer之类的堡垒机。
|服务器 |ECS| 2核8G/CentOS7.x|2|双节点负载均衡和故障转移，如果是测试环境，单机也可以
|数据库|RDS| 4核8G及以上/MySQL5.7+|1| MySQL双机集群
|缓存 |REDIS	|1G|1|可购买云服务或自建redis服务
|共享存储| NFS| 50G |1| 可购买云服务或自建NFS服务
|负载均衡|SLB|5-50M公网IP|1|也可自建nginx配置负载均衡

### 1.2 基础服务

除了服务运行需要的服务外，服务的在线发布还需要DI相关的两个基础服务。请保障以下两个服务，在服务运行环境可被正常访问（网络和权限）。

#### 1.2.1 gitlab代码仓库

存放项目源代码和环境配置，发布时，服务器通过脚本动态拉取源代码编译发布。如果目标环境有gitlab仓库，则直接使用，也可以使用gitee或acooly的代码仓库。

#### 1.2.2 nexus的库仓库

可采用目标环境的nexus仓库或acooly的仓库。

如果采用目标环境自建的nexus仓库，可使用脚本`install.nexus.sh`进行配置安装。脚本自动安装后，可通过默认地址和账号访问。

* **Nexus2:** 脚本自动安装的是nexus2。默认端口8081，初始管理账号：admin/admin123
* **Nexus3:** 如果你自行安装nexus3的请参考：[Nexus3 初始密码不再是admin123](https://www.jianshu.com/p/fcb128e34c87)

安装完成后，同时需要在其内部配置acooly仓库的host代理仓库，以便访问acooly相关的基础工具库。具体操作如下：

1. 以管理员身份登录你的nexus服务，点击左侧的Repositories菜单，进入仓库列表。
2. 在Repositories管理界面的顶部，点击`Add`按钮，选择`Proxy Repository`。
3. 在弹出的`New Proxy Repository`中根据提示填写以下表单内容，然后点击`save`按钮保存
	4. Repository ID：acooly-snapshots
	5. Repository Name：acooly-snapshots
	6. Provider：Maven2
	7. Repository Policy：Snapshot
	8. Remote Storage Location：http://acooly.cn/nexus/content/repositories/snapshots/
	9. Expiration Settings: 所有参数输入：5，表示5分钟
	10. 其他参数：全部默认即可
4. check仓库列表中出现名称为`acooly-snapshots`的`proxy`仓库。
5. 在`Repositories`列表中选择`Public Respositories`记录，在下方的`Configuration`选项卡内，从`Available Repositories`列表中选择`acooly-snapshots`加入到左侧的`Ordered Group Repositories`列表中，保存设置。

请记录你的nexus仓库的拉包地址：http://yourNexusDomain/nexus/content/groups/public/

## 2.服务器配置

环境配置前，需要先完成环境及设备准备，包括所有服务的访问地址，账号密码表。


### 2.1 工具包安装

环境准备好后，我们只针对单台服务的OS环境和部署进行说明。我们的环境以linux（CentOS 6.x - 7.x）为准（非常不建议windows服务器，至于原因，请自行百度和脑补~）

#### 2.1.1 下载安装
请参考：[快速开始](https://acooly.cn/docs/core/acooly-quickstart.html) -> 工具包部分，在生产服务器上下周和安装配置好工具包，也可以直接通过git clone获取。

#### 2.1.2 [推荐] git-clone安装
使用SSH登录应用服务器,先安装git，然后clone工具包

root身份操作：

```sh
# pwd
# /root/
# yum install git
...
# git clone http://gitlab.acooly.cn/acoolys/acooly-script.git
# echo "export PATH=\$PATH:/root/acooly-script/deploy/env/bin" >> /etc/profile
# source /etc/profile
# cp -r acooly-script/deploy/app /opt/
```

#### 2.1.3 配置工具环境变量

* 环境部署工具: ${acooly_script}/deploy/env/bin (设置到PATH环境变量中，方便使用)
* 应用部署工具: ${acooly_script}/deploy/app/bin（不设置PATH，拷贝所有部署命令到/opt/apps下）


### 2.2 应用服务环境安装配置

请使用环境工具包里面的命令直接安装对应的软件和服务即可，下面介绍典型配置。

#### 2.2.1 [必选] 操作系统加固

这个一般是运维的事（如果你有的化...）

1. 如果你是自建非阿里云环境，请先运行yum仓管镜像设置脚本: `install.centosRepos.sh`，建立阿里云的仓库镜像，便于后续软件的安装。

2. 运行工具包里面的`common.sh`进行简单配置包括：机器名，软件需要的公共目录，防火墙配置，系统性能参数配置，ssh配置，常用软件（lrzsz git telnet zip unzip expect iotop）等。该命令有相关提示和说明。

root身份操作：

```sh
# common.sh
...
please enter sshd port(22):
// 这里是调整修改sshd服务的端口，默认22，请直接回车           
sshd port: 
           
disable root login(no)[yes/no]:
// 这里是是否禁用非root用户链接登录ssh，模式不禁用，直接回车
PermitRootLogin:    

please enter a new host name:
// 这里是修改机器名，一般采用app+序号，比如：app1  
new host name: app1
...
```
#### 2.2.2 [必选] JDK和Maven

acooly框架的部署方式为：拉取（脚本或工具）项目源代码 --> 服务器打包 --> 运行 模式。我们需要安装配置JDK和maven环境。请运行：`install.jdk.sh` 和 `install.maven.sh`

> jdk和maven脚本运行后，会自动下载，安装和配置完需要的参数，无需再人工配置。

这里特别注意：如果你签名的nexus使用的是自建的，则需要在maven安装配置完成后，调整下默认的settings.xml的配置。操作如下：

```sh
// 使用vim打开maven的settings.xml配置文件。
# vim /opt/apache-maven-3.6.0/conf/settings.xml
修改<profiles>下所有<url>节点的地址为前面你的nexus的`Public repositories`的访问地址：`http://yourNexusDomain/nexus/content/groups/public/`, 然后保持退出。
```

#### 2.2.3.[可选] 共享磁盘(NFS)

如果你的多节点部署（一般我们建议每个应用至少双节点高可用），你可能需要NFS共享磁盘（低安全性）配置。请运行命令：`install.nfs.sh`.支持两种方式的挂载。

* 阿里云NAS挂载，请运行命令后直接输入nas的域名地址即可
* 共享磁盘挂载，请输入共享持平的局域网地址

>注意：脚本工具会自动创建和挂载到本地目录`/data`

#### 2.2.4.[可选] 建立运行普通用户

如果你不喜欢，习惯用root运行，没有人能强求你的，你跳过就OK。否则，请运行：`install.optuser.sh`，后续的操作请切换到该普通用户。


### 2.2.5 [可选] MySQL和Redis

如果是阿里云，你的RDS，REDIS无需安装，否则，你可以使用对应的安装脚本自动下载合适的版本并安装配置。如果你选择自建安装，推荐使用下面的工具直接运行安装。

* mysql: `install.mysql5.7.centos7.sh`
* redis: `install.redis.sh`

>相关配置，请百度或自行脑补~

>请记住你安装或购买的mysql, redis等服务的账号，你需要提供给开发团队，以便其在工程中配置环境感知配置文件。

### 2.2.6 [可选] zookeeper

如果你是微服务的平台，需要使用dubbo环境，无论是否阿里云，请自行安装zk环境，于是请运行：`install.zk.sh`


#### 2.2.7.[可选] 节点复制

应用节点复制：`clone.sh`,基于SCP拷贝相关的软件和配置到另外的节点，节省公网带宽消耗和提高安装配置效率。
zookeeper节点复制：`install.zk.clone.sh`

>不熟悉的情况下，建议谨慎使用。

### 2.3 环境参数

当你完成所有的安装和配置工作后，请收集一下服务相关的参数，一并提交给开发团队进行参数配置。

|服务|地址|账号|备注
|---|---|---|---
|mysql|内网访问地址？|用户名/密码|
|redis|内网访问地址？|密码|如果是阿里云服务，则默认无密码
|nfs|挂载本地路径||采用脚本方式挂载则默认是`/data`


### 2.5.应用部署

* 程序目录：我们把所有的项目程序都部署到：/opt/app，支持多个项目共同运行，但请规划好内存和资源。该目录已在前面脚本中创建。
* 部署工具：为方便操作，请拷贝\${acooly-script}/deploy/app下所有文件到 /opt/app下。
* 拉取代码：每个项目部署时都需要先手动的完整拉取一次代码。请在/opt/app下运营git clone方式拉取。

	```bash
	cd /opt/app
	git clone http://.../${projectName}.git
	```
* 启动服务：在/opt/app下运行 `./start.sh ${projectName}` , 命令会自动更新代码，打包和启动

	```sh
	# ./start.sh
	start.sh appName [appOption] [appProfile] [appHeapSize]
	1.appName:------[必选] 当前被部署的工程的名称，就是拉取代码后的目录名称
	2.appOption:----[可选] 启动选项，(默认)normal: 正常启动；quick:快速启动，不拉取和更新代码
	3.appProfile:---[可选] 环境感知，默认: online
	4.appHeapSize:--[可选] Java堆大小，单元M，默认：1024M  
	```

* 其他命令：`stop.sh`,`dump.sh`,`restart.sh`,`quickstart.sh` 具体应用场景，请参考：[工具包文档](https://gitlab.acooly.cn/acoolys/acooly-script/blob/master/README.md)
	
日志目录：/var/log/webapps/${projectName}/

如果启动没有异常，最后会显示本地访问地址，你可以在内外环境下直接访问该地址验证。

例如：

```
[main] TomcatWebServer-:- Tomcat started on port(s): 8088 (http) with context path ''
2020-04-24 12:25:21.840 INFO  [main] Main-:- Started Main in 30.344 seconds (JVM running for 31.396)
2020-04-24 12:25:21.841 INFO  [main] AcoolyApplicationRunListener-:- 启动成功: http://127.0.0.1:8088 
```

你可以通过curl命令测试访问：

```sh
curl # curl http://127.0.0.1:8088/healthCheck
ok
```
如果你没有打算配置nginx反向代理或LBS，你可以直接开放应用对应的端口公网访问访问权限，直接使用IP+端口访问访问。


