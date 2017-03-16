## 1. 组件介绍

此组件给应用邮件服务能力

## 2. 使用说明

### 2.1 邮件服务类

    com.acooly.module.mail.MailService

### 2.2 如何使用邮件模板

1. 新增邮件模板

    邮件模板为`freemarker`模板

    `src/main/resources/mail/register_success.ftl`：

            <html>
              <head>
                    <title>恭喜您注册成功</title>
                </head>
                <body>
                    <h1>${message},${name}</h1>
                </body>
            </html>


2. 使用邮件模板

    	MailDto dto = new MailDto();
    	dto.to("qiuboboy@qq.com").subject("恭喜您注册成功").param("name", "x").param("message", "how are you!")
    			.templateName("register_success");
    		mailService.sendAsync(dto);