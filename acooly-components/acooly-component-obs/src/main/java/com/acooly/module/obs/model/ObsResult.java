package com.acooly.module.obs.model;

import lombok.Data;

/** @author shuijing */
@Data
public class ObsResult {

  private boolean success = true;

  private String code = "";

  private String message = "";
}
