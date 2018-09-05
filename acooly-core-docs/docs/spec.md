## 规范

根据开发指南要求开发代码，并参考如下规范

###  代码格式

统一使用idea内置`code style`

### 代码规范

采用阿里巴巴代码规范，相关资源如下: 

* [阿里巴巴JAVA开发手册](https://github.com/alibaba/p3c/blob/master/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C%EF%BC%88%E8%AF%A6%E5%B0%BD%E7%89%88%EF%BC%89.pdf)
* [IDEA插件使用文档](https://github.com/alibaba/p3c/wiki/IDEA%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3)

### 请求对象

对于参数比较多的情况，建议自定义OrderBase子类

* 查询类请求继承OrderBase
* 命令类请求继承BizOrderBase

### 关于响应对象的使用规范

1. 	`ResultBase`中已经定义了`status`、`code`、`detail`。所有子类去掉其他`code`和`message`.
2. 查询类请求，如果查询对象不存在，返回`status== success`，业务调用方需判断dto是否为空.
3. 命令类请求，且为异步场景。当业务完全执行完毕后`status==success`。任何中间状态都为`status==processing`.

### 枚举继承`Messageable`

### 关于响应码`ResultCode`


openapi对外提供服务，对于异常的情况，我们需要返回明确的`ResultCode`。响应码应该为枚举类型，并且实现`Messageable`接口

#### 5.1. 异常处理

在抛出异常时，需要设置`ResultCode`,`BusinessException`构造器支持下面几种方式设置`ResultCode`：

	BusinessException#BusinessException(Messageable)
	BusinessException#BusinessException(Messageable, String)
	BusinessException#BusinessException(Messageable, Throwable)

#### 5.2. 公共响应码

框架提供了常用的响应码`com.acooly.core.common.facade.ResultCode`,没有特殊需求时，可以使用公共响应码。

#### 5.3. 应用响应码

应用响应码放在`facade`模块中，名字为`应用名+ResultCode`，比如会员系统为`CustomerResultCode`，交易系统为`TradeResultCode`.响应码枚举类型不需要加应用名前缀，根据业务实际情况定义。

参考如下定义规则：

	 #枚举必须大写
	 #枚举name和code一样
	 #多个单词用_分隔
	 SUCCESS("SUCCESS", "成功"),
	 INTERNAL_ERROR("INTERNAL_ERROR", "内部错误"),

