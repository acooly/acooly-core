## 1. 组件介绍

此组件提供实名认证、银行卡二三三要素认证能力

## 2. 使用说明

### 2.1 服务类

`com.acooly.module.certification.CertificationService`
    

### 2.2 配置

* `acooly.certification.appCode=a08c5badb879******bc8d67e709b` 
  必选，使用阿里云提供者必填appCode
  
* `acooly.certification.bankCertProvider=ali`
  可选，银行卡二三四要素认证提供者，目前默认为ali(阿里云提供商)  

* `acooly.certification.provider=ali`
  可选，实名认证提供者，目前默认为ali(阿里云提供商)
    
* `acooly.certification.url=http://idcard.market.alicloudapi.com`
  可选，实名认证服务地址，可修改 



### 2.3 接口使用
```
             @Autowired private CertificationService certificationService;
            
             public void four(String realName, String cardNo, String certId, String phoneNum) {
                BankCardResult result =
                    certificationService.bankCardCertFour(realName, cardNo, certId, phoneNum);
                log.info("银行卡四要素验证结果:{}", result.toString());
              }

```

            
              