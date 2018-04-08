package com.acooly.core.common.view;

import lombok.Data;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-08 16:35
 */
@Data
public class ViewResult implements Serializable {
  private static final long serialVersionUID = 1l;
  private Object data;
  private boolean success;
  private String code;
  private String message;

  public ViewResult success(@Nullable Object data) {
    ViewResult viewResult = new ViewResult();
    viewResult.setSuccess(true);
    viewResult.setData(data);
    return viewResult;
  }

  public ViewResult failure(@Nullable String code, @Nullable String message) {
    ViewResult viewResult = new ViewResult();
    viewResult.setSuccess(false);
    viewResult.setCode(code);
    viewResult.setMessage(message);
    return viewResult;
  }

  public ViewResult failure(@Nonnull String message) {
    Assert.hasText(message);
    return failure(null, message);
  }
}
