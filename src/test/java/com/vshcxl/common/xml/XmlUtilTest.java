package com.vshcxl.common.xml;

import java.util.Map;

import org.junit.Test;

import com.snowstone.commons.kern.apiext.xml.XmlUtil;

public class XmlUtilTest {

	@Test
	public void getResult() {

		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><person><name>zhangsan</name><age>age</age></person>";
		Map<String, Object> map = XmlUtil.getResult(xml);
		System.out.println(map.get("name"));

	}
}
