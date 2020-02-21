package com.acooly.module.tenant.core;

import java.io.Serializable;
import lombok.Data;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Data
public abstract  class TenantMessage implements Serializable {

    private String tenantId ;


}
