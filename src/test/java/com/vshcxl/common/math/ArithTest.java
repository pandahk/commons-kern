package com.vshcxl.common.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.math.Arith;

public class ArithTest {

	@Test
	public void add(){
		double v1=10.2312;
		double v2=23.4545678;
		double ret=Arith.add(v1, v2);
		System.out.println(ret);
		
	}
	@Test
	public void addDouble(){
		Double[] dou=new Double[]{12.231,13.267};
		double ret=Arith.add(dou);
		System.out.println(ret);
		
	}
	
	
	
	@Test
	public void addBigdecimal(){
		BigDecimal v1=new BigDecimal("10.1234");
		BigDecimal v2=new BigDecimal("16.6734");
		BigDecimal ret=Arith.add(v1,v2);
		System.out.println(ret);
		
	}
	
	@Test
	public void commTest(){
		BigDecimal v1=new BigDecimal("10.12");
		BigDecimal v2=new BigDecimal("16.67");
		BigDecimal ret=Arith.mul(v1, v2);
		System.out.println(ret.setScale(2, RoundingMode.HALF_UP));
		System.out.println("1---------------");
		
		BigDecimal ret2=Arith.div(v1, v2);
		System.out.println(ret2);
		System.out.println("2---------------");
		
		Double v3=new Double("12.33");
		Double v4=new Double("12.78");
		Double ret3=Arith.div(v3, v4);
		System.out.println(ret3);
		System.out.println("3---------------");
		
		
		BigDecimal ret4=Arith.div(v1, v2, 3);
		System.out.println(ret4);
		System.out.println("4---------------");
		
		Double ret5=Arith.div(v3, v4, 4);
		System.out.println(ret5);
		System.out.println("5---------------");
		
		
		BigDecimal ret6=Arith.sub(v2, v1);
		System.out.println(ret6);
		System.out.println("6---------------");
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
