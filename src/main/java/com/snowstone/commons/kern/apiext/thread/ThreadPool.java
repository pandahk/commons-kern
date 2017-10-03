package com.snowstone.commons.kern.apiext.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snowstone.commons.kern.Conf;
import com.snowstone.commons.kern.contants.ExcepEnum;
import com.snowstone.commons.kern.exception.SystemException;

/**
 * 线程池 单例模式 懒加载 keepAliveTime 当线程池中的线程数量大于
 * corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止
 * 由于使用了LinkedBlockingQueue所以maximumPoolSize
 * 没用(除非用ArrayBlockingQueue或者指定linkedBlockingQueue的长度)
 * 
 * 线程池 可容纳的总线程数=maxSize+queueSize,达到总线程数后,新的请求就被线程池拒绝
 * 线程在基本线程数内,线程的执行顺序是无序的，当请求的线程数大于基本线程后，这些超出的被放进队列中，由于队列是fifo 所以这些线程执行时有序的
 * 
 * @author vshcxl
 *
 */
public abstract class ThreadPool {
	private static Logger logger = LoggerFactory.getLogger(ThreadPool.class);
	private static volatile ThreadPoolExecutor instance;
	private static String pre = "thread.pool";// 前缀
	private static String coreSize;
	private static String maxSize;
	private static String queueSize;
	private static String keepAliveTime;
	private static String unit;
	private static final Object lock = new Object();

	static {
		Map<String, String> poolMap = new ConcurrentHashMap<String, String>();
		poolMap = Conf.getPre(pre);
		coreSize = poolMap.get(pre + ".coreSize");
		maxSize = poolMap.get(pre + ".maxSize");
		queueSize = poolMap.get(pre + ".maxSize");
		keepAliveTime = poolMap.get(pre + ".keepAliveTime");
		unit = poolMap.get(pre + ".unit");

	}

	/**
	 * 双重检查 volatile 多线程可见
	 * 
	 * @return
	 */
	public static ExecutorService getInstance() {
		if (null == instance) {
			synchronized (lock) {
				if (null == instance) {
					try {
						if (StringUtils.isEmpty(coreSize) || StringUtils.isEmpty(maxSize)
								|| StringUtils.isEmpty(queueSize) || StringUtils.isEmpty(keepAliveTime)) {
							throw new IllegalArgumentException("池核心数、最大数、队列数必须定义");
						}
						TimeUnit unitT = StringUtils.isEmpty(unit) ? TimeUnit.SECONDS : ThreadUtil.getTimeUnit(unit);
						ThreadFactory threadFactory = Executors.defaultThreadFactory();
						// 用于被拒绝任务的处理程序，它直接在 execute
						// 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务。
						RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
						LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(
								Integer.parseInt(queueSize));
						instance = new ThreadPoolExecutor(Integer.parseInt(coreSize), // 核心线程数
								Integer.parseInt(maxSize), // 最大线程数
								Long.parseLong(keepAliveTime), // 保持时间
								unitT, // 保持时间对应的单位
								workQueue, threadFactory, // 线程工厂
								new RejectedExecution());// 异常捕获器
						logger.info("线程池初始化成功,coreSize:{},maxSize:{},queueSize:{},keepAliveTime:{},unit:{}", coreSize,
								maxSize, queueSize, keepAliveTime, unit);
					} catch (Throwable e) {
						logger.error("线程池初始化异常", e);
						throw new SystemException(ExcepEnum.SYSTEM_ERROR);
					}
				}
			}
		}
		return instance;
	}

}
