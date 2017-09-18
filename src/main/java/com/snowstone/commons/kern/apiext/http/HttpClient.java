package com.snowstone.commons.kern.apiext.http;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONObject;
import com.snowstone.commons.kern.Conf;
import com.snowstone.commons.kern.apiext.io.IOUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HttpClient {

	public static final String ENCODE = "UTF-8";
	public static final String HEAD_TYPE = "application/json";
	public static final String CONTENT_TYPE = "text/json";

	public static HttpResult doGet(String url) {
		HttpRequestBase rq = new HttpGet(url);
		HttpThread ht = new HttpThread(rq);
		HttpResult result = ht.call();
		return result;
	}

	public static FutureTask<HttpResult> doGetAsyn(String url) {
		HttpRequestBase rq = new HttpGet(url);
		HttpThread ht = new HttpThread(rq);
		return ht.callAsyn();
	}

	public static HttpResult doPost(String url, JSONObject param, Header... headers) {
		HttpThread ht = doPostCommon(url, param.toJSONString(), headers);
		HttpResult result = ht.call();
		return result;
	}

	public static FutureTask<HttpResult> doPostAsyn(String url, JSONObject param, Header... headers) {
		HttpThread ht = doPostCommon(url, param.toJSONString(), headers);
		return ht.callAsyn();
	}

	public static HttpThread doPostCommon(String url, String param, Header... headers) {
		HttpPost rq = new HttpPost(url);
		rq.addHeader(HTTP.CONTENT_TYPE, HEAD_TYPE);
		if (ArrayUtils.isNotEmpty(headers)) {
			for (Header header : headers) {
				rq.addHeader(header);
			}
		}
		StringEntity se = new StringEntity(param, ENCODE);
		se.setContentType(CONTENT_TYPE);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, HEAD_TYPE));
		rq.setEntity(se);
		HttpThread ht = new HttpThread(rq);
		return ht;
	}

	public static HttpThread doPostXml(String url, String param, Header... headers) {
		String headType = "application/xml";
		HttpPost rq = new HttpPost(url);
		rq.addHeader(HTTP.CONTENT_TYPE, headType);
		if (ArrayUtils.isNotEmpty(headers)) {
			for (Header header : headers) {
				rq.addHeader(header);
			}
		}
		if (StringUtils.isEmpty(param)) {
			StringEntity se = new StringEntity(param, ENCODE);
			// se.setContentType(CONTENT_TYPE);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, headType));
			rq.setEntity(se);
		}
		HttpThread ht = new HttpThread(rq);
		return ht;
	}

	/***
	 * 返回真正的URL
	 * 
	 * @param relaUrl
	 * @return
	 */
	public static String packurl(String relaUrl) {
		return IOUtil.mergeFolderAndFilePath(Conf.get("common.http.url.pre"), relaUrl);
	}

	public static void main(String[] args) {
		String url="http://gc.ditu.aliyun.com/geocoding";
		JSONObject jb=new JSONObject();
		jb.put("a", "苏州市");
		HttpResult hr=doPost(url, jb, null);
		System.out.println(hr.getBodyStr());
		
//		String ul="https://suggest.taobao.com/sug?code=utf-8&q=%E5%8D%AB%E8%A1%A3&callback=cb";
//		FutureTask<HttpResult> gethr=doGetAsyn(ul);
//		try {
//			System.out.println(gethr.get().getBodyStr());
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
		
	}

}
