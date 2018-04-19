package com.acooly.core.test.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.module.mybatis.typehandler.DBMap;
import lombok.Data;

import javax.persistence.Transient;

@Data
public class City extends AbstractEntity {
    private String name;

    private String state;

    @Transient
    private String data;

    private DBMap<String,String> ext;
}
