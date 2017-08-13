package com.acooly.module.certification.cert.impl;

import com.acooly.core.utils.enums.Messageable;
import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.certification.CertificationProperties;
import com.acooly.module.certification.cert.BankCardCertService;
import com.acooly.module.certification.cert.CertficationException;
import com.acooly.module.certification.common.Response;
import com.acooly.module.certification.enums.BankCardResult;
import com.acooly.module.certification.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** @author shuijing */
@Slf4j
@Service("aliBankCardCertService")
public class AliBankCardCertServiceImpl implements BankCardCertService {

  @Autowired private CertificationProperties certificationProperties;

  private final String certUrl = "http://ali-bankcard4.showapi.com";

  public static final String SUCCESS_CODE = "0";

  @Override
  public BankCardResult bankCardCert(
      String realName, String cardNo, String certId, String phoneNum) {

    String appCode = certificationProperties.getAppCode();
    String path = "/bank4";
    //二要素
    if (StringUtils.isEmpty(certId) && StringUtils.isEmpty(phoneNum)) {
      path = "/bank2";
    }
    //三要素
    if (!StringUtils.isEmpty(certId) && StringUtils.isEmpty(phoneNum)) {
      path = "/bank3";
    }

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "APPCODE " + appCode);
    Map<String, String> params = new HashMap<>();
    params.put("acct_name", realName);
    params.put("acct_pan", cardNo);
    params.put("cert_id", certId);
    params.put("cert_type", "01");
    params.put("needBelongArea", "true");
    params.put("phone_num", phoneNum);
    BankCardResult result;
    try {
      Response response =
          HttpUtil.httpGet(certUrl, path, certificationProperties.getTimeout(), headers, params);

      result = unmashall(response);

    } catch (ConnectTimeoutException e) {
      log.warn("银行卡二三四要素校验连接超时:{}", e.getMessage());
      throw new CertficationException(ResultStatus.failure.getCode(), "连接超时");
    } catch (CertficationException e) {
      throw e;
    } catch (Exception e) {
      log.warn("银行卡二三四要素校验未知异常:{}", e.getMessage());
      throw new CertficationException(ResultStatus.failure.getCode(), "未知异常");
    }
    return result;
  }

  private BankCardResult unmashall(Response response) throws CertficationException {

    if (StringUtils.isEmpty(response.getBody())) {
      throw new CertficationException(ResultStatus.failure.getCode(), "银行卡二三四要素校验返回空");
    }

    BankCardResult result = new BankCardResult();
    JSONObject total = JSON.parseObject(response.getBody());

    String resCode = total.getString("showapi_res_code");
    String resError = total.getString("showapi_res_error");

    if (!SUCCESS_CODE.equals(resCode)) {
      throw new CertficationException(ResultStatus.failure.getCode(), resError);
    }

    if (SUCCESS_CODE.equals(resCode)) {
      JSONObject resBody = total.getJSONObject("showapi_res_body");
      String retCode = resBody.getString("ret_code");
      String code = resBody.getString("code");

      String msg = resBody.getString("msg");

      String notNullmsg = StringUtils.isEmpty(msg) ? RET_CODE.get(code) : msg;

      if (!SUCCESS_CODE.equals(retCode)) {
        log.info("银行卡二三四要素校验失败，结果:{}", notNullmsg);
        throw new CertficationException(ResultStatus.failure.getCode(), notNullmsg);
      }

      JSONObject belong = resBody.getJSONObject("belong");

      result.setBelongArea(belong.getString("area"));
      result.setBankTel(belong.getString("tel"));
      result.setBrand(belong.getString("brand"));
      result.setBankName(belong.getString("bankName"));
      result.setCardType(belong.getString("cardType"));
      result.setBankUrl(belong.getString("url"));
      result.setCardNo(belong.getString("cardNum"));
      if (SUCCESS_CODE.equals(code)) {
        result.setStatus(ResultStatus.success);
        log.info("银行卡二三四要素校验成功{}", result);
      } else {
        result.setStatus(ResultStatus.failure);
        result.setDetail(notNullmsg);
        log.info("银行卡二三四要素校验失败：{},{}", notNullmsg,result);
      }
    }
    return result;
  }

  /*public static void main(String[] args) {

    String s =
        "{\n"
            + "\t\"showapi_res_code\": 0,\n"
            + "\t\"showapi_res_error\": \"\",\n"
            + "\t\"showapi_res_body\": {\n"
            + "\t\t\"code\": \"0\",\n"
            + "\t\t\"msg\": \"资料匹配,账号正常\",\n"
            + "\t\t\"ret_code\": \"0\",\n"
            + "\t\t\"error\": \"\",\n"
            + "                \"belong\":{\n"
            + "                      \"area\": \"福建省 - 漳州市\",//地区\n"
            + "\t\t      \"tel\": \"95568\",//银行客服\n"
            + "\t\t      \"brand\": \"民生借记卡(银联卡)\",//银行卡产品名称\n"
            + "\t\t      \"bankName\": \"中国民生银行（03050000）\",//银行名称\n"
            + "\t\t       \"cardType\": \"借记卡\",//银行卡种\n"
            + "\t\t       \"url\": \"www.cmbc.com.cn\",//银行官网\n"
            + "\t\t       \"cardNum\": \"622622070288xxxx\"\n"
            + "               }\n"
            + "\t}\n"
            + "}";
    JSONObject jsonObject = JSON.parseObject(s);
    JSONObject resBody = jsonObject.getJSONObject("showapi_res_body");

    String retCode = resBody.getString("ret_code");
    String msg = resBody.getString("msg");

    JSONObject belong = resBody.getJSONObject("belong");
    String bankName = belong.getString("bankName");
    String resCode = jsonObject.getString("showapi_res_code");
    //String showapi_res_code = jsonObject.getString("showapi_res_code");
    JSONObject resError = jsonObject.getJSONObject("showapi_res_error");
    System.out.printf("aa");
  }*/

  private static final Map<String, String> RET_CODE =
      new LinkedHashMap<String, String>() {
        {
          put("0", "匹配");
          put("1", "交易失败,请联系发卡行");
          put("4", "此卡被没收,请于发卡方联系");
          put("5", " 持卡人认证失败");
          put("14", "无效卡号");
          put("15", "此卡无对应发卡方");
          put("21", "该卡未初始化或睡眠卡");
          put("34", "作弊卡,吞卡 ");
          put("40", "发卡方不支持的交易");
          put("41", "此卡已经挂失");
          put("43", "此卡被没收");
          put("54", "该卡已过期");
          put("57", "发卡方不允许此交易");
          put("62", "受限制的卡");
          put("75", "密码错误次数超限");
          put("82", "身份证号码有误");
          put("83", "银行卡号码有误");
          put("84", "手机号码不合法");
          put("86", "持卡人信息有误");
          put("87", "未开通无卡支付");
          put("96", "交易失败请重试");
        }
      };
}