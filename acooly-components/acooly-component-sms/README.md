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
    
### 2.3 注意
   
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