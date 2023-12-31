<!-- title: 分布式缓存组件 -->
<!-- type: infrastructure -->
<!-- author: qiubo -->
<!-- date: 2019-12-21 -->
## 1. 组件介绍

此组件提供分布式缓存的能力

对于spring 声明式缓存，加入如下能力：

1. 根据codis特性优化性能
2. 响应结果为null时不缓存数据，需要缓存null时，建议用空对象代替(我们的使用场景很少会对结果为null时缓存)
3. 当缓存操作异常时不抛出异常
4. 缓存key格式为:cacheName+":"+param,(cacheName为@Cacheable上的cacheName参数)

配置命名空间`spring.redis`

## 2. 使用

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-cache</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。
         
### 2.1 基于spring注解使用缓存能力

    // 先从缓存取 user 
    @Cacheable(value = "cacheName",key = "#id")   
     public Object findUser( long id ) {    
       return user;    
     }    

    // 更新缓存 user    
    @CachePut(value = "cacheName" ,key = "#user.id")        
    public Object updateUser(Object user){     
      return  user ;      
    }      

    // 从缓存中删除 user 的key   
    @CacheEvict(value = "cachaName" ,key = "#id")   
    public void deleteUser(long id){    
      return ;   
    }

### 2.2 基于redisTemplate使用底层缓存存取    

    // 获取redisTemplate bean实例 
    @Autowired
    private RedisTemplate redisTemplate;
    
    // 对简单的 key-value 的封装
    ValueOperations va = redisTemplate.opsForValue() ;
    va.set("test_key","test_value");
    Object cachValue = va.get("test_key");
    // 对hash表操作的封装 
    HashOperations hash =  redisTemplate.opsForHash() ;
    // 对链表操作的封装 
    ListOperations list = redisTemplate.opsForList() ;
    // 对集合操作的封装 
    SetOperations set = redisTemplate.opsForSet() 

### 2.3 使用redis限速服务

     @Autowired
     private RateChecker rateChecker;
     
     Integer interval=1000;//一秒
     Long maxRequests=100L;//最大请求数100
     rateChecker.check("key",1000,100);