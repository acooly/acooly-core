package com.acooly.core.test.domain;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.type.DBMap;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;

@Getter
@Setter
public class City extends AbstractEntity {
    private String name;

    private String state;

    @Transient
    private String data;

    private DBMap<String, String> ext;
}
