package com.vshcxl.common.auth;

import org.junit.Test;

import com.jszhan.commons.kern.apiext.auth.BASE64;

public class BASE64Test {
	
	@Test
	public void encodeBase64(){
		
		String str="928684283@qq.com";
    	System.out.println("原始值:"+str);
    	String enstr=BASE64.encodeBase64(str.getBytes());
		System.out.println("编码后:"+enstr);
		
	}
	
	@Test
	public void decodeBase64(){
		
		String str="928684283@qq.com";
    	System.out.println("原始值:"+str);
    	String enstr=BASE64.encodeBase64(str.getBytes());
		System.out.println("编码后:"+enstr);
		byte[] destr=BASE64.decodeBase64(enstr);
		 String outputStr = new String(destr); 
		System.out.println("解码后:"+outputStr);
	}
	
	
	
}
