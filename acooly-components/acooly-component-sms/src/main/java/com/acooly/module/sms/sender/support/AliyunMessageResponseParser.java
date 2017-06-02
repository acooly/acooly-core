package com.acooly.module.sms.sender.support;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/** @author shuijing */
public class AliyunMessageResponseParser extends BaseXMLSerializer {

  public static final String MessageId = "MessageId";
  public static final String Message = "Message";
  public static final String MessageBodyMD5 = "MessageBodyMD5";

  public static final AliyunMessageResponseParser getInstance() {
    return AliyunMessageResponseParser.SingletonHolder.INSTANCE;
  }

  public Document parse(String xml) throws ParserConfigurationException, IOException, SAXException {
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(xml));
    Document parse = getDocmentBuilder().parse(is);
    return parse;
  }

  private static class SingletonHolder {
    private static final AliyunMessageResponseParser INSTANCE = new AliyunMessageResponseParser();
  }
}
