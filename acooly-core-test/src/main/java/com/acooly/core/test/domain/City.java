
package com.acooly.core.test.domain;

import com.acooly.core.common.domain.AbstractEntity;
import lombok.Data;

@Data
public class City extends AbstractEntity {
  private String name;

  private String state;

}
