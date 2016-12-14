package com.acooly.core.main;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Maps;

public class XmlParser {

	public static void main(String[] args) {
		String xml = "<data><forgcode>6652000</forgcode><resmsg>调用成功</resmsg><result>1</result><txchannel>1</txchannel><resfilemny>0</resfilemny><rescode>1</rescode><list><payendmnh>201312</payendmnh><curnopay>0</curnopay><curbal>0</curbal><pername>杨怀竹</pername><ninterdate>2014-06-30 00:00:00.0</ninterdate><bmny>3000</bmny><acccode>220130165927</acccode><linterdate>2014-06-30 00:00:00.0</linterdate><lbal>15715.69</lbal><lasttime>2014-03-27 15:29:25.0</lasttime><accstate>01</accstate><percode>220130165927</percode><ydaccbal>15715.69</ydaccbal><peride>99</peride><depstate>01</depstate><lratecum>5082000</lratecum><corpcode>120130103718</corpcode><deltime>null</deltime><bmnytimes>0</bmnytimes><dlistmnh>201401</dlistmnh><codetype>01</codetype><deptype>01</deptype><corpacc>120130103718</corpacc><accbal>15715.69</accbal><depmny>720</depmny><perperscale>12</perperscale><corpname>915水文工程地质队</corpname><fpaymnh>200607</fpaymnh><curratecum>1728000</curratecum><regtime>2006-07-01 00:00:00.0</regtime><corpdepmny>360</corpdepmny><cusver>1</cusver><scaletimes>0</scaletimes><codeno>411521198611260032</codeno><lnopay>0</lnopay><perdepmny>360</perdepmny><depacc>120130103718</depacc><percorpscale>12</percorpscale></list><resident>bank12961</resident><torgcode>6652gjj</torgcode></data>";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			Element root = document.getDocumentElement();

			Map<String, Object> data = parseNode(root);
			System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 递归打印document的主要节点
	 * 
	 * @param e
	 */
	private static Map<String, Object> parseNode(Element e) {
		Map<String, Object> data = Maps.newHashMap();
		if (e.hasChildNodes()) {
			NodeList subList = e.getChildNodes();
			for (int i = 0; i < subList.getLength(); i++) {
				Node n = subList.item(i);
				if (n.getFirstChild().getNodeType() == Node.TEXT_NODE) {
					data.put(n.getNodeName(), n.getTextContent());
				} else {
					data.put(n.getNodeName(), parseNode((Element) n));
				}
			}
		}
		return data;
	}

}
