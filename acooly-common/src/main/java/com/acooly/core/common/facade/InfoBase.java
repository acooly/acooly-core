package com.acooly.core.common.facade;

import com.acooly.core.utils.ToString;

import java.io.Serializable;

/** @author qiubo@yiji.com */
public class InfoBase implements Serializable {
  @Override
  public String toString() {
    return ToString.toString(this);
  }
}
