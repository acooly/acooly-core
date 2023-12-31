/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-03 14:30
 */
package com.acooly.core.utils.ie;

import com.acooly.core.common.facade.InfoBase;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.core.Ordered;

/**
 * @author zhangpu
 * @date 2022-08-03 14:30
 */
@Getter
@Setter
public class ExportColumnMeta extends InfoBase implements Ordered {

    private String header;
    private String name;
    private int width;
    private boolean showMapping = true;
    private String format;
    private Object value;
    private int order;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ExportColumnMeta)) {
            return false;
        }

        ExportColumnMeta that = (ExportColumnMeta) o;

        return new EqualsBuilder().append(getName(), that.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }
}
