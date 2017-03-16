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