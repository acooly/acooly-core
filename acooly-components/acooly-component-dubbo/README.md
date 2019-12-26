<!-- title: dubbo组件 -->
<!-- type: infrastructure -->
<!-- author: qiubo -->

## 1. 组件介绍

此组件给应用dubbo能力

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-dubbo</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

* 此组件已经初始化了dubbo基本配置，如果要配置provider或者consumer，可以通过xml或者java config来配置：
* 此组件支持nacos 注册中心,使用方式如下：

1、请先下载nacos 并启动
```shell script
wget https://github.com/alibaba/nacos/releases/download/1.1.4/nacos-server-1.1.4.zip
unzip nacos-server-1.1.4.zip
bin/startup.sh -m standalone
```
2、开启nacos配置
```shell script
acooly.dubbo.enableNacos=true
acooly.dubbo.nacosUrl=127.0.0.1:8848 //多个地址使用nacos://127.0.0.1:8848?backup=127.0.0.1:8849,127.0.0.1:8850
```


    
### 2.1. java config配置

#### provider

下面的代码暴露了DemoService服务，版本为1.5

	//使用dubbo提供的annotation
	import com.alibaba.dubbo.config.annotation.Service;
	//如果实现类只实现了一个接口，可以不指定interfaceClass，实现多个接口时，请指定要暴露的接口
	@Service(version = "1.5")
	public class DemoServiceImpl implements DemoService {
	
		@Override
		public String echo(String msg) {
			return msg;
		}
	}


#### consumer：

下面的代码使用了UserService服务，版本为1.5

	@Reference(version = "1.5")
	private UserService userService;


## 3. FAQ

### 3.1 获取请求上下文信息

参考`com.acooly.module.dubbo.DubboRequestContext`

上下文信息也可以自动传递。在dubbo服务A内调用其他dubbo服务B时。

请求dubbo服务A的`partnerId`和`gid`会自动传递到调用dubbo服务B。

如果dubbo服务A的请求对象为`BizOrderBase`及其子类,`merchOrderNo`和`bizOrderNo`也会自动传递。

### 3.2 如何设置provider暴露ip

设置启动变量`-Ddubbo.provider.ip=xxx`或设置配置文件。

### 3.3 如何增加自定义扫描服务(@Service @Reference)的路径

添加配置`acooly.dubbo.cumstomConfigPackage=com.acooly.module.security.service`
可自定义增加注解扫描路径，多个路径用用,分割，此路径下会扫描{@link Reference}，{@link Service}这两个注解，默认会扫描{@link Apps#getBasePackage()}路径

### 3.4 如何mock dubbo服务

#### 配置需要被mock的dubbo服务接口

    acooly.dubbo.consumer.mockInterfaces[0]=com.acooly.core.test.dubbo.mock.XXFacade

上面的配置mock掉所有如下字段

      @Reference(version = "1.0")
      private XXFacade xxFacade;

#### 增加mock实现

    @Service
    public class XXFacadeMock implements XXFacade {
        @Override
        public SingleResult<String> echo(SingleOrder<String> msg) {
            return SingleResult.from("mocked");
        }
    }

实现mock接口，并注册到spring容器中。为了避免混淆，mock服务实现类必须已`Mock`为后缀。


### 3.4 忽略Log日志输出

    public interface DemoFacade {
        @DubboLogIgnore
        SingleResult<String> echo1(SingleOrder<String> msg);
    }

实现在接口方法上面增加`@DubboLogIgnore`注解可忽略consumer和provider的日志输出