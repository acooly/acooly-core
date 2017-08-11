## 规范

根据开发指南要求开发代码，并参考如下规范

### 1. code format

统一使用google-java-format

https://github.com/google/google-java-format

### 2. 请求对象

对于参数比较多的情况，建议自定义OrderBase子类

* 查询类请求继承OrderBase
* 命令类请求继承BizOrderBase

### 3. 关于响应对象的使用规范

1. 	`ResultBase`中已经定义了`status`、`code`、`detail`。所有子类去掉其他`code`和`message`.
2. 查询类请求，如果查询对象不存在，返回`status== success`，业务调用方需判断dto是否为空.
3. 命令类请求，且为异步场景。当业务完全执行完毕后`status==success`。任何中间状态都为`status==processing`.

### 4. 枚举继承`Messageable`



### 参考更多

* [**易极付代码开发规范**](http://gitlab.yiji/peigen/tech-manage-doc/blob/master/人员管理/制度规范/技术中心/开发规范/易极付代码开发规范.md)

* [**阿里巴巴JAVA开发手册**](http://gitlab.yiji/fintech/fintech-docs/blob/master/spec/阿里巴巴%20JAVA%20开发手册.pdf)
