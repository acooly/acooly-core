package com.acooly.module.obs.client.oss;

import lombok.Getter;
import lombok.Setter;

/**
 * A generic result that contains some basic response options, such as requestId.
 *
 * @author shuijing
 */
@Getter
@Setter
public abstract class GenericResult {
   
    private String requestId;
    private Long clientCRC;
    private Long serverCRC;
    ResponseMessage response=new ResponseMessage();
}
