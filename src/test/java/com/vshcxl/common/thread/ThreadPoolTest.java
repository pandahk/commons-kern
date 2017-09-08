package com.vshcxl.common.thread;

import org.junit.Test;

import com.snowstone.commons.kern.apiext.thread.ThreadPool;

public class ThreadPoolTest {

	@Test
	public void runTest() {
		for (int i = 0; i < 15; i++) {
			final int j = i;
			ThreadPool.getInstance().submit(new Runnable() {
				public void run() {
//					Thread.currentThread().setName(" t" + j);
					System.out.println("线程:" + j + " " + Thread.currentThread().getName() + "  开始执行");
					try {
						Thread.sleep(3000L);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("线程:" + j + Thread.currentThread().getName() + " 执行完毕");
				}
			});

		}
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
