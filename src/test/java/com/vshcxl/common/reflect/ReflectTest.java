package com.vshcxl.common.reflect;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jszhan.commons.kern.apiext.reflect.ReflectAssist;

public class ReflectTest {

	
	@Test
	public void findGetField(){
		
		try {
			List<String> fields=ReflectAssist.findGetField(Class.forName("com.vshcxl.common.collection.Person"));
			System.out.println(fields);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findGetMethod(){
		
		try {
			List<String> fields=ReflectAssist.findGetMethod(Class.forName("com.vshcxl.common.collection.Person"));
			System.out.println(fields);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void isInterface(){
		
		try {
			
			boolean flag=ReflectAssist.isInterface(Class.forName("com.vshcxl.common.reflect.Demo"), "com.vshcxl.common.reflect.DemoInterfact");
			System.out.println(flag);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
