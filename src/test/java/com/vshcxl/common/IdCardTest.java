package com.vshcxl.common;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.validate.IdCardUtil;

public class IdCardTest {

	
	@Test
	public void getBirthByIdCard(){
		
		String age=IdCardUtil.getBirthByIdCard("412825198812072935");
		System.out.println(age);
		
	}
	
	@Test
	public void getAgeByIdCard(){
		
		int age=IdCardUtil.getAgeByIdCard("412825199612072935");
		System.out.println(age);
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
