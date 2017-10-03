package com.snowstone.commons.kern.apiext.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snowstone.commons.kern.Conf;
import com.snowstone.commons.kern.apiext.reflect.ReflectAssist;

public abstract class RedisClient {
	private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
	private static volatile JedisPool jedisPool;// 非切片连接池
	private static int defautlDb = 0;// 默认数据库
	private final static Object lockObj = new Object();
	public final static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();// gson的格式化

	/****
	 * 通过配置得到 Jedis
	 * 
	 * @return Jedis实例
	 */
	public static final Jedis getConnection() {
		if (jedisPool == null) {
			synchronized (lockObj) {
				if (jedisPool == null) {
					String name = "redisserver";
					Map<String, String> confMap = Conf.getPre(name);
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(Integer.parseInt(confMap.get(name + ".maxTotal")));
					config.setMaxIdle(Integer.parseInt(confMap.get(name + ".maxIdle")));
					config.setMaxWaitMillis(Integer.parseInt(confMap.get(name + ".maxWaitMillis")));
					config.setTestOnBorrow(Boolean.parseBoolean(confMap.get(name + ".testOnBorrow")));

					config.setTimeBetweenEvictionRunsMillis(30000);
					config.setNumTestsPerEvictionRun(-1);
					config.setTestWhileIdle(true);
					config.setMinEvictableIdleTimeMillis(60000);
					config.setLifo(true);

					defautlDb = confMap.get(name + ".defaultDb") == null ? 0
							: Integer.parseInt(confMap.get(name + ".defaultDb"));
					String password = confMap.get(name + ".password");
					if (StringUtils.isBlank(password)) {// 有设置密码
						jedisPool = new JedisPool(config, confMap.get(name + ".host"),
								Integer.parseInt(confMap.get(name + ".port")));
					} else {
						jedisPool = new JedisPool(config, confMap.get(name + ".host"),
								Integer.parseInt(confMap.get(name + ".port")),
								Integer.parseInt(confMap.get(name + ".maxIdle")), password);
					}
					logger.info("初始化池成功");
				}
			}
		}
		Jedis jedis = jedisPool.getResource();
		jedis.select(defautlDb);
		return jedis;
	}

	/***
	 * 释放资源
	 *
	 * @param jedis
	 */
	public static final void returnResource(Jedis jedis) {
		try {
			if (jedis != null) {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error("jedis释放连接失败", e);
			throw new RuntimeException(e);
		}
	}

	public static final void set(String key, String value, Integer expire) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			jedis.set(key, value);
			if (null != expire) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("set设值失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}

	}

	public static final String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			logger.error("get取值失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	public static final void set(String key, String value) {
		set(key, value, null);
	}

	/**
	 * incr a=1 ,incr a=2,incr a=3 根据key 做一个计数器
	 * 
	 * @param key
	 * @return 返回增加1后的值(第一次调用就是0+1)
	 */
	public static final long incr(String key) {
		Jedis jedis = null;
		long result = 0l;
		try {
			jedis = getConnection();
			result = jedis.incr(key);
		} catch (Exception e) {
			logger.error("incr失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 为一个已经存在的KEY 设置有效期
	 * 
	 * @param key
	 *            需要设置有效期的key
	 * @param seconds
	 *            有效期时间 秒(推荐写法 1*60 表示1分钟 容易看懂)
	 */
	public static final void expire(String key, int seconds) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			Long isSet = jedis.expire(key, seconds);
			if (isSet != 1l) {
				throw new RuntimeException("未设置有效的key值或key已经存在一个过期时间");
			}
		} catch (Exception e) {
			logger.error("expire失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 用来检测某个Key 还有多少有效时间（秒）
	 * 
	 * @param key
	 *            需要查看有效期的key
	 * @return 正数表示剩余的有效期 -1表示参数已经过期， -2表示没有存在的Key
	 */
	public static final Long ttl(String key) {
		Jedis jedis = null;
		Long seconds = null;
		try {
			jedis = getConnection();
			seconds = jedis.ttl(key);
		} catch (Exception e) {
			logger.error("ttl失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
		return seconds;
	}

	/**
	 * 删除key
	 * 
	 * @param key
	 *            需要删除的key
	 * @return true:删除成功 ,false:其他失败
	 */
	public static final boolean del(String key) {
		Jedis jedis = null;
		Long ret = 0L;
		try {
			jedis = getConnection();
			ret = jedis.del(key);
			if (ret == 1L) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("del失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 想一个List尾部插入缓存内容
	 * 
	 * @param key
	 *            list名字
	 * @param value
	 *            插入的内容
	 */
	public static final void lpushList(String key, List<String> value) {
		lpushArray(key, value.toArray(new String[] {}));
	}

	/**
	 * 想一个List尾部插入缓存内容
	 * 
	 * @param key
	 *            list名字
	 * @param value
	 *            插入的内容
	 */
	public static final void lpushArray(String key, String... value) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			jedis.lpush(key, value);
		} catch (Exception e) {
			logger.error("lpushArray失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/*****
	 * 把对象做为Map存放到Redis
	 * 
	 * @param jedis
	 *            连接的引用，外部传入
	 * @param key
	 *            要放入的key值
	 * @param obj
	 *            要放入的对象
	 * @param expire
	 *            超时时间，单位（秒）
	 */
	public static final <T extends Serializable> void putObjByMap(String key, T obj, Integer expire) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			Map<String, String> inpumap = ReflectAssist.convertMapFromBean(obj);
			jedis.hmset(key, inpumap);
			if (expire != null) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("把对象做为Map存放到Redis失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/***
	 * 把对象做为Map存放到Redis，没有超时时间，永久性放入
	 * 
	 * @param jedis
	 *            连接的引用，外部传入
	 * @param key
	 *            要放入的key值
	 * @param obj
	 *            要放入的对象
	 */
	public static final <T extends Serializable> void putObjByMap(String key, T obj) {
		putObjByMap(key, obj, null);
	}

	/***
	 * 取Map指定列的值
	 * 
	 * @param jedis
	 *            连接的引用，外部传入
	 * @param key
	 *            要放入的key值
	 * @param fields
	 *            要取的列名
	 * @return
	 */
	public static final Map<String, String> getMapByField(String key, String... fields) {
		Jedis jedis = null;
		Map<String, String> retobj = new HashMap<>();
		try {
			jedis = getConnection();
			if (jedis == null || StringUtils.isBlank(key) || ArrayUtils.isEmpty(fields)) {
				return retobj;
			}
			List<String> values = jedis.hmget(key, fields);
			for (String string : values) {
				if (StringUtils.isEmpty(string)) {
					return retobj;
				}
			}
			if (ArrayUtils.isNotEmpty(fields) && CollectionUtils.isNotEmpty(values)) {
				for (int i = 0; i < fields.length; i++) {
					retobj.put(fields[i], values.get(i));
				}
			}
		} catch (Exception e) {
			logger.error("取Map指定列的值失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
		return retobj;
	}

	/**
	 * 获取map中所有列的值
	 * 
	 * @param key
	 * @return
	 */
	public static final Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		Map<String, String> retobj = new HashMap<String, String>();
		try {
			jedis = getConnection();
			if (jedis == null || StringUtils.isEmpty(key)) {
				return retobj;
			}
			Map<String, String> values = jedis.hgetAll(key);
			if (!values.isEmpty() && values.size() > 0) {
				return values;
			}
		} catch (Exception e) {
			logger.error("取Map所有列的值失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
		return retobj;
	}

	/**
	 * @method lpushObjByJson 把object转为json存入list
	 * @return void
	 * @author llq
	 * @date 2016年1月19日 下午3:22:49
	 */
	public static final <T extends Serializable> void lpushObjByJson(String key, T obj, Integer expire) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			String json = gson.toJson(obj);
			jedis.lpush(key, json);
			if (expire != null) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("把object转为json存入list失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * @method lpushObjByJson 把object列表转为json存入list
	 * @param key
	 * @param objList
	 * @param expire
	 */
	public static final <T extends Serializable> void lpushObjListByJson(String key, List<T> objList, Integer expire) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			for (T t : objList) {
				String json = gson.toJson(t);
				jedis.lpush(key, json);
			}
			if (expire != null) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("把对象集合转为json存入redis失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * @method getObjListByJsonValue 把list中的json取出为object的list
	 * @return List<T> 如果key不存在，返回null；如果list为空，返回空list
	 * @author llq
	 * @date 2016年1月14日 上午11:09:39
	 */
	public static final <T extends Serializable> List<T> getObjListByJsonValue(String key, Class clazz) {
		Jedis jedis = null;
		List<T> tList = new ArrayList<>();
		try {
			jedis = getConnection();
			if (!jedis.exists(key)) {
				return null;
			}
			List<String> jsonstrList = jedis.lrange(key, 0, -1);
			for (String jsonstr : jsonstrList) {
				T retobj = (T) gson.fromJson(jsonstr, clazz);
				tList.add(retobj);
			}
		} catch (Exception e) {
			logger.error("把list中的json取出为object的list失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
		return tList;
	}

	/**
	 * @method saddObjByJson 把object转为json存入set
	 * @return void
	 * @author llq
	 * @date 2016年1月19日 下午3:25:09
	 */
	public static final <T extends Serializable> void saddObjByJson(String key, T obj, Integer expire) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			String json = gson.toJson(obj);
			jedis.sadd(key, json);
			if (expire != null) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("把object转为json存入set失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}

	}

	/**
	 * @method getObjSetByJsonValue 把set中的json取出为object的set
	 * @return Set<T>
	 * @author llq
	 * @date 2016年1月19日 下午3:26:00
	 */
	public static final <T extends Serializable> Set<T> getObjSetByJsonValue(String key, Class clazz) {
		Jedis jedis = null;
		Set<T> tSet = new HashSet<>();
		try {
			jedis = getConnection();
			if (!jedis.exists(key)) {
				return null;
			}
			Set<String> jsonstrSet = jedis.smembers(key);

			for (Iterator<String> iterator = jsonstrSet.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				T retobj = (T) gson.fromJson(string, clazz);
				tSet.add(retobj);
			}
		} catch (Exception e) {
			logger.error("把set中的json取出为object的set失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
		return tSet;
	}

	/***
	 * 把对象做为Json存放到Redis
	 * 
	 * @param jedis
	 *            连接的引用，外部传入
	 * @param obj
	 *            要放入的对象
	 * @param key
	 *            要放入的key值
	 * @param expire
	 *            超时时间，单位（秒）
	 */
	public static final <T extends Serializable> void putObjByJson(String key, T obj, Integer expire) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			String json = gson.toJson(obj);
			jedis.set(key, json);
			if (expire != null) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error("把对象做为Json存入失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

	/****
	 * 把对象做为Json存放到Redis
	 * 
	 * @param jedis
	 *            连接的引用，外部传入
	 * @param obj
	 *            要放入的对象
	 * @param key
	 *            要放入的key值
	 */
	public static final <T extends Serializable> void putObjByJson(String key, T obj) {
		putObjByJson(key, obj, null);
	}

	/***
	 * Redis上的值为Json,取对象的值
	 * 
	 * @param clazz
	 *            要返回的对象的类
	 * @param jedis
	 *            连接的引用，外部传入
	 * @param key
	 *            要放入的key值
	 * @return
	 */
	public static final <T extends Serializable> T getObjByJsonValue(String key, Class clazz) {
		Jedis jedis = null;
		try {
			jedis = getConnection();
			String jsonstr = jedis.get(String.valueOf(key));
			T retobj = (T) gson.fromJson(jsonstr, clazz);
			return retobj;
		} catch (Exception e) {
			logger.error("取出json对象失败", e);
			throw new RuntimeException(e);
		} finally {
			returnResource(jedis);
		}
	}

}
