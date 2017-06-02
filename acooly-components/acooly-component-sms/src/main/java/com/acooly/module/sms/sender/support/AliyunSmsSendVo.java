package com.acooly.module.sms.sender.support;

import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/** @author shuijing */
public class AliyunSmsSendVo {

  private static Gson gson = null;
  /** 短信签名 */
  @Getter @Setter private String FreeSignName;
  /** 模板CODE */
  @Getter @Setter private String TemplateCode;
  /** 模板参数 设值后不用设置SmsParams */
  @Getter @Setter private transient Map<String, String> SmsParamsMap = Maps.newHashMap();
  @Getter @Setter private String SmsParams;

  public static synchronized Gson getGson() {
    if (gson == null) {
      GsonBuilder b = new GsonBuilder();
      b.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
      BooleanSerializer serializer = new BooleanSerializer();
      b.registerTypeAdapter(Boolean.class, serializer);
      b.registerTypeAdapter(boolean.class, serializer);
      gson = b.create();
    }
    return gson;
  }

  public String toJson() {
    if (!this.SmsParamsMap.isEmpty()) {
      this.SmsParams = getGson().toJson(this.SmsParamsMap);
    }
    return getGson().toJson(this);
  }

  public AliyunSmsSendVo fromJson(String jsonStr) {
    return getGson().fromJson(jsonStr, this.getClass());
  }
}
