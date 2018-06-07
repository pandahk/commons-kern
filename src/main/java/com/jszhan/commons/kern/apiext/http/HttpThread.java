package com.jszhan.commons.kern.apiext.http;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import com.jszhan.commons.kern.apiext.thread.ThreadPool;


public class HttpThread implements Callable<HttpResult> {

	private final CloseableHttpClient httpClient;
	private final HttpContext context;
	private final HttpRequestBase httpReq;

	public HttpThread(CloseableHttpClient httpClient, HttpRequestBase httpReq, HttpContext context) {
		this.httpClient = httpClient;
		this.context = context;
		this.httpReq = httpReq;
	}

	public HttpThread(CloseableHttpClient httpClient, HttpRequestBase httpReq) {
		this.httpClient = httpClient;
		this.context = HttpClientContext.create();
		this.httpReq = httpReq;
	}

	public HttpThread(HttpRequestBase httpReq) {
		this.httpClient = HttpConnPool.getInstance().getClient();
		this.context = HttpClientContext.create();
		this.httpReq = httpReq;
	}

	@Override
	public HttpResult call() {
		try {
			CloseableHttpResponse response = httpClient.execute(httpReq, context);
			try {
				HttpEntity entity = response.getEntity();
				HttpResult ret = new HttpResult();
				ret.setContext(context);
				ret.setBody(IOUtils.toByteArray(entity.getContent()));
				ret.setContentLength(entity.getContentLength());
				ret.setContentType(entity.getContentType());
				ret.setContentEncoding(entity.getContentEncoding());
				ret.setStatusLine(response.getStatusLine());
				return ret;
			} finally {
				response.close();
			}
		} catch (ClientProtocolException ex) {
		} catch (IOException ex) {
		}
		return null;
	}

	/***
	 * 异步调用，如果不需要返回值可忽略，如果需要返回值用 task.get(); 会阻塞线程
	 */
	public FutureTask<HttpResult> callAsyn() {
		FutureTask<HttpResult> task = new FutureTask<HttpResult>(this);
		ThreadPool.getInstance().submit(task);
		return task;
	}

	public HttpResult callBlock() {
		FutureTask<HttpResult> task = callAsyn();
		HttpResult ret = null;
		try {
			ret = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
