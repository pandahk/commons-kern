package com.snowstone.commons.kern.apiext.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snowstone.commons.kern.Conf;
import com.snowstone.commons.kern.exception.ExceptAll;
import com.snowstone.commons.kern.exception.ProjectException;


/***
 * http辅助使用类
 * 
 * @author vshcxl
 *
 */
public abstract class HttpClient {
	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	public static String sendGet(String url) throws ProjectException {
		return sendGet(url, Conf.get("common.encode"));
	}
	/**
	 * 发送Get请求
	 */
	public static String sendGet(String url, String ecode) throws ProjectException {
		String result = null;
		HttpClientBuilder httpClient = HttpClientBuilder.create();
		HttpGet get = new HttpGet(url);
		InputStream in = null;
		try {
			HttpResponse response = httpClient.build().execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				entity = new BufferedHttpEntity(entity);
				in = entity.getContent();
				byte[] read = new byte[1024];
				byte[] all = new byte[0];
				int num;
				while ((num = in.read(read)) > 0) {
					byte[] temp = new byte[all.length + num];
					System.arraycopy(all, 0, temp, 0, all.length);
					System.arraycopy(read, 0, temp, all.length, num);
					all = temp;
				}
				result = new String(all, ecode);
			}
		} catch (Exception e) {
			logger.error("客户端连接错误",e);
			throw new ProjectException(ExceptAll.Project_default);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error("关闭流错误",e);
					throw new ProjectException(ExceptAll.project_streamclose);
				}
			get.abort();
		}
		return result;
	}

	

	/**
	 * 发送带参数的Get请求
	 * 
	 * @param url
	 *            发送请求地址
	 * @param params
	 *            发送请求的参数
	 * @return 返回内容
	 * @throws ProjectException
	 *             请求异常
	 */
	public static String sendGet(String url, Map<String, String> params) throws ProjectException {
		Set<String> keys = params.keySet();
		StringBuilder urlBuilder = new StringBuilder(url + "?");
		for (String key : keys) {
			urlBuilder.append(key).append("=").append(params.get(key)).append("&");
		}
		urlBuilder.delete(urlBuilder.length() - 1, urlBuilder.length());
		return sendGet(urlBuilder.toString());
	}

	/**
	 * 发送post json格式  by.headers
	 * @param url
	 * @param headerValues
	 * @param params
	 * @return
	 * @throws ProjectException
	 */
	public static String sendPostByJson(String url,Map<String,String> headerValues,Map<String, String> params ) throws ProjectException{
		HttpPost post = new HttpPost(url);
		if (!headerValues.isEmpty()&&MapUtils.isNotEmpty(headerValues)) {
			Set<String> keys=headerValues.keySet();
			for (String key : keys) {
				post.addHeader(key, headerValues.get(key));
			}
		}
		return postMethodByJson(params, post);
		
	}

	
	
	/**
	 * @param params
	 * @param post
	 * @return
	 * @throws ProjectException
	 */
	private static String postMethodByJson(Map<String, String> params, HttpPost post) throws ProjectException {
		String result = null;
		HttpClientBuilder httpClient = HttpClientBuilder.create();
		// 创建表单参数列表
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		Set<String> keys = params.keySet();
		for (String key : keys) {
			qparams.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			post.addHeader("Content-type","application/x-www-form-urlencoded; text/html; charset=utf-8");  
			post.setHeader("Accept", "text/html"); 
			// 填充表单
			post.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(params)));

			HttpResponse response = httpClient.build().execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				entity = new BufferedHttpEntity(entity);

				InputStream in = entity.getContent();
				byte[] read = new byte[1024];
				byte[] all = new byte[0];
				int num;
				while ((num = in.read(read)) > 0) {
					byte[] temp = new byte[all.length + num];
					System.arraycopy(all, 0, temp, 0, all.length);
					System.arraycopy(read, 0, temp, all.length, num);
					all = temp;
				}
				result = new String(all, "UTF-8");
				if (null != in) {
					in.close();
				}
			}
			post.abort();

			return result;
		} catch (Exception e) {
			throw new ProjectException(ExceptAll.Project_default);
		}
	}
	
	
	/**
	 * 发送带参数的post请求
	 * 
	 * @param url
	 *            发送请求地址
	 * @param params
	 *            发送请求的参数
	 * @return 返回内容
	 * @throws ProjectException
	 *             请求异常
	 */
	public static String sendPost(String url, Map<String, String> params) throws ProjectException {
		String result = null;
		HttpClientBuilder httpClient = HttpClientBuilder.create();
		HttpPost post = new HttpPost(url);
		// 创建表单参数列表
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		Set<String> keys = params.keySet();
		for (String key : keys) {
			qparams.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			post.addHeader("Content-type","application/x-www-form-urlencoded; text/html; charset=utf-8");  
			post.setHeader("Accept", "text/html"); 
			// 填充表单
			post.setEntity(new UrlEncodedFormEntity(qparams, Conf.get("common.encode")));

			HttpResponse response = httpClient.build().execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				entity = new BufferedHttpEntity(entity);

				InputStream in = entity.getContent();
				byte[] read = new byte[1024];
				byte[] all = new byte[0];
				int num;
				while ((num = in.read(read)) > 0) {
					byte[] temp = new byte[all.length + num];
					System.arraycopy(all, 0, temp, 0, all.length);
					System.arraycopy(read, 0, temp, all.length, num);
					all = temp;
				}
				result = new String(all, "UTF-8");
				if (null != in) {
					in.close();
				}
			}
			post.abort();

			return result;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

	}
}
