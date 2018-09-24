<!-- title: 后台管理组件   -->
<!-- type: infrastructure -->
<!-- author: kule,qiubo,shuijing -->
## 1. 组件介绍

此组件提供文件上传(图片压缩)、下载、访问的能力

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-security</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

## 3. FAQ

### 3.1 怎么开启登录短信验证码

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
* 开启短信验证码功能后，在新增user的时候，填写手机号码


### 3.2 怎么设置不需要需登录(授权、认证)访问所有boss页面

只需要添加配置：
`acooly.security.shiroFilterAnon=true
`

开启后shiro filter链都会设为不拦截，可在系统不需要任何授权、认证时开启（慎用）

### 3.3 设置单点登录启用dubbo权限校验服务

在使用单点登录的时候，主boss作为权限的主配置提供者，默认提供http接口校验权限`{@link com.acooly.module.security.web.RoleFacadeController.isPermitted}`
当使用dubbo作为权限校验时候，主boss应用必须开启服务提供者：
`acooly.security.enableSSOAuthzService=true
`

### 3.4 新增用户后，需要做其他业务操作，如何扩展？
       
               //异步事件处理器
               @Handler(delivery = Invoke.Asynchronously)
               public void handleCreateCustomerEventAsyn(UserCreatedEvent user) {
                   //do what you like
                   log.info("异步用户保存事件处理器{}",user.toString());
               }
               
               
## 4.附录

### 开发资源

1. **图标库**：
    boss后台的开发可以使用：fontawesome字体图标库，已引入到框架中。
    图标地址：http://www.fontawesome.com.cn/faicons/
    案例：
    ```html
    <i class="fa fa-flask fa-fw fa-lg fa-col" aria-hidden="true"></i>
    ```
2. layui库：已经引入，可以直接使用其组件。

### 风格

* acooly：v3标准/easyui标准风格
* acooly4：layui风格


             