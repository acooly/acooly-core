/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-05 17:08 创建
 */
package com.acooly.module.web.jackson;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.utils.Money;
import com.acooly.module.web.WebProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author qiubo@yiji.com
 * @author zhangpu@acooly.cn 2019-06-23 修复webProperties为注入BUG，造成enableMoneyDisplayYuan参数无效
 */
@JsonComponent
public class MoneyJsonSerializer extends JsonSerializer<Money> {
    @Autowired
    private WebProperties webProperties;

    @Override
    public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        if (webProperties == null) {
            webProperties = Apps.buildProperties(WebProperties.class);
        }
        if (webProperties.isEnableMoneyDisplayYuan()) {
            jgen.writeString(value.getAmount().toString());
        } else {
            jgen.writeNumber(value.getCent());
        }
    }

    @Override
    public Class<Money> handledType() {
        return Money.class;
    }
}
