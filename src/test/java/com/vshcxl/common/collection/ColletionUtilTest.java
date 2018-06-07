package com.vshcxl.common.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.jszhan.commons.kern.Conf;
import com.jszhan.commons.kern.apiext.collection.CollectionUtil;

public class ColletionUtilTest {

	@Test
	public void array2List() {
		Person p = new Person();
		p.setAge("12");
		p.setName("zs");
		List<Person> list = new ArrayList<>();
		list.add(p);
		String[] array = new String[] { "name", "age" };
		List<String> ll = CollectionUtil.array2List(array);
		System.out.println(ll.toString());

	}

	@Test
	public void array2Set() {
		String[] array2 = new String[] { "nameset", "ageset" };
		Set<String> set = CollectionUtil.array2Set(array2);
		System.out.println(set.toString());

	}

	@Test
	public void collections2List() {
		List<Person> list3 = new ArrayList<>();
		List<Person> list2 = new ArrayList<>();
		Person p1 = new Person();
		p1.setAge("25");
		p1.setName("zs67");
		Person p2 = new Person();
		p2.setAge("26");
		p2.setName("lisi");
		list3.add(p1);
		list2.add(p2);
		List<Person> ll2 = CollectionUtil.collections2List(list2, list3);
		System.out.println(ll2.toString());

	}

	@Test
	public void listJoin() {
		String[] arr = new String[] { "zhangsan", "hanmeimei" };
		String rt = CollectionUtil.listJoin(Arrays.asList(arr), ";");
		System.out.println(rt);

	}

	@Test
	public void getPropsByKeypre() {
		Properties proper = Conf.copyProperties();
		Map<String, String> map = CollectionUtil.getPropsByKeypre(proper, "thread.pool");
		System.out.println(map.get("thread.pool.coreSize"));

	}

}
