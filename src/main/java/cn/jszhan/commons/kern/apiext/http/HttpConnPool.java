package cn.jszhan.commons.kern.apiext.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import cn.jszhan.commons.kern.Conf;


public class HttpConnPool {
	private static Object lockobj = new Object();
	private static volatile HttpConnPool INSTANCE;
	private PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	private RequestConfig requestConfig;
	private CloseableHttpClient defaultClient;
	

	/***
	 * 双重检查
	 * 
	 * @return
	 */
	public static final HttpConnPool getInstance() {
		if (INSTANCE == null) {
			synchronized (lockobj) {
				if (INSTANCE == null) {
					HttpConnPool tempobj = new HttpConnPool();
					tempobj.cm = new PoolingHttpClientConnectionManager();
					// 将最大连接数增加到200
					tempobj.cm.setMaxTotal(
							Integer.parseInt(Conf.get("common.http.connpool.maxtotal")));
					// 将每个路由基础的连接增加到20
					tempobj.cm.setDefaultMaxPerRoute(
							Integer.parseInt(Conf.get("common.http.connpool.maxperroute")));
					// 默认client端
					tempobj.requestConfig = RequestConfig.custom()
							.setSocketTimeout(Integer.parseInt(Conf.get("common.http.connpool.defaultconn.sockettimeout")))
							.setConnectTimeout(Integer.parseInt(Conf.get("common.http.connpool.defaultconn.connecttimeout")))
							.setConnectionRequestTimeout(Integer.parseInt(Conf.get("common.http.connpool.defaultconn.requesttimeout")))
							.setStaleConnectionCheckEnabled(true).build();
					tempobj.defaultClient = HttpClients.custom().setDefaultRequestConfig(tempobj.requestConfig)
							.setConnectionManager(tempobj.cm).build();
					INSTANCE = tempobj;
				}
			}
		}
		return INSTANCE;
	}
	
	
	public CloseableHttpClient newSSLClient(TrustStrategy trustStrategy){
		if(trustStrategy==null){
			trustStrategy=new TrustStrategy() {
	            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {//信任所有
	                return true;
	            }
	        };
		}
		SSLContext sslContext;
		try {
			sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(null,trustStrategy).build();
		} catch (Exception e) {
			throw new IllegalArgumentException("创建  SSLContext异常");
		} 
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		HttpClientBuilder build= HttpClients.custom().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslsf);
		return build.build();
	}
	
	public CloseableHttpClient newSSLClient(){
		return newSSLClient(null);
	}
	

	/***
	 * 设置目录机的最大连接数
	 * 
	 * @param host
	 * @param port
	 * @param maxnum
	 */
	public void setMaxPerRoute(String host, int port, int maxnum) {
		HttpHost localhost = new HttpHost(host, port);
		cm.setMaxPerRoute(new HttpRoute(localhost), maxnum);
	}

	/***
	 * 得到默认的客户端，不需要关闭
	 * 
	 * @return
	 */
	public CloseableHttpClient getClient() {
		if (defaultClient == null) {
			RequestConfig reqconfig = RequestConfig.custom().setSocketTimeout(5000)
					.setConnectTimeout(Integer.parseInt(Conf.get("common.http.connpool.defaultconn.connecttimeout")))
					.setConnectionRequestTimeout(Integer.parseInt(Conf.get("common.http.connpool.defaultconn.requesttimeout")))
					.setStaleConnectionCheckEnabled(true).build();
			defaultClient = HttpClients.custom().setDefaultRequestConfig(reqconfig).setConnectionManager(cm).build();
		}
		return defaultClient;
	}

	/***
	 * 得到新的客户端，需要客户自行关闭
	 * 
	 * @return
	 */
	public CloseableHttpClient newHttpClient() {
		RequestConfig reqconfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(reqconfig)
				.setConnectionManager(cm).build();
		return httpClient;
	}

}
