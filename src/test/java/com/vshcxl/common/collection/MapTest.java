package com.vshcxl.common.collection;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snowstone.commons.kern.apiext.collection.MapUtil;

public class MapTest {

	@Test//ok
	public void transMap2Bean() throws InstantiationException, IllegalAccessException {

		Map map = new HashMap<>();
		map.put("name", "zhangsan");
		map.put("age", "25");

		Person per = new Person();
		MapUtil.transMap2Bean(map, per);
		System.out.println(per.getAge() + " " + per.getName());
		
	}
	
	
	@Test//ok
	public void transMap2Bean2() throws InstantiationException, IllegalAccessException {

		Map map = new HashMap<>();
		map.put("name", "zhangsan");
		map.put("age", "25");

		Person per = new Person();
		MapUtil.transMap2Bean2(map, per);
		System.out.println(per.getAge() + " " + per.getName());
	}
	
	@Test//ok
	public void transBean2Map() throws InstantiationException, IllegalAccessException {

		Map<String, Object> map = new HashMap<>();

		Person per = new Person();
		per.setAge("22");
		per.setName("lisi");
		
		map=MapUtil.transBean2Map(per);
		System.out.println(map.get("age")+"  "+per.getName() );
	}
	
	
	
	
	
	
	
	
	
	
	
}
