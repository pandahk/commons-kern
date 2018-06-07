package com.jszhan.commons.kern.apiext.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@SuppressWarnings("all")
public abstract class ReflectAssist {
	public static Logger logger = LoggerFactory.getLogger(ReflectAssist.class);

	private static String[] excludeGet = new String[] { "getClass" };

	public static Object invokeStaticMethed(String className, String methodName, Class[] paramclass, Object... param)
			throws Exception {
		Class c = Class.forName(className);
		Method m = c.getMethod(methodName, paramclass);
		Object retobj = m.invoke(c, param);
		return retobj;
	}

	/****
	 * 用简单参数调用静态方法
	 * 
	 * @param className
	 *            要调用的静态方法所在的类名
	 * @param methodName
	 *            静态方法名
	 * @param param
	 *            调用的参数
	 * @return
	 * @throws Exception
	 *             调用时错误
	 */
	public static Object invokeStaticMethed(String className, String methodName, Object... param) throws Exception {
		Class[] paramClass = null;
		if (!org.apache.commons.lang3.ArrayUtils.isEmpty(param)) {
			paramClass = new Class[param.length];
			for (int i = 0; i < param.length; i++) {
				paramClass[i] = param.getClass();
			}
		}
		return invokeStaticMethed(className, methodName, paramClass, param);
	}

	/***
	 * 调用对象中的方法
	 * 
	 * @param invokeObj
	 *            方法所在的对象
	 * @param methodName
	 *            方法名
	 * @param param
	 *            调用的参数
	 * @return 调用方法返回的结果
	 */
	public static Object invokeMothed(Object invokeObj, String methodName, Object... param) {
		Class c = invokeObj.getClass();
		if (StringUtils.isEmpty(methodName)) {
			logger.error("反射中缺少方法");
			return null;
		}
		Method[] m = c.getMethods();// .getMethod(methodName,ptypes);
		Method exeMethod = null;
		for (int i = 0; i < m.length; i++) {
			Method tempMethod = m[i];
			if (!methodName.equals(tempMethod.getName())) {// 方法名不相等
				continue;
			}
			Class[] classAry = tempMethod.getParameterTypes();
			if (classAry.length != param.length) {// 方法的参数不匹配
				continue;
			}
			if ((param == null && classAry == null) || (classAry.length == 0 && param.length == 0)) {// 无参数调用
				exeMethod = tempMethod;
				break;
			}

			boolean isthisMethod = true;
			for (int j = 1; j < classAry.length; j++) {
				Class classAryEle = classAry[j];
				Object paramEle = param[j];
				if (classAryEle.isArray() && paramEle.getClass().isArray()) {// TODO
																				// 参数是数组的如何查询它的元类型？？？？？
					try {
						Object[] paramArry = (Object[]) param;
						Object[] classInstArry = (Object[]) classAryEle.newInstance();
						if (paramArry[0].getClass().isAssignableFrom(classInstArry[0].getClass())) {
							isthisMethod = false;
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (!paramEle.getClass().isArray() && !classAryEle.isArray()) {
					if (!paramEle.getClass().isAssignableFrom(classAryEle)) {
						isthisMethod = false;
						break;
					}
				} else {
					isthisMethod = false;
					break;
				}
			}
			if (isthisMethod) {
				exeMethod = tempMethod;
				break;
			}

		}
		if (exeMethod != null) {
			try {
				return exeMethod.invoke(invokeObj, param);
			} catch (Exception e) {
				logger.error("反射调用方法出错。");
			}
		}
		return null;
	}

	/***
	 * 判断类是否基本数据类型
	 * 
	 * @param clz
	 *            要判断的类
	 * @return 是否基本数据类型 true:是，false:否
	 */
	public static boolean isPrimitieClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

	/***
	 * 找到get方法且没有参数的方法
	 * 
	 * @param clz
	 *            get方法所在的类
	 * @return 所有的get方法的方法名，排除"getClass"方法
	 */
	public static List<String> findGetMethod(Class clz) {
		List<String> methList = new ArrayList<String>();
		Method[] m = clz.getMethods();
		if (m.length == 0) {
			return methList;
		}
		for (int i = 0; i < m.length; i++) {
			Method method = m[i];
			String methodName = method.getName();
			if (!methodName.startsWith("get")) {
				continue;
			}
			if (ArrayUtils.contains(excludeGet, methodName)) {
				continue;
			}
			Class[] classAry = method.getParameterTypes();
			if (classAry.length == 0) {
				methList.add(method.getName());
			}
		}
		return methList;
	}

	/***
	 * 找到get方法对应的域
	 * 
	 * @param clz
	 *            域所在的类
	 * @return 所有的域的名称
	 */
	public static List<String> findGetField(Class clz) {
		List<String> retList = new ArrayList<String>();
		List<String> methodList = findGetMethod(clz);
		for (String methodname : methodList) {
			String ele = methodname.substring(3);
			retList.add(ele.substring(0, 1).toLowerCase() + ele.substring(1));
		}
		return retList;
	}

	

	/***
	 * 把对象转为Map值, 主要用于把对象放到redis中(未测试)
	 * 
	 * @param obj
	 *            要转换的对象
	 * @return 转换后的Map值
	 */
	public static <T extends Serializable> Map<String, String> convertMapFromBean(T obj) {
		Map<String, String> retmap = new HashMap<String, String>();
		if (obj == null) {
			return retmap;
		}
		List<String> fields = findGetField(obj.getClass());
		if (CollectionUtils.isNotEmpty(fields)) {
			for (String field : fields) {
				packMap(retmap, null, obj, field);
			}
		}
		return retmap;
	}

	/***
	 * 把对象的某个域设置到map中(未测试)，使用递归的方法找出子对象的值放入map
	 * 
	 * @param map
	 *            要放结果的map
	 * @param oldfield
	 *            父对象的域名称
	 * @param obj
	 *            要操作的对象
	 * @param field
	 *            要操作的域名称
	 */
	private static void packMap(Map<String, String> map, String oldfield, Object obj, String field) {
		String key = StringUtils.isEmpty(oldfield) ? field : String.format("%s.%s", oldfield, field);
		Object fieldObj = null;
		try {
			fieldObj = PropertyUtils.getProperty(obj, field);
		} catch (Exception e) {
		}
		if (fieldObj == null) {
			return;
		}
		if (isPrimitieClass(fieldObj.getClass()) || fieldObj instanceof String || fieldObj instanceof Enum) {
			String value = String.valueOf(fieldObj);
			if (StringUtils.isEmpty(value) && value.startsWith("org.apache.openjpa.enhance")) {// 由jpa生成的对象不放入
				return;
			}
			map.put(key, value);
		} else if (fieldObj instanceof Date) {
//			String datestr = DateUtil.format((Date)fieldObj, DateUtil.YMDHMS_PATTERN);
//			map.put(key, datestr);
		} else {
			// 可能是一个复合对象
			List<String> fields = findGetField(fieldObj.getClass());
			if (CollectionUtils.isNotEmpty(fields)) {
				for (String subFields : fields) {
					packMap(map, key, fieldObj, subFields);
				}
			}
		}
	}

	/***
	 * 判断类是否实现某个接口
	 * 
	 * @param classz
	 *            要判断的类
	 * @param szInterface
	 *            要判断的接口
	 * @return 是否继承了接口，true：是 false：否
	 */
	public static boolean isInterface(Class classz, String szInterface) {
		Class[] face = classz.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(szInterface)) {
				return true;
			} else {
				Class[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != classz.getSuperclass()) {
			return isInterface(classz.getSuperclass(), szInterface);
		}
		return false;
	}
	
}
