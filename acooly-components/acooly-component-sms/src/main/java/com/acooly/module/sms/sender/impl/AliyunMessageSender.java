package com.acooly.module.sms.sender.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.core.utils.security.Cryptos;
import com.acooly.module.sms.SmsProperties;
import com.acooly.module.sms.sender.ShortMessageSendException;
import com.acooly.module.sms.sender.support.AliyunSmsAttributes;
import com.acooly.module.sms.sender.support.AliyunSmsSendVo;
import com.acooly.module.sms.sender.support.parser.AliyunMessageResponseParser;
import com.acooly.module.sms.sender.support.parser.BaseMessageResponseParser;
import com.acooly.module.sms.sender.support.serializer.AliyunMessageSendSerializer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 阿里云短信接口
 *
 * @author shuijing
 * @link https://help.aliyun.com/document_detail/27497.html?spm=5176.doc27501.6.733.LnsIrn
 * <p>阿里云通道只支持模板和签名为短信内容 发送接口send(String mobileNo, String content) content内容需为json格式 见测试用例： @See
 * Scom.acooly.core.test.web.TestController#testAliyunSms()
 */
@Service("aliyunMessageSender")
public class AliyunMessageSender extends AbstractShortMessageSender {

    @Autowired
    private SmsProperties properties;

    public static String getGMT(Date dateCST) {
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return (df.format(dateCST));
    }

    @Override
    public String send(String mobileNo, String content) {
        ArrayList<String> list = Lists.newArrayListWithCapacity(1);
        list.add(mobileNo);
        return send(list, content);
    }

    @Override
    public String send(List<String> mobileNos, String content) {

        String mobileNo = Joiner.on(",").join(mobileNos);
        String gmt = getGMT(new Date());

        SmsProperties.Aliyun aliyun = properties.getAliyun();
        String topicName = aliyun.getTopicName();
        String cityName = topicName.substring(topicName.indexOf("-") + 1);

        String encode = aliyunSign(gmt, topicName, aliyun.getAccessKeySecret());

        //content为AliyunSmsSendVo转换的json字符串
        AliyunSmsSendVo aliVo = AliyunSmsSendVo.getGson().fromJson(content, AliyunSmsSendVo.class);

        AliyunSmsAttributes alisa = new AliyunSmsAttributes();
        alisa.setTemplateCode(aliVo.getTemplateCode());
        alisa.setSmsParams(aliVo.getSmsParams());
        alisa.setFreeSignName(aliVo.getFreeSignName());
        alisa.setReceiver(mobileNo);
        String paramString = alisa.toJson();

        Https instance = Https.getInstance();
        instance.connectTimeout(timeout / 2);
        instance.readTimeout(timeout / 2);
        try {
            String xml = AliyunMessageSendSerializer.getInstance().serialize(paramString, "UTF-8");
            InputStream xmlSerialize = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            //"http://1095791883952390.mns.cn-hangzhou.aliyuncs.com/topics/sms.topic-cn-hangzhou/messages"
            HttpPost httppost =
                    new HttpPost(
                            "http://"
                                    + aliyun.getAccountId()
                                    + ".mns."
                                    + cityName
                                    + ".aliyuncs.com/topics/"
                                    + topicName
                                    + "/messages");

            InputStreamEntity inputStreamEntity = new InputStreamEntity(xmlSerialize);
            httppost.setEntity(inputStreamEntity);

            Map<String, String> headers = Maps.newHashMap();
            headers.put("Authorization", "MNS " + aliyun.getAccessKeyId() + ":" + encode);
            headers.put("Date", gmt);
            headers.put("Content-Type", "text/xml;charset=utf-8");
            headers.put("x-mns-version", "2015-06-06");

            HttpResult result = instance.execute(null, httppost, headers, false, "utf-8");
            return handleResult(result, paramString);

        } catch (Exception e) {
            logger.warn("发送短信失败 {号码:" + mobileNo + ",内容:" + content + "}, 原因:" + e.getMessage());
            if (e instanceof BusinessException) {
                String code = ((BusinessException) e).getCode();
                throw new ShortMessageSendException(
                        code, ((BusinessException) e).message(), e.getMessage());
            }
            if (e instanceof IOException
                    || e instanceof ParserConfigurationException
                    || e instanceof SAXException) {
                throw new ShortMessageSendException("-1", "解析返回数据出错", e.getMessage());
            }
            throw new ShortMessageSendException("-1", "请求失败", e.getMessage());
        }
    }

    private String aliyunSign(String gmtDate, String topicName, String accessKeySecret) {
        StringBuilder sign = new StringBuilder();
        sign.append("POST")
                .append("\n")
                .append("")
                .append("\n")
                .append("text/xml;charset=utf-8")
                .append("\n")
                .append(gmtDate)
                .append("\n")
                .append("x-mns-version:2015-06-06")
                .append("\n")
                .append("/topics/")
                .append(topicName)
                .append("/messages");
        String signStr = sign.toString();
        return new String(
                Base64.encodeBase64(Cryptos.hmacSha1(signStr.getBytes(), accessKeySecret.getBytes())));
    }

    protected String handleResult(HttpResult result, String paramString)
            throws IOException, ParserConfigurationException, SAXException {

        String body = result.getBody();
        if (StringUtils.isEmpty(body)) {
            throw new BusinessException("返回数据为空");
        }
        Document document = AliyunMessageResponseParser.getInstance().parse(body);
        NodeList messageId = document.getElementsByTagName(AliyunMessageResponseParser.MessageId);

        if (messageId.getLength() == 0 && result.getStatus() != 201) {
            //error
            NodeList message = document.getElementsByTagName(AliyunMessageResponseParser.Message);
            Element line = (Element) message.item(0);
            throw new BusinessException(
                    "发送失败：" + BaseMessageResponseParser.getCharacterDataFromElement(line));
        } else {
            //success
            Element msgid = (Element) messageId.item(0);
            NodeList messageBodyMD5 =
                    document.getElementsByTagName(AliyunMessageResponseParser.MessageBodyMD5);
            Element msgMD5 = (Element) messageBodyMD5.item(0);
            logger.info(
                    "{} 发送成功，MessageId:{},MessageBodyMD5:{}",
                    paramString,
                    BaseMessageResponseParser.getCharacterDataFromElement(msgid),
                    BaseMessageResponseParser.getCharacterDataFromElement(msgMD5));
            return "success";
        }
    }

    @Override
    public String getProvider() {
        return "阿里云";
    }
}
