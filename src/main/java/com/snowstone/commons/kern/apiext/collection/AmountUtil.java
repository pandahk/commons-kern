package com.snowstone.commons.kern.apiext.collection;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;

import com.snowstone.commons.kern.apiext.math.Arith;
import com.snowstone.commons.kern.apiext.number.NumberUtil;



/***
 * 字符串辅助类
 * 
 * @author vshcxl
 *
 */
public abstract class AmountUtil {


	/**
	 * 把分转为元
	 * 
	 * @param fen
	 *            要转换的分数值
	 * @return String 返回元
	 * 
	 */
	public static String convertFenToYuan(String fen) {
		return convertFenToYuan(Double.parseDouble(fen));
	}

	/**
	 * 把分转换为元
	 * 
	 * @param fen
	 *            要转换的分数值
	 * @return String 返回元
	 */
	public static String convertFenToYuan(double fen) {
		String result;
		try {
			double yuan = fen / 100;
			result = Arith.handleScale(yuan, 2).toString();
		} catch (Exception ex) {
			result = "0.00";
		}
		return result;
	}

	/***
	 * 获取字符串中含数字和字母的个数
	 * 
	 * @param src
	 *            输计算的字符串
	 * @return 计算的个数
	 */
	public static int sumOfNumLet(String src) {
		String figures = "0123456789";
		String letters = "abcdefghijklmnopqrstuvwxyz";
		int sum = 0;
		for (int i = 0; (src != null) && (i < src.length()); i++) {
			char ch = src.charAt(i);
			if ((figures.indexOf(ch) != -1) || (letters.indexOf(ch) != -1))
				sum++;
		}
		return sum;
	}

	/**
	 * 首字母转成大写
	 * 
	 * @param s
	 *            要转换字符串
	 * @return 转换后字符串
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (StringUtils.isBlank(s)) {
			return "";
		} else if (Character.isUpperCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}

	
}