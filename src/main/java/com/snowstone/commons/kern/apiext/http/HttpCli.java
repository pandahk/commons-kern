package com.snowstone.commons.kern.apiext.http;

import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snowstone.commons.kern.apiext.auth.BASE64;

public class HttpCli {
	private final static Logger logger = LoggerFactory.getLogger(BASE64.class);

	public static String httpPostWithJSON(String url, String json) {
		// 将JSON进行UTF-8编码,以便传输中文
		HttpPost httpPost = null;
		String result = null;
		try {
			String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);

			String APPLICATION_JSON = "application/json";
			String CONTENT_TYPE_TEXT_JSON = "text/json";
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

			StringEntity se = new StringEntity(json);
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httpPost.setEntity(se);
			CloseableHttpResponse response = httpClient.execute(httpPost);
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
				result = new String(all, "gbk");
				if (null != in) {
					in.close();
				}
			}
		} catch (Exception e) {
			logger.error("发送异常", e);
			throw new RuntimeException(e);
		} finally {
			httpPost.abort();
		}

		return result;
	}

}
