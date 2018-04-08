package com.acooly.core.common.view;

import com.acooly.core.common.facade.DtoAble;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.common.facade.ResultCode;
import com.acooly.core.utils.enums.ResultStatus;
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
  /** 请求处理状态 */
  private boolean success;
  /** 响应数据 */
  private Object data;
  /** 请求处理失败后状态码 */
  private String code;
  /** 请求处理失败后消息 */
  private String detail;

  public static ViewResult success(@Nullable Object data) {
    ViewResult viewResult = new ViewResult();
    viewResult.setSuccess(true);
    viewResult.setData(data);
    return viewResult;
  }

  public static ViewResult failure(@Nullable String code, @Nullable String detail) {
    ViewResult viewResult = new ViewResult();
    viewResult.setSuccess(false);
    viewResult.setCode(code);
    viewResult.setDetail(detail);
    return viewResult;
  }

  public static ViewResult failure(@Nonnull String detail) {
    Assert.hasText(detail);
    return failure(null, detail);
  }

  /** 通过dubbo响应结果构造ViewResult */
  public static ViewResult from(@Nullable ResultBase resultBase) {
    if (resultBase == null) {
      return failure(ResultCode.INTERNAL_ERROR.code(), ResultCode.INTERNAL_ERROR.message());
    } else {
      if (resultBase.getStatus() == ResultStatus.failure) {
        return failure(resultBase.getCode(), resultBase.getDetail());
      } else {
        if (resultBase instanceof DtoAble) {
          return success(((DtoAble) resultBase).getDto());
        } else {
          return success(null);
        }
      }
    }
  }
}
