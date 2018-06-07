	package com.jszhan.commons.kern.apiext.thread;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

public abstract class ThreadUtil {

	public static TimeUnit getTimeUnit(String TimeUnitStr) {
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
