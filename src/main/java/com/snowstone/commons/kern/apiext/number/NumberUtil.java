package com.snowstone.commons.kern.apiext.number;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snowstone.commons.kern.apiext.http.HttpClient;


/***
 * 计算类型的处理
 * 
 * @author vshcxl
 *
 */
public abstract class NumberUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String numberChar = "0123456789";
	
	/**
	 * 生成6位随机数
	 * 
	 * @return
	 */
	public static String generateRandomNumber() {
		// 生成6位随机数
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
		}
		return sb.toString();
	}
	
	
	
	/**
	 * 得到当前时间产生yyyyMMddHHmmssSSSS格式的长整形
	 * 
	 * @return long 符合条件的结果
	 */
	public synchronized static long proUniqNumByTime() {
		return new Long(new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()));
	}

	
}
