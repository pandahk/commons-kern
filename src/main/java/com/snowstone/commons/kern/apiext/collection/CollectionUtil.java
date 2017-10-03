package com.snowstone.commons.kern.apiext.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;

import com.snowstone.commons.kern.Conf;
import com.snowstone.commons.kern.apiext.reflect.ReflectAssist;

public abstract class CollectionUtil {
	

	private static transient final String DEFAULT_SPLIT_STR = ",";

	/**
	 * 数组转List
	 *
	 * @param arr
	 *            an array of T objects.
	 * @param <T>
	 *            a T object.
	 * @return a {@link java.util.List} object.
	 */
	public static final <T> List<T> array2List(T[] arr) {
		if (ArrayUtils.isEmpty(arr)) {
			return null;
		}
		return Arrays.asList(arr);
	}

	/**
	 * 数组转SET
	 *
	 * @param arr
	 *            an array of T objects.
	 * @param <T>
	 *            a T object.
	 * @return a {@link java.util.Set} object.
	 */
	public static final <T> Set<T> array2Set(T[] arr) {
		if (ArrayUtils.isEmpty(arr)) {
			return null;
		}
		return new LinkedHashSet<T>(Arrays.asList(arr));
	}

	/**
	 * 合并集合
	 *
	 * @param collections
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link java.util.List} object.
	 */
	@SuppressWarnings("unchecked")
	public static final <T> List<T> collections2List(Collection<T>... collections) {
		if (ArrayUtils.isEmpty(collections)) {
			return null;
		}

		final List<T> li = new ArrayList<T>();
		for (Collection<T> foo : collections) {
			if (CollectionUtils.isNotEmpty(foo)) {
				li.addAll(foo);
			}
		}
		return li;
	}

	/**
	 * 合并集合
	 *
	 * @param collections
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link java.util.Set} object.
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Set<T> collections2Set(Collection<T>... collections) {
		if (ArrayUtils.isEmpty(collections)) {
			return null;
		}
		final Set<T> set = new LinkedHashSet<T>();
		for (Collection<T> foo : collections) {
			if (CollectionUtils.isNotEmpty(foo)) {
				set.addAll(foo);
			}
		}
		return set;
	}

	/**
	 * 拼接集合字符串
	 *
	 * @param collection
	 *            a {@link java.util.Collection} object.
	 * @param joinStr
	 *            a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String listJoin(List<?> fromList, String joinStr) {
		if (CollectionUtils.isEmpty(fromList))
			return null;
		joinStr = StringUtils.isBlank(joinStr) ? DEFAULT_SPLIT_STR : joinStr;
		StringBuffer toList = new StringBuffer();
		Iterator iterator = fromList.iterator();
		toList.append(String.valueOf(iterator.next()));
		while (iterator.hasNext()) {
			String string = String.valueOf(iterator.next());
			toList.append(joinStr);
			toList.append(string);
		}
		return toList.toString();
	}

	/****
	 * 得到Properties中key以 keyPre+"." 开头的所有属性的集合
	 * 
	 * @param prop
	 *            源属性
	 * @param keyPre
	 *            开始值
	 * @return 满足条件的属性集合
	 */
	public static Map<String, String> getPropsByKeypre(Properties prop, String keyPre) {
		if (prop == null || prop.size() == 0 || StringUtils.isEmpty(keyPre)) {
			return new HashMap<String, String>();
		}
		Set<Object> propKeys = prop.keySet();
		Map<String, String> retMap = new HashMap<String, String>();
		for (Object object : propKeys) {
			String tempStr = String.format("%s.",
					keyPre.endsWith(".") ? keyPre.substring(0, keyPre.length() - 1) : keyPre);
			String key = String.valueOf(object);
			if (key.startsWith(tempStr)) {
				retMap.put(key, prop.getProperty(key));
			}
		}
		return retMap;
	}
	/**
	 * 通过List得到对象的单个列值
	 * 
	 * @param fromList
	 *            要操作的数据源
	 * @param colName
	 *            要提取的列名
	 * @return List 提取预定列的List
	 */
	public static List<?> getColFromObj(List<?> fromList, String colName) {
		List<Object> retList = new ArrayList<Object>();
		if (CollectionUtils.isEmpty(fromList)) {
			return retList;
		}
		for (Object object : fromList) {
			Object result = null;
			if (ReflectAssist.isInterface(object.getClass(), "java.util.Map")) {
				Map tempObjMap = (Map) object;
				result = tempObjMap.get(colName);
			} else {
				result = MVEL.eval(colName, object);
			}
			retList.add(result);
		}
		return retList;
	}
	
}
