## 1. 组件介绍

此组件给应用短信服务能力

## 2. 使用说明

### 2.1 短信服务类

    com.acooly.module.sms.SmsService

### 2.2 如何配置短信模板

1. 新增短信模板配置文件

    `src/main/resources/smstemplate.properties`：

        ##短信模板配置文件，如下面的例子，模板名为register_success，值为freemarker表达式
        acooly.sms.template.register_success=${name}恭喜您注册成功。

2. 加载短信模板配置文件

   `@PropertySource("classpath:/smstemplate.properties")`

    把此注解加到配置类(标注了@Configuration注解的类)。
    
### 2.3 各个渠道配置说明

1. 亿美
```
acooly.sms.provider=emay
acooly.sms.emay.sn=9SDK-***-0999-RKRMS
acooly.sms.emay.password=7****4
acooly.sms.emay.sign=\u9526\u5DDE\u94F6\u884C
```

2. 漫道
```
acooly.sms.url=http://sdk.entinfo.cn:8061
acooly.sms.username=xxx
acooly.sms.password=xxx
acooly.sms.batchUser=xxx
acooly.sms.batchPswd=xxx
acooly.sms.prefix=xxx(前缀)
acooly.sms.posfix=xxx(后缀)
```
3. 重庆客亲通
```
acooly.sms.url=http://sms.17871.net/WS
acooly.sms.username=xxx
acooly.sms.password=xxx
```
4. 创蓝253
```
acooly.sms.provider=cl253
acooly.sms.cl253.un=N8***498
acooly.sms.cl253.pw=9RnI****83d7
acooly.sms.cl253.sign=\u519B\u878D\u878D\u8D44\u79DF\u8D41
```

5. 阿里云
```
acooly.sms.provider=aliyun
acooly.sms.aliyun.accountId=109579****52390
acooly.sms.aliyun.accessKeyId=LTAIKG****SxZdYJQ
acooly.sms.aliyun.accessKeySecret=WVQ1t6y****KFgMq9xTUJ4um86
acooly.sms.aliyun.topicName=sms.topic-cn-hangzhou
```

6. 容联.云通讯
```
acooly.sms.provider=cloopen
acooly.sms.cloopen.accountId=8aaf07085a3c*****44aee64302f2
acooly.sms.cloopen.accountToken=54b8454ee795*****f386582305b7fe
acooly.sms.cloopen.appId=8aaf07085b8e61*****b8f6274f40111
```
 
6. MOCK测试 记录日志但不会发送短信
```
acooly.sms.provider=mock
acooly.sms.cloopen.accountId=8aaf07085a3c*****44aee64302f2
acooly.sms.cloopen.accountToken=54b8454ee795*****f386582305b7fe
acooly.sms.cloopen.appId=8aaf07085b8e61*****b8f6274f40111
```    
### 2.4 注意
   
1. 阿里云短信通道,阿里云通道只支持模板和签名为短信内容,
   发送接口send(String mobileNo, String content) content内容需为json格式 如：
   `AliyunSmsSendVo vo=new AliyunSmsSendVo();
    params.put("customer", "Testcustomer");
    asa.setFreeSignName("观世宇");
    asa.setSmsParamsMap(params);
    asa.setTemplateCode("SMS_67185863");
    content = asa.toJson();`
    测试用例见 `com.acooly.core.test.web.TestController#testAliyunSms()`
    
2. 容联.云通讯通道,只支持模板短信内容,
   发送接口send(String mobileNo, String content) content内容需为json格式 如：
   `  CloopenSmsSendVo clo = new CloopenSmsSendVo();
      clo.setTemplateId("1");
      List<String> data = new ArrayList<>();
      data.add("aaattt");
      clo.setDatas(data);
      smsService.send("18612299409", clo.toJson());`
    测试用例见 `com.acooly.core.test.web.TestController#testCloopenSms()`    
    
3. 短信黑名单遵循以下规则
  如数据库表`sms_black_list`有黑名单记录，则发送以数据库记录为准。如数据库表无记录，则以配置文件`acooly.sms.blacklist`配置为准
    