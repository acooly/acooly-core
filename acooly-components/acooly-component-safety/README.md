safety 安全组件
===========

## 简介

作为框架的所有安全相关的能力的工具组件。包括：签名，加解密等。防御相关的已独立为defence,不包括在该组件里面。主要是解决openapi等体系场景内的各个组件或系统都独立存在安全相关的能力代码，而且都是采用代码拷贝方式复用，还经常出现名字冲突等问题。

>为什么叫safety？
>只是因为security这个名字已被boss组件acooly-component-security使用。

## todo

* 待完成加解密部分

## 直接使用

请参考本组件的单元测试代码。单元测试中，分别对基于digest的签名，基于证书(pfx或jks)的签名和基于公私钥对的签名都做了测试（或者说是demo），这里都是采用直接编码方式使用。

集成方式为：依赖该组件，但是不要打开组件开关（spring不会自动扫描）


## Spring方式

请参考acooly-core-test模块中的SafetyTestController.

你可以运行工程后，访问：/safety/signDigest.html?signType=Sha256Hex&key=123
获得以下结果：

```js
{
    signature: "8343ada7565a844344ab07a2b21e42b28cc51096ae8bfc7b136e15e77357a744",
    plain: {
        1: "12312312",
        a: "12asdf",
        ccc: "中国人",
        asdf: "2323"
    },
    signType: "Sha256Hex",
    verify: true,
    key: "123"
}
```

