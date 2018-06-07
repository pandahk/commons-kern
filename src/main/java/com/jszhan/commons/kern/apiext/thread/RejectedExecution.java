package com.jszhan.commons.kern.apiext.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jszhan.commons.kern.apiext.reflect.ReflectAssist;


/****
 * 当线程被拒绝时采取的策略，如果线程实现cn.rjzjh.commons.util.thread.ICancelHandle接口则调用
 * 
 * @author Administrator
 * 
 */
public class RejectedExecution implements RejectedExecutionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		logger.error(new  Thread(r).getName()+"    线程池拒绝，" + "TaskCount:" + executor.getTaskCount() + " ActiveCount:" + executor.getActiveCount()
				+ " CorePoolSize:" + executor.getCorePoolSize());

		if (!executor.isShutdown()) {
			if (ReflectAssist.isInterface(r.getClass(), "com.snowstone.commons.kern.apiext.thread.ICancelHandle")) {
				ICancelHandle cancelDo = (ICancelHandle) r;
				cancelDo.doCancle();
			}
		}

	}

}
