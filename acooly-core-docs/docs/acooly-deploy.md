<!-- title: 生产环境与部署  -->
<!-- type: core -->
<!-- author: zhangpu -->
<!-- date: 2019-03-01 -->
Acooly生产环境与部署
====

本文介绍acooly开发的项目工程的生产环境准备和部署方案说明。可选的，我们可选择自建服务器或云服务器（阿里云为例）介绍不同环境的典型生产环境准备和部署方案。


## 1.生产环境

### 1.1.环境选择

#### 阿里云服务

如果是阿里云的环境，关于负载均衡，故障转移，缓存服务，高可用数据库等服务，请直接购买阿里云产品解决。包括：ECS,SLB,REDIS,RDS等，相关的购买，使用和配置请参考阿里云文档或服务商。

#### 自建服务器

自建服务器包括完全自建机房或IDC租用等方式，目前该方案的应用除了大型企业或机构，一般在没有安全要求的情况下，基于成本已很少选择。如果是该方案，需自己完成负载均衡，故障转移，缓存服务，高可用数据库等的安装和配置。

### 1.2.工具包安装

环境准备好后，我们只针对单台服务的OS环境和部署进行说明。我们的生产环境易linux（CentOS 6.x - 7.x）为准（非常不建议windows服务器，至于原因，请自行百度和脑补~）

请参考：[快速开始](quickstart.html) -> 工具包部分，在生产服务器上下周和安装配置好工具包，这里需要设置：PATH=\$PATH:\${acooly\_script}/deploy/env/bin:${acooly\_script}/deploy/app/bin

> 建议设置到：/etc/profile --> PATH=\$PATH:\${acooly\_script}/deploy/env/bin:\${acooly\_script}/deploy/app/bin

* 环境工具包：${acooly_script}/deploy/env/bin
* 部署工具包：${acooly_script}/deploy/app/bin

## 2.部署

### 2.1.OS配置

请使用环境工具包里面的命令直接安装对应的软件和服务即可，下面介绍典型配置。

#### 2.1.1.[必选] 操作系统加固

这个一般是运维的事（如果你有的化...）

1. 如果你是自建非阿里云环境，请先运行yum仓管镜像设置脚本: `install.centosRepos.sh`，建立阿里云的仓库镜像，便于后续软件的安装。

2. 运行工具包里面的`common.sh`进行简单配置包括：机器名，软件需要的公共目录，防火墙配置，系统性能参数配置，ssh配置，常用软件（lrzsz git telnet zip unzip expect iotop）等。该命令有相关提示和说明。


#### 2.1.2.[可选] 服务安装配置

如果是阿里云，你的RDS，REDIS和SLB都有了，无需安装，否则，你可以使用对应的安装脚本自动下载合适的版本并安装配置。

* mysql: `install.mysql5.7.centos7.sh`
* redis: `install.redis.sh`
* nginx: `install.nexus.sh`

>相关配置，请百度或自行脑补~

如果你是微服务的平台，需要使用dubbo环境，无论是否阿里云，请自行安装zk环境，于是请运行：`install.zk.sh`

#### 2.1.3.[必选] 运行环境

acooly框架的部署方式为：拉取（脚本或工具）项目源代码 --> 服务器打包 --> 运行 模式。我们需要安装配置JDK和maven环境。请运行：`install.jdk.sh` 和 `install.maven.sh`

> jdk和maven脚本运行后，会自动下载，安装和配置完需要的参数，无需再人工配置。

#### 2.1.4.[可选] 建立运行普通用户

如果你不喜欢，习惯用root运行，没有人能强求你的，你跳过就OK。否则，请运行：`install.optuser.sh`，后续的操作请切换到该普通用户。

#### 2.1.5.[可选] 共享磁盘(NFS)

如果你的多节点部署（一般我们建议每个应用至少），你可能需要NFS共享磁盘配置。请运行命令：`install.nfs.sh`.支持两种方式的挂载。

* 阿里云NAS挂载，请运行命令后直接输入nas的域名地址即可
* 共享磁盘挂载，请输入共享持平的局域网地址


### 2.2.部署工程

* 程序目录：我们把所有的项目程序都部署到：/opt/app，支持多个项目共同运行，但请规划好内存和资源。该目录已在前面脚本中创建。
* 部署工具：为方便操作，请拷贝\${acooly-script}/deploy/app下所有文件到 /opt/app下。
* 拉取代码：每个项目部署时都需要先手动的完整拉取一次代码。请在/opt/app下运营git clone方式拉取。

	```bash
	cd /opt/app
	git clone http://.../${projectName}.git
	```
* 启动服务：在/opt/app下运行 `./start.sh ${projectName}` , 命令会自动更新代码，打包和启动
* 其他命令：`stop.sh`,`dump.sh`,`restart.sh`,`quickstart.sh` 具体应用场景，请参考：[工具包文档](https://gitlab.acooly.cn/acoolys/acooly-script/blob/master/README.md)
	
日志目录：/var/log/webapps/${projectName}/

### 2.3.节点复制

应用节点复制：`clone.sh`
zookeeper节点复制：`install.zk.clone.sh`
