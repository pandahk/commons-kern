package com.vshcxl.common.HttpClient;

import org.junit.Test;

import com.snowstone.commons.kern.apiext.http.HttpClient;
import com.snowstone.commons.kern.exception.ProjectException;

public class HttpClientTest {

	@Test
	public void sendGet(){
		try {
			String ret=HttpClient.sendGet("http://www.baidu.com");
			System.out.println(ret);
		} catch (ProjectException e) {
			e.printStackTrace();
		}
		
	}
	
}
