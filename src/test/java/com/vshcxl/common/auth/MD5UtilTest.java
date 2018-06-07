package com.vshcxl.common.auth;

import org.junit.Test;

import cn.jszhan.commons.kern.apiext.auth.MD5Util;

public class MD5UtilTest {

	@Test
	public void md5Hex() {
		String ret = MD5Util.md5Hex("abc123456");
		System.out.println(ret);
	}
}
