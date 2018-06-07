package com.vshcxl.common.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.junit.Test;

import com.jszhan.commons.kern.apiext.xml.XmlUtil;

public class XmlUtilTest {

	@Test
	public void getResult() {

		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><person><name>zhangsan</name><age>age</age></person>";
		Map<String, Object> map = XmlUtil.getResult(xml);
		System.out.println(map.get("name"));

	}
	
	@Test
	public void toComXml() {
		System.out.println(XmlUtil.toComXml(getSimpleDepartment()));
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
    
    
    
}
