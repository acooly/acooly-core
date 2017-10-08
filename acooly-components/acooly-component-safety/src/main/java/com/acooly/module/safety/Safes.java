/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-10-08 23:07 创建
 */
package com.acooly.module.safety;

import com.acooly.module.safety.signature.SignTypeEnum;
import com.acooly.module.safety.signature.Signer;
import com.acooly.module.safety.signature.SignerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author zhangpu 2017-10-08 23:07
 */
@Component
public class Safes {

    @Resource(name = "safetySignerFactory")
    private SignerFactory signerFactory;

    private static Safes safes;

    @PostConstruct
    public void init() {
        safes = this;
    }

    public static Signer getSigner(String signType) {
        return safes.signerFactory.getSigner(signType);
    }

    public static Signer getSigner(SignTypeEnum signType) {
        return safes.signerFactory.getSigner(signType.name());
    }

}
