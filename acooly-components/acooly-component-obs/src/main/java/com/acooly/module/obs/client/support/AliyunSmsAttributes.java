package com.acooly.module.obs.client.support;

import lombok.Getter;
import lombok.Setter;

/** @author shuijing */
public class AliyunSmsAttributes extends AliyunSmsSendVo {
  /** 短信接收人 */
  @Getter @Setter private String Receiver;
}
