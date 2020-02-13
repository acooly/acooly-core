<!-- title: 数据库连接池组件   -->
<!-- type: infrastructure -->
<!-- author: qiubo -->
<!-- date: 2019-12-31 -->
## 1. 组件介绍

此组件提供druid的数据源

* 对慢查询和大数据集sql打印日志到`sql-10dt.log`文件
* 检查数据库规范执行
* 在开发者模式时，如果发现mysql数据库不存在，自动创建数据库
* 自动创建表

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-ds</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。
	
## 3. FAQ

### 3.1 如何关闭自动创建表

自动创建表特性默认开启，如果线上环境需要关闭，请配置

    acooly.ds.autoCreateTable=false

### 3.2 如何创建多个数据源

此组件会创建一个数据源(使用配置前缀为`acooly.ds`)，我们的应用程序有使用到多个数据源的情况，可以通过此组件创建一个数据源，然后在通过如下的代码创建其他数据源：

        @Bean
        //DruidDataSource的bean name=xxDataSource，读取环境配置前缀yiji.ds1
    	public DruidDataSource xxDataSource() {
            return DruidProperties.buildFromEnv("yiji.ds1");
    	}
    	 @Bean
         //DruidDataSource的bean name=yyDataSource，读取环境配置前缀yiji.ds2
         public DruidDataSource yyDataSource() {
            return DruidProperties.buildFromEnv("yiji.ds2");
         }

### 3.3 组件升级数据库怎么办？

组件在积累的过程中，避免不了修改数据库schema。首先，我们禁止组件删除数据库字段和修改数据库字段名称、类型。组件升级只能新增数据库字段。

比如`api_service`增加了`gid`字段。我们可以在api组件初始化器中加入如下代码：

        setPropertyIfMissing("acooly.ds.dbPatchs.api_service[0].columnName", "gid");//多个字段用逗号隔开
        setPropertyIfMissing("acooly.ds.dbPatchs.api_service[0].patchSql", "ALTER TABLE `api_service` ADD COLUMN `gid` VARCHAR(45) NULL COMMENT '全局唯一id';");

也可以在应用配置文件中加入如下配置:

    acooly.ds.dbPatchs.api_service.columnName=gid
    acooly.ds.dbPatchs.api_service.patchSql=ALTER TABLE `api_service` ADD COLUMN `gid` VARCHAR(45) NULL COMMENT '全局唯一id';

当应用启动过程中，发现`api_service`缺少`gid`字段，应用启动过程中会报错并关闭应用。

    2017-06-30 22:46:10.391 ERROR [DatabasePatchCheckThread1] DBPatchChecker:65-- 表:api_service缺少以下字段：[gid]，请按照提示执行sql语句:
    ALTER TABLE `api_service` ADD COLUMN `gid` VARCHAR(45) NULL COMMENT '全局唯一id';