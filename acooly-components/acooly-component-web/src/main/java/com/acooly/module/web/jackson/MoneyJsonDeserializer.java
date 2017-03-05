/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-05 17:08 创建
 */
package com.acooly.module.web.jackson;

import com.acooly.core.utils.Money;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author qiubo@yiji.com
 */
@JsonComponent
public class MoneyJsonDeserializer extends StdDeserializer<Money> {
    public MoneyJsonDeserializer() {
        this(null);
    }

    public MoneyJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        long  value = p.getLongValue();
        return Money.cent(value);
    }
    @Override
    public Class<Money> handledType() {
        return Money.class;
    }
}