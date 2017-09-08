package com.vshcxl.common.collection;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.snowstone.commons.kern.apiext.collection.MapUtil;

public class MapTest {

	@Test
	public void convertMap() throws InstantiationException, IllegalAccessException {

		Map map = new HashMap<>();
		map.put("name", "zhangsan");
		map.put("age", "25");

		Person per = new Person();
		MapUtil.transMap2Bean(map, per);
		System.out.println(per.getAge() + " " + per.getName());

	}
}
