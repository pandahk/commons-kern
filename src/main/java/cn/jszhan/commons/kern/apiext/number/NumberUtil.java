package cn.jszhan.commons.kern.apiext.number;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jszhan.commons.kern.apiext.http.HttpClient;

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
	 * 整数补位
	 * @param i
	 * @return
	 */
	public String fillNumberFormat(int i) {
		// 得到一个NumberFormat的实例
		NumberFormat nf = NumberFormat.getInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(4);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(4);
		return nf.format(i);
	}
	/**
	 * 整数补位
	 * @param i
	 * @return
	 */
	public String fill(int i){
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		return  String.format("%04d", i);
	}
	

	/**
	 * 得到当前时间产生yyyyMMddHHmmssSSSS格式的长整形
	 * 
	 * @return long 符合条件的结果
	 */
	public synchronized static long proUniqNumByTime() {
		return new Long(new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()));
	}

	public static void main(String[] args) {

		// 待测试数据
		int i = 10;
		// 得到一个NumberFormat的实例
		NumberFormat nf = NumberFormat.getInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(4);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(4);
		// 输出测试语句
		System.out.println(nf.format(i));

		int youNumber = 1;
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		String str = String.format("%04d", youNumber);
		System.out.println(str); // 0001

	}
}
