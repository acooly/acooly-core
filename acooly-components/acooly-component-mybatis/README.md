<!-- title: MyBatis组件  -->
<!-- type: infrastructure -->
<!-- author: qiubo,zhangpu -->
<!-- date: 2019-12-30 -->
## 1. 组件介绍

此组件提供mybatis的规范使用和封装，整合mapper扩展支持CRUD自动处理，多条件分页查询自动处理等能力，支持多数据源。

maven坐标：

```xml
<dependency>
	<groupId>com.acooly</groupId>
	<artifactId>acooly-component-mybatis</artifactId>
	<version>${acooly-latest-version}</version>
</dependency>
```

`${acooly-latest-version}`为框架最新版本。

## 2. 特性说明

### 2.1 DAO扫描

默认情况下，根据springboot特性，已经加入了当前应用的根包下的所有dao包的扫描（`${appRootPackage}..dao`）,如果在你当前工程有需要非当前应用根包的mybatis代码和dao，则可显示使用@MapperScan指定扫描dao的包路径。

例如：

1、例如当前应用的根包路径为：com.acooly.demo



## 3. FAQ

### 3.1 关于mybatis的缓存

mybatis中有一级缓存和二级缓存，一级缓存默认打开(sqlSession中的缓存)，二级缓存默认关闭(应用单独配置，比如用分布式缓存)。

一级缓存会造成一些问题，比如在一个事务中，应用两次调用同样的查询，这两次调用都会使用同一个sqlSession，第二次调用不会访问数据库，直接从sqlSession的缓存中返回结果。

合理使用缓存能提高程序性能，考虑到大多数场景，现把一级缓存关闭。如果需要打开一级缓存，请如下配置：

	acooly.mybatis.settings.localCacheScope=SESSION


### 3.2 使用 Mybatis Common Mapper
 
Mybatis增加单表增删改查通用能力，不用写一行sql语句，单表的操作能力全覆盖。

组件会在系统启动阶段组装`org.apache.ibatis.mapping.SqlSource`。相对于`mybatis generator`，不用手动生成代码，更不用当schema变动时重新生成、合并`Mapper.xml`文件.

在`Mapper.xml`文件中仍然可以自定义语句(`id`不能和`BaseMapper`中的方法同名).

使用例子如下：

#### 3.2.1. 定义DO

	@Getter
	@Setter
	public class City extends AbstractEntity {

		private String name;
	
		private Integer age;
	}
	
上面使用到了jpa annotation添加元数据。非数据库字段请添加`transient`关键字。


#### 3.2.2. 定义Mapper接口

	public interface CityMapper extends EntityMybatisDao<City> {
	}

注意：Mapper接口需要继承`EntityMybatisDao`

#### 3.2.2. 使用mapper

`EntityMybatisDao`继承`EntityDao`,除了提供`EntityDao`的能力外，他还可以做更多，单表操作告别sql。


	
	//插入所有属性
	cityMapper.create(city);
	//插入所有非null属性
	cityMapper.insertSelective(city);
	//主键查询
	assertThat(cityMapper.selectByPrimaryKey(city.getId())).isNotNull();
	//主键删除
	cityMapper.deleteByPrimaryKey(city.getId());
	//查询所有
	assertThat(cityMapper.selectAll()).isEmpty();
	
	//通过Example对象组装查询条件
	//SELECT id,name,age FROM city WHERE ( age > ? and id <= ? ) or( name like ? )
	Example example = new Example(City.class);
	example.createCriteria().andGreaterThan("age", 21).andLessThanOrEqualTo("age", 22);
	example.or().andLike("name", "abe");
	assertThat(cityMapper.selectByExample(example)).hasSize(2);
	
	//通过Example对象组装查询条件
	//SELECT id,name,age FROM city WHERE ( name like ? )
	Example example1 = new Example(City.class);
	example1.createCriteria().andLike("name", "ab%");
	assertThat(cityMapper.selectByExample(example1)).hasSize(2);
	
	//通过不为空的属性查询
	City sc1=new City();
	sc1.setAge(22);
	sc1.setName("ac");
	//SELECT id,name,age FROM city WHERE name = ? AND age = ?
	assertThat(cityMapper.select(sc1)).hasSize(1);
	
	//分页查询
	//SELECT id,name,age FROM city WHERE name = ? AND age = ? LIMIT 10
	assertThat(cityMapper.selectByRowBounds(sc1,new RowBounds(0, 10))).hasSize(1);
	
	//分页查询
	//SELECT id,name,age FROM city LIMIT 10
	PageHelper.startPage(1, 10);
	assertThat(cityMapper.selectAll()).hasSize(3);
	
更多能力参考`com.acooly.module.mybatis.EntityMybatisDao`接口。

#### 3.2.4 More

 相关使用文档参考:https://github.com/abel533/Mapper

 
### 3.3 扩展接口

参考showcase

        public interface CityDao extends EntityMybatisDao<City> {
        
            /**
             * 使用mybatis mapper配置文件 src/main/resources/mybatis/CityMapper.xml
             * Mapper文件中的id和方法名匹配
             */
            List<City> findByName(String name);
        
            /**
             * 使用annotation
             * @param state
             * @return
             */
            @Select("SELECT * FROM city WHERE state = #{state}")
            List<City> findByState(String state);
            
            @Delete("DELETE FROM city WHERE name =#{name}")
            void deleteByName(String name);
            
            /**
             * 支持in，参数为集合或者数组
             */
            @Select("select * from city where id in #{ids}")
            List<City> selectByIn(@Param("ids") List<String> ids);
            
            //支持分页
            PageInfo<City1> selectAllByPage1(PageInfo pageInfo);
            mapper写入  <select id="selectAllByPage1" resultType="City1">
                            select * from City1 order by name desc
                        </select>
        }


### 3.4 使用多个数据源

### 3.4.1 配置

    #配置第一个数据源accout
    accout.ds.url=jdbc:mysql://127.0.0.1:3306/accout
    accout.ds.username=root
    accout.ds.password=123456
    #配置第二个数据源trade
    trade.ds.url=jdbc:mysql://192.168.57.22:3306/trade
    trade.ds.username=root
    trade.ds.password=root
    #启用多数据源支持
    acooly.mybatis.supportMultiDataSource=true
    
    #配置accout mybatis
    
    # 数据源前缀
    acooly.mybatis.multi.accout.dsPrefix=accout.ds
    # dao包路径，位于此包下的dao会使用accout数据库
    acooly.mybatis.multi.accout.scanPackage=com.fintech.it.account
    # 配置为主数据库，多个数据源时只能配置一个主数据库
    acooly.mybatis.multi.accout.primary=true
    
    #配置trade mybatis
    acooly.mybatis.multi.trade.dsPrefix=trade.ds
    # dao包路径，位于此包下的dao会使用trade数据库
    acooly.mybatis.multi.trade.scanPackage=com.fintech.it.trade

### 3.4.2 使用注意

1. 主数据源提供的相关beanName为:dataSource、jdbcTemplate、pagedJdbcTemplate、sqlSessionFactory
2. 非主数据源提供的相关beanName为(以上面为例):tradeDataSource、tradeJdbcTemplate、tradePagedJdbcTemplate、tradeSqlSessionFactory
3. 目前不支持进程内的多数据源分布式事务！


### 3.5 关联分页查询

这里举个一对一的列子,`Config`一对一`Customer`

#### 3.5.1 实体类

    package entity;
    @Entity
    @Table(name = "config")
    @Getter
    @Setter
    public class Config extends AbstractEntity {
    	/** 配置项名称 */
    	@NotEmpty
    	@Size(max=128)
        private String configName = "";
    	/** 配置值 */
    	@NotEmpty
    	@Size(max=2048)
        private String configValue;
    	/** 配置描述 */
    	@NotEmpty
    	@Size(max=255)
        private String comments;
    	/** 本地缓存过期时间 */
        private Integer localCacheExpire = 0;
    	/** redis缓存过期时间 */
        private Integer redisCacheExpire = 600000;
    	/** customer_id */
        private Long customerId;
        //一对一关系
        @Transient
        private Customer customer;
    }
    
    package entity;
    @Entity
    @Table(name = "customer")
    @Getter
    @Setter
    public class Customer extends AbstractEntity {
    	/** 名称 */
    	@NotEmpty
    	@Size(max=128)
        private String name = "";
    	/** 年龄 */
    	@NotNull
        private Integer age;
    	/** 地址 */
    	@NotEmpty
    	@Size(max=255)
        private String address;
    }
    
#### 3.5.2 dao

    package dao.CustomerDao;
    public interface ConfigDao extends EntityMybatisDao<Config> {
        //关联查询方法
        PageInfo<Config> queryPage(PageInfo pageInfo, Map<String, Object> map, Map<String, Boolean> sortMap);
    }
    
#### 3.5.3 mapper
 
 CustomerMapper.xml
 
    <mapper namespace="dao.CustomerDao">
        <!--coder自动生成语句-->
        <resultMap id="customerResultMap" type="entity.Customer">
            <id property="id" column="customer_id"/>
            <result property="name" column="customer_name"/>
            <result property="age" column="customer_age"/>
            <result property="address" column="customer_address"/>
            <result property="createTime" column="customer_create_time"/>
            <result property="updateTime" column="customer_update_time"/>
        </resultMap>
        <!--coder自动生成语句-->
        <sql id="customerSqlSelect">
            customer.id as customer_id,
            customer.name as customer_name,
            customer.age as customer_age,
            customer.address as customer_address,
            customer.create_time as customer_create_time,
            customer.update_time as customer_update_time
        </sql>
    
    </mapper>

ConfigMapper.xml

    <mapper namespace="dao.ConfigDao">
        <!--coder自动生成语句-->
        <resultMap id="configResultMap" type="entity.Config">
            <id property="id" column="config_id"/>
            <result property="configName" column="config_config_name"/>
            <result property="configValue" column="config_config_value"/>
            <result property="comments" column="config_comments"/>
            <result property="localCacheExpire" column="config_local_cache_expire"/>
            <result property="redisCacheExpire" column="config_redis_cache_expire"/>
            <result property="createTime" column="config_create_time"/>
            <result property="updateTime" column="config_update_time"/>
            <result property="customerId" column="config_customer_id"/>
            <!--增加关联隐射-->
            <association property="customer"
                         resultMap="dao.CustomerDao.customerResultMap"/>
        </resultMap>
    
        <!--coder自动生成语句-->
        <sql id="configSqlSelect">
            config.id as config_id,
            config.config_name as config_config_name,
            config.config_value as config_config_value,
            config.comments as config_comments,
            config.local_cache_expire as config_local_cache_expire,
            config.redis_cache_expire as config_redis_cache_expire,
            config.create_time as config_create_time,
            config.update_time as config_update_time,
            config.customer_id as config_customer_id
        </sql>
    
         <!--此sql为自动增加查询条件-->
        <select id="queryPage" resultMap="configResultMap">
            select
            <include refid="dao.ConfigDao.configSqlSelect"/>,
            <include refid="dao.CustomerDao.customerSqlSelect"/>
            FROM config
            LEFT JOIN customer ON config.customer_id=customer.id
        </select>
    </mapper>
    
#### 3.5.4 使用

     PageInfo pageInfo = new PageInfo();
     Map<String, Object> map = Maps.newHashMap();
     map.put("EQ_configName", "qiu");
     map.put("EQ_customer.name", "bo");
     Map<String, Boolean> sortMap = Maps.newHashMap();
     sortMap.put("configName", true);
     sortMap.put("customer.name", false);
     PageInfo<Config> configs = configDao.queryPage(pageInfo, map, sortMap);
     
#### 3.5.5 生成sql语句

    select
            config.id as config_id,
            config.config_name as config_config_name,
            config.config_value as config_config_value,
            config.comments as config_comments,
            config.local_cache_expire as config_local_cache_expire,
            config.redis_cache_expire as config_redis_cache_expire,
            config.create_time as config_create_time,
            config.update_time as config_update_time,
            config.customer_id as config_customer_id
         ,
            customer.id as customer_id,
            customer.name as customer_name,
            customer.age as customer_age,
            customer.address as customer_address,
            customer.create_time as customer_create_time,
            customer.update_time as customer_update_time
         
            FROM config
            LEFT JOIN customer ON config.customer_id=customer.id WHERE  config_name  = 'qiu' AND  customer.name  = 'bo'  order by config_name  ASC,customer.name  DESC limit 0,10[]

