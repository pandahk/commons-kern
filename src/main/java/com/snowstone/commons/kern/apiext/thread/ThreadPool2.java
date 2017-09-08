package com.snowstone.commons.kern.apiext.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.snowstone.commons.kern.Conf;


public abstract class ThreadPool2 {

	
	private static final Map<String, ExecutorService>  executorServiceMap=new HashMap<String, ExecutorService>();
	
	private static Properties newProperties = null;

	static {
		Conf.addCallBack("ThreadPool", new Conf.Callback() {
			@Override
			public void doReshConf(Properties newProperties) {
				ThreadPool2.newProperties = newProperties;
				for (String poolname : executorServiceMap.keySet()) {
					executorServiceMap.get(poolname).shutdown();
				}
				executorServiceMap.clear();
			}
		}, "thread.pool.%s");
	}

	/*****
	 * 通过名字得到线程池
	 * 
	 * @param poolname
	 * @return
	 */
	public static final ExecutorService getThreadPoolByName(String poolname, Properties properties) {
		if (StringUtils.isEmpty(poolname)) {
			throw new IllegalArgumentException("需要传入线程池名称");
		}
		synchronized (executorServiceMap) {
			if (executorServiceMap.containsKey(poolname)) {
				return executorServiceMap.get(poolname);
			} else {
				String preStr = "thread.pool." + poolname + ".";
				String poolNameStr = properties.getProperty(preStr + "poolname");
				if (StringUtils.isEmpty(poolNameStr)) {
					throw new IllegalArgumentException("你请求的池没有定义");
				}
				String coreSize = properties.getProperty(preStr + "coreSize");
				String maxSize = properties.getProperty(preStr + "maxSize");
				String queueSize = properties.getProperty(preStr + "queueSize");
				String keepAliveTime = properties.getProperty(preStr + "keepAliveTime");
				String unit = properties.getProperty(preStr + "unit");
				if (StringUtils.isEmpty(coreSize) || StringUtils.isEmpty(maxSize) || StringUtils.isEmpty(queueSize)) {
					throw new IllegalArgumentException("池核心数、最大数、队列数必须定义");
				}
				int coreSizeInt = Integer.parseInt(coreSize);
				int maxSizeInt = Integer.parseInt(maxSize);
				int queueSizeInt = Integer.parseInt(queueSize);
				int keepAliveTimeInt = StringUtils.isEmpty(keepAliveTime) ? 10 : Integer.parseInt(keepAliveTime);
				TimeUnit unitT = StringUtils.isEmpty(unit) ? TimeUnit.SECONDS : getTimeUnit(unit);
				BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSizeInt);
				ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSizeInt, maxSizeInt, keepAliveTimeInt, unitT,
						queue, new RejectedExecution());
				executorServiceMap.put(poolname, executor);
				return executor;
			}
		}
	}

	/***
	 * 得到默认的线程池
	 * 
	 * #线程池说明，corePoolSize：池核心线程大小、maxSize：最大线程大小、queue：等待队列大小、keepAliveTime：
	 * 线程保存时间、unit：单位，默认为秒 #默认线程的配置<br>
	 * thread.pool.default.poolname=default thread.pool.default.coreSize=40
	 * thread.pool.default.maxSize=60 thread.pool.default.queueSize=100
	 * thread.pool.default.keepAliveTime=60 thread.pool.default.unit=SECONDS
	 * 
	 * @return
	 */
	public static final ExecutorService getDefaultPool() {
		if (ThreadPool2.newProperties == null) {
			ThreadPool2.newProperties = Conf.copyProperties();
		}
		return getThreadPoolByName("default", ThreadPool2.newProperties);
	}

	private static TimeUnit getTimeUnit(String TimeUnitStr) {
		if (StringUtils.isEmpty(TimeUnitStr)) {
			return null;
		}
		for (TimeUnit timeUnit : TimeUnit.values()) {
			if (timeUnit.name().equals(TimeUnitStr)) {
				return timeUnit;
			}
		}
		return null;
	}

	
	
	
	
	
	
}
