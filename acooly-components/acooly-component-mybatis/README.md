## 1. 组件介绍

此组件提供mybatis的sqlSessionFactory，sqlSessionTemplate, mapperScannerConfigurer定义，启用了[mybatis分页插件](https://github.com/abel533/Mapper)


## 2. FAQ

### 2.1 关于mybatis的缓存

mybatis中有一级缓存和二级缓存，一级缓存默认打开(sqlSession中的缓存)，二级缓存默认关闭(应用单独配置，比如用分布式缓存)。

一级缓存会造成一些问题，比如在一个事务中，应用两次调用同样的查询，这两次调用都会使用同一个sqlSession，第二次调用不会访问数据库，直接从sqlSession的缓存中返回结果。

合理使用缓存能提高程序性能，考虑到大多数场景，现把一级缓存关闭。如果需要打开一级缓存，请如下配置：

	acooly.mybatis.settings.localCacheScope=SESSION


### 2.2 使用 Mybatis Common Mapper
 
Mybatis增加单表增删改查通用能力，不用写一行sql语句，单表的操作能力全覆盖。

组件会在系统启动阶段组装`org.apache.ibatis.mapping.SqlSource`。相对于`mybatis generator`，不用手动生成代码，更不用当schema变动时重新生成、合并`Mapper.xml`文件.

在`Mapper.xml`文件中仍然可以自定义语句(`id`不能和`BaseMapper`中的方法同名).

使用例子如下：

#### 2.2.1. 定义DO

	@Getter
	@Setter
	public class City extends AbstractEntity {

		private String name;
	
		private Integer age;
	}
	
上面使用到了jpa annotation添加元数据。非数据库字段请添加`transient`关键字。


#### 2.2.2. 定义Mapper接口

	public interface CityMapper extends EntityMybatisDao<City> {
	}

注意：Mapper接口需要继承`EntityMybatisDao`

#### 2.2.2. 使用mapper

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

#### 2.2.4 More

 相关使用文档参考:https://github.com/abel533/Mapper

 
### 2.3 扩展接口

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
        }