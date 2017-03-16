## 1. 组件介绍

此组件提供druid的数据源

* 对慢查询和大数据集sql打印日志到`sql-10dt.log`文件
* 检查数据库规范执行

	
## 2. FAQ

### 2.1 如何创建多个数据源

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
