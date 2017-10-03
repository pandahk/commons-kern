package com.snowstone.commons.kern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snowstone.commons.kern.apiext.collection.CollectionUtil;
import com.snowstone.commons.kern.apiext.collection.AmountUtil;
import com.snowstone.commons.kern.apiext.io.IOUtil;


/****
 * 配置文件会在30秒刷一下是否更新
 * 
 * @author vshcxl
 *
 */
public abstract class Conf {
	private static Logger logger = LoggerFactory.getLogger(Conf.class);
	private static final Properties utilProperties = IOUtil.fileToProperties("/commons.properties", Conf.class);// 属性配置
	// 默认区域
	private static Locale curLocale = new Locale(get("common.i18n"));

	
	/***
	 * 通过key得到对应的值
	 * 
	 * @param key
	 *            key值
	 * @return 对应的值
	 */
	public static String get(String key) {
		return String.valueOf(utilProperties.get(key));
	}

	/***
	 * 得到指定前缀的所有key及他们对应的值
	 * 
	 * @param key
	 *            key的前缀
	 * @return 符合条件的结果集
	 */
	public static Map<String, String> getPre(String key) {
		return CollectionUtil.getPropsByKeypre(utilProperties, key);
	}

	/***
	 * 设置当前的Locale
	 * 
	 * @param curLocale
	 *            要设置的Locale
	 */
	public static void setCurLocale(Locale curLocale) {
		if (curLocale != null) {
			Conf.curLocale = curLocale;
		}
	}

	/***
	 * 得到配置文件的副本，防止配置文件的属性被窜改
	 * 
	 * @return
	 */
	public static Properties copyProperties() {
		return (Properties) utilProperties.clone();
	}

	/***
	 * 得到当前的Locale
	 * 
	 * @return 当前的Locale
	 */
	public static Locale getCurLocale() {
		return curLocale;
	}

}
