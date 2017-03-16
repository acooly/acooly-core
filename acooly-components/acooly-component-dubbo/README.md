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


