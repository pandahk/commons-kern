package com.snowstone.commons.kern.apiext.xml;

import java.io.ByteArrayOutputStream;
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
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {

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

	public static void main(String[] args) throws JAXBException {
		
		
		System.out.println(toXml(getSimpleDepartment()));
	}
	/** 
     * 生成一个简单的Department对象 
     * @return 
     */  
    private static Department getSimpleDepartment() {  
        List<Staff> staffs = new ArrayList<Staff>();  
        Staff stf = new Staff();  
        stf.setName("周杰伦");  
        stf.setAge(30);  
        stf.setSmoker(false);  
        staffs.add(stf);
        Staff stf1 = new Staff();  
        stf1.setName("周笔畅");  
        stf1.setAge(28);  
        stf1.setSmoker(false);  
        staffs.add(stf1);  
        Staff stf2 = new Staff();  
        stf2.setName("周星驰");  
        stf2.setAge(40);  
        stf2.setSmoker(true);  
        staffs.add(stf2);  
          
        Department dept = new Department();  
        dept.setName("娱乐圈");  
        dept.setStaffs(staffs);  
          
        return dept;  
    }  
    
    
    
    public static String toXml(Object obj) {
        XStream xstream = new XStream(new DomDriver("utf8"));
        xstream.processAnnotations(obj.getClass()); // 识别obj类中的注解
        // 以格式化的方式输出XML
        return xstream.toXML(obj);
    }
}
