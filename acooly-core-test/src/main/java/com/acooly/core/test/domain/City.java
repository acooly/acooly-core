package com.acooly.core.test.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Map;

@Data
public class City extends AbstractEntity {
    private String name;

    private String state;

    @Transient
    private String data;

    private Map<String,String> ext;
}
