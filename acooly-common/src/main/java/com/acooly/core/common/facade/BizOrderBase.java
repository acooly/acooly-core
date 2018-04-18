package com.acooly.core.common.facade;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 命令请求基类
 *
 * @author qiubo@yiji.com
 */
@Getter
@Setter
public class BizOrderBase extends OrderBase {
    /**
     * 商户订单号
     */
    private String merchOrderNo;
    /**
     * 业务订单号 *
     */
    @NotEmpty
    private String bizOrderNo;
}
