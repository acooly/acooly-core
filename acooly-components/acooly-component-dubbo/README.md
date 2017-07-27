## 1. 组件介绍

此组件给应用dubbo能力

## 2. 使用说明

此组件已经初始化了dubbo基本配置，如果要配置provider或者consumer，可以通过xml或者java config来配置：
    
### 3.1 java config配置

#### 3.1.1 provider

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


#### 3.1.2 consumer：

下面的代码使用了UserService服务，版本为1.5

	@Reference(version = "1.5")
	private UserService userService;


### 4. FAQ

#### 4.1 获取请求上下文信息

参考`com.acooly.module.dubbo.DubboRequestContext`

#### 4.2 如何设置provider暴露ip

设置启动变量`-Ddubbo.provider.ip=xxx`或设置配置文件。

#### 4.3 如何增加自定义扫描服务(@Service @Reference)的路径

添加配置`acooly.dubbo.cumstomConfigPackage=com.acooly.module.security.service`
可自定义增加注解扫描路径，多个路径用用,分割，此路径下会扫描{@link Reference}，{@link Service}这两个注解，默认会扫描{@link Apps#getBasePackage()}路径