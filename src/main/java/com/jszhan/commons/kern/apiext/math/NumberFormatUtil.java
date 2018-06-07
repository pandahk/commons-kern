package com.jszhan.commons.kern.apiext.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

//用于生成首期/非首期债权转让及受让协议
public class NumberFormatUtil {

	public final static String ZHEN = "整";
	static String[] nums = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	static String ts[] = new String[] { "", "万", "亿" };
	static String[] mus = new String[] { "元", "角", "分" };
	static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟" };
	static BigDecimal HUNDRED = new BigDecimal("100");
	static DecimalFormat ndf = new DecimalFormat();;
	static {
		ndf.setRoundingMode(RoundingMode.HALF_UP);
	}

	/**
	 * 将传入的BigDecimal表示成大写金额，所接受参数整数位数不得超过12 位，程序自动四舍五入保留两位小数后开始翻译。
	 * 
	 * @throws RuntimeException
	 */
	public static String getAmtInWords(BigDecimal d1) {
		try {
			Double d = d1.doubleValue();
			String symbol = "";
			if (d < 0)
				symbol = "-";
			String amtStr = complete(String.valueOf((long) (Math.abs(new BigDecimal(format(d, 2).replaceAll(",", "")).multiply(HUNDRED).doubleValue()))));
			if (amtStr.length() > 14)
				throw new RuntimeException("金额转换为大写金额时最大单位不得超过千亿");
			String yuanStr = amtStr.substring(0, 12);
			char jiaoChar = amtStr.charAt(12);
			char fenChar = amtStr.charAt(13);

			String head = "";
			String tail = "";
			if (jiaoChar + fenChar - '0' - '0' == 0)
				tail = ZHEN;
			else if (jiaoChar - '0' == 0) {
				tail = nums[0] + nums[fenChar - '0'] + mus[2];
			} else if (fenChar - '0' == 0) {
				tail = nums[jiaoChar - '0'] + mus[1] + ZHEN;
			} else {
				tail = nums[jiaoChar - '0'] + mus[1] + nums[fenChar - '0'] + mus[2];
			}

			for (int i = 2; i >= 0; i--) {
				String s = "";
				for (int j = 3; j >= 0; j--) {
					int tmp = yuanStr.charAt(11 - i * 4 - j) - '0';
					if (tmp != 0) {
						s = s + nums[tmp] + HanDiviStr[j];
					} else {
						s = s + nums[0];
					}
				}
				s = s.replaceAll("零+", "零");
				s = s.replaceAll("零+\\b", "");
				// if (s.equals("")) s = "零";

				if (!s.equals("")) {
					head += s + ts[i];
				}
			}

			head = head.replaceAll("\\b零+", "");
			head = head.replaceAll("零+\\b", "");
			head = head.replaceAll("零+", "零");
			if (head.equals(""))
				head = "零";

			return symbol + head + mus[0] + tail;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 如果传入的str长度小于14（为null时直接返回14个0）则在前面补0补到14位，若长度大于等于14则返回传入的数值
	 */
	private static String complete(String s) {
		if (s == null)
			return "00000000000000";
		int len = s.length();
		int tmp = 14 - len;
		for (int i = 0; i < tmp; i++) {
			s = "0" + s;
		}
		return s;
	}

	public static String format(double d, int num) {
		ndf.setMaximumFractionDigits(num);
		ndf.setMinimumFractionDigits(num);
		return ndf.format(d);

	}

}
