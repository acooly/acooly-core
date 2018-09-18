<!-- title: 文件上传组件   -->
<!-- type: infrastructure -->
<!-- author: kule,qiubo -->
## 1. 组件介绍

此组件提供文件上传(图片压缩)、下载、访问的能力

## 2. 使用说明

maven坐标：

     <dependency>
        <groupId>com.acooly</groupId>
        <artifactId>acooly-component-ofile</artifactId>
        <version>${acooly-latest-version}</version>
      </dependency>

`${acooly-latest-version}`为框架最新版本或者购买的版本。

## 3. 配置
 
 1. `acooly.ofile.storageNameSpace=taodai` 存储命名空间，默认为空，如果填写，文件存储路径会变为：storageRoot/storageNameSpace，如：/data/media/taodai


## 特性

### 文件上传认证

ofile的文件上传都可以直接调用/ofile/upload.html方法，通过OFileUploadAuthenticate接口定义了安全认证。默认内置的认证方式支持两种，Session和摘要签名。

一个系统可以采用多个认证器，只要其中一个认证器通过认证则可上传

#### Session方式认证
无需开发，内置：com.acooly.module.ofile.auth.OFileUploadSessionAuthenticate

你可以通过配置文件定义行为：

```ini
# 是否打开session认证
acooly.ofile.checkSession=true
# 你的系统内的定义的需要验证的Session变量名称
acooly.ofile.checkSessionKey=YOUR_SYSTEM_SESSION_NAME
```
#### 配置方式的签名认证

应对App的简单场景，采用固定的预定accessKey和secretKey签名认证。无需开发，内置：com.acooly.module.ofile.auth.ConfiguredSignatureOFileUploadAuthenticate

你可以通过配置文件定义行为：

```
acooly.ofile.configuredSignAuthEnable=true
acooly.ofile.configuredSignAuthAccessKey=YOUR_ACCESSKEY
acooly.ofile.configuredSignAuthSecretKey=YOUR_SECRETKEY
```


#### 自定义签名认证

在你的系统内实现OFileUploadAuthenticate接口，加入到spring容器即可。

>特别的需要说明，如果你需要与OpenAPI集成统一的认证秘钥体系，你可以继承com.acooly.module.ofile.auth.AbstractSignatureOFileUploadAuthenticate抽象类，实现getSecretKey(String accessKey)方法即可（注入AuthInfoRealm获取accessKey对应的secretKey）。

