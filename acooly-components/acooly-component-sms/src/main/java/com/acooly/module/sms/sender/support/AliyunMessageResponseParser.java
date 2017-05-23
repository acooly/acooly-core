package com.acooly.module.sms.sender.support;


import javax.xml.parsers.*;

import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * @author shuijing
 */
public class AliyunMessageResponseParser extends BaseXMLSerializer {

    public static final String MessageId = "MessageId";
    public static final String Message = "Message";
    public static final String MessageBodyMD5 = "MessageBodyMD5";

    private static class SingletonHolder {
        private static final AliyunMessageResponseParser INSTANCE = new AliyunMessageResponseParser();
    }

    public static final AliyunMessageResponseParser getInstance() {
        return AliyunMessageResponseParser.SingletonHolder.INSTANCE;
    }

    public Document parse(String xml) throws ParserConfigurationException, IOException, SAXException {
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document parse = getDocmentBuilder().parse(is);
        return parse;
    }
}
