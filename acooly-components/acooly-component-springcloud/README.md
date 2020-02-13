<!-- title: SpringCloud组件 -->
<!-- type: infrastructure -->
<!-- author: aleishus -->
<!-- date: 2019-12-18 -->

## 1. 组件介绍

此组件提供调用 SpringCloud Restful服务的能力。注意，注册中心必须使用`nacos`，不支持`eureka`

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-springcloud</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

此组件默认支持Ribbon,OpenFeign和RestTemplate,并且支持http客户端连接池，详细配置见下方。此外为了防止重复调用且未做幂等性问题，默认关闭重试机制，即`failfast`。


    
### 2.1 使用前提

请先下载nacos 并启动

```shell
wget https://github.com/alibaba/nacos/releases/download/1.1.4/nacos-server-1.1.4.zip
unzip nacos-server-1.1.4.zip
bin/startup.sh -m standalone
```

访问地址 http://127.0.0.1:8848/nacos  默认用户名/密码 nacos/nacos

更多详细的配置请参考[nacos官网](https://nacos.io/)

### 2.2 例子

#### 服务端提供Restful服务接口

```java
@RestController
@RequestMapping("v1")
public class TestController {

 
    @RequestMapping(value = "getFoo", method = RequestMethod.GET)
    public Foo foo(@RequestParam("name")  String name ) {
        Foo foo = new Foo();
        foo.setName(name);
        foo.setAge(21);
        foo.setHobby("计算机");
        Map<String, String> attach = new HashMap<>(1);
        attach.put("from", environment.getProperty("spring.application.name")); //name is AppTest1
        attach.put("source", "spring cloud");
        attach.put("port", environment.getProperty("server.port"));
        foo.setAttachment(attach);
        return foo;
    }
    
}
```
#### 服务端提供Feign Client的facade包

```java
@FeignClient(value = "AppTest1")
@RequestMapping("v1")
public interface FooService {

    @RequestMapping(value = "getFoo", method = RequestMethod.GET)
    Foo getFoo(@RequestParam("name") String name );
}

```

#### 调用端使用
1、首先引入服务端提供facade 依赖

2、在`main`启动方法类中使用注解 `@EnableFeignClients(basePackages = {"xx.xx"})` xx.xx 是需要扫描facade 路径

3、然后使用依赖注入调用即可

```java
@Resource
private FooService fooService
```

### 2.3 一些关键的配置
* `feign.httpclient.maxConnections` 连接池大小 默认 `200`
* `feign.httpclient.maxConnectionsPerRoute` 每个地址最多能用多少个连接 默认 `50`
* `spring.cloud.nacos.discovery.serverAddr` nacos注册中心地址默认 `127.0.0.1:8848` nacos集群多地址请使用`,`隔开
* `feign.client.config.defult.loggerLevel` feign 日志级别如下 ：默认 `NONE`
  * `BASIC`, 只记录请求方法和URL以及响应状态代码和执行时间。
  * `HEADERS`, 记录请求和应答的头的基本信息。
  * `FULL`, 记录请求和响应的头信息，正文和元数据。

* 其他详细配置请参考`spring.cloud.nacos.*` 和 spring cloud 组件配置文档
    * [Ribbon](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-ribbon.html)
    * [Feign](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html) 

## 3. FAQ
### 可使用RestTemplate 方式做测试调用（已经集成负载均衡）

```java
    @Resource
    private RestTemplate template;

    @Test
    public void test() {
        template.getForObject("http://AppTest1/v1/getFoo?name={1}", Foo.class, "212");
    }

```

