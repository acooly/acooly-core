package com.acooly.module.appopenapi.support;

import com.acooly.core.common.facade.DtoBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto extends DtoBase {
    private String accessKey;
    private String customerId;
    private String extJson;
    /**
     * 返回false 不保存登录记录
     */
    private boolean loginSuccess = true;
}
