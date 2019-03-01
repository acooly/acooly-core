<!-- title: 开发规范  -->
<!-- type: core -->
<!-- author: zhangpu, qiubo -->
<!-- date: 2018-10-12 -->

根据开发指南要求进行设计和开发代码，这里介绍具体规范和约定，非常重要。


## 1.编码规范

###  1.1.代码格式

统一使用idea内置`code style`

### 1.2.代码规范

采用阿里巴巴代码规范，相关资源如下: 

* [阿里巴巴JAVA开发手册](https://github.com/alibaba/p3c/blob/master/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C%EF%BC%88%E8%AF%A6%E5%B0%BD%E7%89%88%EF%BC%89.pdf)
* [IDEA插件使用文档](https://github.com/alibaba/p3c/wiki/IDEA%E6%8F%92%E4%BB%B6%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3)

### 1.3.代码模板

框架提供了IDEA代码模板[idea\_settings\_acooly.jar](res/ide/idea/idea_settings_acooly.jar),包括如下内容：

* 新建java类自动生成头和类的javadoc，包含作者(默认当前操作系统用户名)和时间
* 新建java类标注自动引入`@Slf4j`
* 新建java枚举，自动实现`Messageable`接口
* 新建java测试类，自动引入`Assertions`
* try-cache打印日志并包裹异常
* 代码模板：tryc 自动生成cache段，并初始化好BusinessException

>注意：实体类请删除@Slf4j


### 1.4.枚举约定

* 业务开发中，要求所有的枚举都必须实现`Messageable`接口。
* 枚举的类名称必须以Enum结尾，例如、：userTypeEnum
* 枚举值必须大写，可以使用"_"分割。
* 枚举name和code一样


```java
public enum OptResultEnum implements Messageable{
    SUCCESS("SUCCESS", "成功"),
    INTERNAL_ERROR("INTERNAL_ERROR", "内部错误");
    
    private String code;
    private String message;
    
    //...
}
```


## 2.异常处理

### 2.1.dao层

如果是使用MyBatis或JPA，请不用处理，直接交给spring-data。
如果是jdbcTemplate自己写SQL实现，请使用: DataAccessException及子类。

### 2.2.service层

服务层异常处理后，对外统一使用BusinessException异常，并在服务层定义对应的错误码（Messagable的枚举），通过BusinessException向上传递。

例如：会员管理模块或系统，定义CustomerErrorCodeEnum来定义业务中出现的错误码和错误消息，然后在服务层开发中通过BusinessException(Messageable errorCode...)向上抛出异常。

> 服务层定义错误码并抛出的好处是，facade，openapi和控制层都可以统一直接使用异常和错误码，不用重复翻译和处理异常。

### 2.3.facade和openapi

接口层的异常处理策略：

1. 参数错误的情况下，可使用OrderCheckException或其自定义子类。
2. 如果调用服务层获得了BuisnessException则不用再处理，直接交给框架。
3. 接口层自己产生的异常，请使用BusinessExcetion(Messageable ...)进行包装。

```java
    try {
        // do business
    } catch (IllegalArgumentException ie) {
        // 参数错误，包括：OrderCheckException
        throw new BusinessException(CommonErrorCodes.PARAMETER_ERROR, ie.getMessage());
    } catch (BusinessException be) {
        // 下层抛出的业务异常
        throw be;
    } catch (Exception e) {
        // 其他异常
        throw new BusinessException(CommonErrorCodes.INTERNAL_ERROR);
    }
```

>以上代码结构，可在IDEA中导入idea_settings_acooly.jar配置后，可通过tryc快捷生成结构。

### 2.4.控制层

控制层原则上不应该抛出异常给spring框架和servlet容器，都应该捕获和处理掉。


## 3.facade开发

### 3.1.请求对象

对于参数比较多的情况，建议自定义OrderBase子类

* 查询类请求继承OrderBase
* 命令类请求继承BizOrderBase

### 3.2.响应对象

1. 	`ResultBase`中已经定义了`status`、`code`、`detail`。所有子类去掉其他`code`和`message`.
2. 查询类请求，如果查询对象不存在，返回`status== success`，业务调用方需判断dto是否为空.
3. 命令类请求，且为异步场景。当业务完全执行完毕后`status==success`。任何中间状态都为`status==processing`.


### 3.3.关于响应码`ResultCode`

openapi对外提供服务，对于异常的情况，我们需要返回明确的`ResultCode`。响应码应该为枚举类型，并且实现`Messageable`接口

#### 异常处理

在抛出异常时，需要设置`ResultCode`,`BusinessException`构造器支持下面几种方式设置`ResultCode`：

	BusinessException#BusinessException(Messageable)
	BusinessException#BusinessException(Messageable, String)
	BusinessException#BusinessException(Messageable, Throwable)

####  公共响应码

框架提供了常用的响应码`com.acooly.core.common.facade.ResultCode`,没有特殊需求时，可以使用公共响应码。

#### 应用响应码

应用响应码放在`facade`模块中，名字为`应用名+ResultCode`，比如会员系统为`CustomerResultCode`，交易系统为`TradeResultCode`.响应码枚举类型不需要加应用名前缀，根据业务实际情况定义。


## 数据库设计

请参考：[代码生成](acooly-coder.html)
