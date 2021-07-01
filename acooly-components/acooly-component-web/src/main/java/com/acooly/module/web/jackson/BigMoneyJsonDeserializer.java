/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-07-01 14:00
 */
package com.acooly.module.web.jackson;

import com.acooly.core.utils.BigMoney;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author zhangpu
 * @date 2021-07-01 14:00
 */
@Slf4j
@JsonComponent
public class BigMoneyJsonDeserializer extends StdDeserializer<BigMoney> {

    protected BigMoneyJsonDeserializer() {
        this(null);
    }

    protected BigMoneyJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BigMoney deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        BigDecimal decimal = p.getDecimalValue();
        return decimal == null ? null : BigMoney.valueOf(decimal);
    }

    @Override
    public Class<BigMoney> handledType() {
        return BigMoney.class;
    }
}
