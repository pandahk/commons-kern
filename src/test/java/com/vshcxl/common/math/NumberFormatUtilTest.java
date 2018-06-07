package com.vshcxl.common.math;

import java.math.BigDecimal;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.math.NumberFormatUtil;

public class NumberFormatUtilTest {

	
	@Test
	public void getAmtInWords(){
		String ret=NumberFormatUtil.getAmtInWords(new BigDecimal("12.2367"));
		System.out.println(ret);
		
	}
}
