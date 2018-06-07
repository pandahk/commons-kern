package com.vshcxl.common.date;

import java.util.Date;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.time.DateUtils;

public class DateUtilsTest {

	@Test
	public void addDate(){
		Date date=DateUtils.addDate(new Date(), 1);
		System.out.println(date);
	}
	
	
	
}
