### 1. 怎么配置csrf排除路径

    acooly.security.csrf.exclusions.ofile[0]=/ofile/gateway/**
    acooly.security.csrf.exclusions.ofile[1]=/ofile/cs/**
    
### 2. 怎么开启登录短信验证码

* 配置开启短信验证
    `acooly.security.loginSmsEnable=true
    `
* 项目在引入security组件同时必须引入sms组件
    `<dependency>
          <groupId>com.acooly</groupId>
          <artifactId>acooly-component-sms</artifactId>
     </dependency>`
     
     短信验证码发送已经适配各个短信通道
     特殊的通道，比如 容联云通讯 可以修改短信模板id
     `acooly.sms.cloopen.loginVerifCodeTemplateId=183256
     `
* 可配置页面重新发送按钮间隔,单位秒，默认为30秒
     `acooly.framework.smsSendInterval=60
      `