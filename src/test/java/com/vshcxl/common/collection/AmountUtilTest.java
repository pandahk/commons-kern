package com.vshcxl.common.collection;

import org.junit.Test;

import com.jszhan.commons.kern.apiext.collection.AmountUtil;

public class AmountUtilTest {

	@Test
	public void convertFenToYuan(){
		String rt=AmountUtil.convertFenToYuan("15.7376");
		String rt2=AmountUtil.convertFenToYuan("1589765");
		System.out.println(rt);
		System.out.println(rt2);
		
	}
	@Test
	public void convertFenToYuanDouble(){
		String rt3=AmountUtil.convertFenToYuan(12.76);
		System.out.println(rt3);
		
	}
	
	@Test
	public void sumOfNumLet(){
		int count=AmountUtil.sumOfNumLet("12456jfowefsf");
		System.out.println(count);
		
	}
	
	@Test
	public void toUpperCaseFirstOne(){
		String ret=AmountUtil.toUpperCaseFirstOne("school");
		System.out.println(ret);
		
	}
	
}
