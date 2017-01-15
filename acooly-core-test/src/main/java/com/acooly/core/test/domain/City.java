
package com.acooly.core.test.domain;

import com.acooly.core.common.domain.AbstractEntity;


public class City extends AbstractEntity {
    private String name;

    private  String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
