package com.acooly.module.sso;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shuijing
 */
@Data
@ConfigurationProperties(Ssoproperties.PREFIX)
public class Ssoproperties {

    public static final String PREFIX = "acooly.sso";

    private String ssoServerUrl;
}
