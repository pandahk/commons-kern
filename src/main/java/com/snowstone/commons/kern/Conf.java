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
	private static long lastModified = 0L;
	private static final Map<String, Callback> reshBacks = new HashMap<>();// 重新加载配置文件时需要的回调函数,key：模块名
	private static final Map<String, String[]> props = new HashMap<>();
	// 默认区域
	private static Locale curLocale = new Locale(get("common.i18n"));

	/***
	 * 配置项需要在属性文件有变化时的回调方法
	 * 
	 * @author xl.c
	 *
	 */
	public static interface Callback {
		public void doReshConf(Properties newProperties);
	}

	static {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				FileInputStream fileInputStream = null;
				try {
					URL jarurl = Thread.currentThread().getContextClassLoader().getResource("/commons.properties");// 绝对路径
					if (jarurl == null) {
						jarurl = Thread.currentThread().getContextClassLoader().getResource("commons.properties");
					}
					String url = jarurl.getPath();
					File file = new File(url);
					long curmodified = file.lastModified();
					if (curmodified > lastModified) {
						lastModified = curmodified;
						Properties oldProperties = (Properties) utilProperties.clone();// 旧配置属性
						Properties newProperties = new Properties();
						fileInputStream = new FileInputStream(file);
						newProperties.load(fileInputStream);
						// 重新装配新的属性
						utilProperties.clear();
						for (Object key : newProperties.keySet()) {
							utilProperties.put(key, newProperties.get(key));
						}
						for (String moudle : reshBacks.keySet()) {
							String[] propNames = props.get(moudle);
							if (ArrayUtils.isEmpty(propNames)) {// 没有观察的属性名称不做调用
								continue;
							}
							// 查找是否观察的属性有变化
							boolean ischange = false;
							for (String propName : propNames) {
								if (propName.endsWith("%s")) {// 取多个属性值，如redisserver%s
									String keyPre = propName.substring(0, propName.length() - 2);
									Map<String, String> oldmap = CollectionUtil.getPropsByKeypre(oldProperties, keyPre);
									Map<String, String> newmap = CollectionUtil.getPropsByKeypre(newProperties, keyPre);
									for (String key : oldmap.keySet()) {
										String oldValue = oldmap.get(key);
										String newValue = newmap.get(key);
										if (!oldValue.equals(newValue)) {
											ischange = true;
											break;
										}

									}
									if (ischange) {
										break;
									}
								} else {
									String oldValue = oldProperties.getProperty(propName);
									String newValue = newProperties.getProperty(propName);
									if (!oldValue.equals(newValue)) {
										ischange = true;
										break;
									}
								}

							}
							if (ischange) {
								try {
									reshBacks.get(moudle).doReshConf(utilProperties);// 也是新的Properties
								} catch (Exception e) {
									logger.error("加载配置文件失败，回调模块[" + moudle + "]错误", e);
								}
							}
						}
						logger.info("成功刷新配置文件");
					}
				} catch (Exception e) {
					logger.error("classpath的根目录下没有commonsUtil.properties文件，将使用commons.jar包的缺少配置。", e);
				} finally {
					if (fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}, 0, 60 * 1000);

	}

	/***
	 * 添加回调方法
	 * 
	 * @param moudle
	 *            模块名
	 * @param callback
	 *            回调类
	 * @param proNames
	 *            关心的属性名
	 */
	public static void addCallBack(String moudle, Callback callback, String... proNames) {
		Validate.isTrue(ArrayUtils.isNotEmpty(proNames));
		props.put(moudle, proNames);
		reshBacks.put(moudle, callback);
	}

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
