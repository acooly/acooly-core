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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author qiubo@yiji.com
 * @author zhangpu@acooly.cn 2019-06-23 修复webProperties为注入BUG，造成enableMoneyDisplayYuan参数无效
 */
@JsonComponent
public class MoneyJsonDeserializer extends StdDeserializer<Money> {
    @Autowired
    private WebProperties webProperties;

    public MoneyJsonDeserializer() {
        this(null);
    }

    public MoneyJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (webProperties == null) {
            webProperties = Apps.buildProperties(WebProperties.class);
        }
        if (webProperties.isEnableMoneyDisplayYuan()) {
            return new Money(p.getValueAsString());
        } else {
            long value = p.getLongValue();
            return Money.cent(value);
        }
    }

    @Override
    public Class<Money> handledType() {
        return Money.class;
    }
}
