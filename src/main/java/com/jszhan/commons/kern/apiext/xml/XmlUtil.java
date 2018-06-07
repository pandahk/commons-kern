package com.jszhan.commons.kern.apiext.xml;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {

	/**
	 * xml to map
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> getResult(String xml) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			Iterator<Element> it = root.elementIterator();
			while (it.hasNext()) {
				Element element = it.next();
				map.put(element.getName(), element.getTextTrim());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}


	/**
	 * obj to xml
	 * @param obj
	 * @return 以格式化的方式输出XML
	 */
    public static String toFXml(Object obj) {
        XStream xstream = new XStream(new DomDriver("utf8"));
        xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
        // 以格式化的方式输出XML
        return xstream.toXML(obj);
    }
    /**
	 * obj to xml
	 * @param obj
	 * @return  以压缩的方式输出XML
	 */
    public static String toComXml(Object obj) {
		XStream xstream = new XStream(new DomDriver("GBK"));
		xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
		// 以压缩的方式输出XML
		StringWriter sw = new StringWriter();
		xstream.marshal(obj, new CompactWriter(sw));
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>").append(sw.toString());
		return sb.toString();
	}

}
