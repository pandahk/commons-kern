package com.vshcxl.common.collection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.collection.ByteUtils;

public class ByteUtilTest {

	@Test
	public void objectToBytes() {

		Person p = new Person();
		p.setAge("25");
		p.setName("zhangsan");
		byte[] by = null;
		try {
			by = ByteUtils.objectToBytes(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(by);

	}

	@Test
	public void bytesToObject() {
		Person p = new Person();
		p.setAge("25");
		p.setName("zhangsan");
		byte[] by = null;
		try {
			by = ByteUtils.objectToBytes(p);
			Object obj;
			obj = ByteUtils.bytesToObject(by);

			System.out.println(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void toFile() throws IOException {
		Person p = new Person();
		p.setAge("25");
		p.setName("zhangsan");
		byte[] by = null;
		by = ByteUtils.objectToBytes(p);
		ByteUtils.toFile(by, new File("d:/mp.txt"));// 错误用法，不可放入对象转成的二进制
		ByteUtils.toFile(ByteUtils.getBytes(new FileInputStream("d:/a.txt")), new File("d:/mp.txt"));// ok

	}

}
