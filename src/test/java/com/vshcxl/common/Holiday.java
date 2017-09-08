package com.vshcxl.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Holiday {

	public static void main(String[] args) {
		
		weekendOfYear(2017);
	}


	private static void weekendOfYear(int year) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar calendar = new GregorianCalendar(year, 0, 1);
	    int i = 1;
	    while (calendar.get(Calendar.YEAR) < year + 1) {
	        calendar.set(Calendar.WEEK_OF_YEAR, i++);
	        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	        if (calendar.get(Calendar.YEAR) == year) {
	            // System.out.printf("星期天：%tF%n", calendar);
	            System.out.println( sdf.format(calendar.getTime()));
	        }
	        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	        if (calendar.get(Calendar.YEAR) == year) {
	            // System.out.printf("星期六：%tF%n", calendar);
	            System.out.println(sdf.format(calendar.getTime()));
	        }
	    }
	}
}
