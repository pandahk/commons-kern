package cn.jszhan.commons.kern.apiext.http.download;
//package com.snowstone.commons.kern.apiext.http.download;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.concurrent.CountDownLatch;
//
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.snowstone.commons.kern.apiext.auth.BASE64;
//import com.snowstone.commons.kern.apiext.io.IOUtil;
//import com.snowstone.commons.kern.apiext.thread.ThreadPool;
//
//import lombok.extern.slf4j.Slf4j;
//
///***
// * 多线程下载
// * 
// * @author andy.zhou
// *
// */
//@Slf4j
//public class Download {
//	private final String url;
//	private final long fileSize;
//	private final String dirPath;
//	private final String fileName;
//	private final String fileNameRemote;
//	private final String filePath;
//
//	private CloseableHttpClient httpClient;
//	/**
//	 * 
//	 * 每个线程下载的字节数
//	 */
//	private long unitSize = 2 * 1024 * 1024;
//
//	public Download(String url, long fileSize, String dirPath, String fileName)
//			throws IOException {
//		this.url = url;
//		this.fileSize = fileSize < 0 ? getRemoteFileSize(url) : fileSize;
//		this.fileNameRemote = StringUtil.getFileName(url);
//		this.fileName = StringUtil.isNull(fileName) ? this.fileNameRemote
//				: fileName;
//		this.dirPath = dirPath;
//		this.filePath = IOUtil.mergeFolderAndFilePath(dirPath, this.fileName);
//		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//		cm.setMaxTotal(100);
//		this.httpClient = HttpClients.custom().setConnectionManager(cm).build();// HttpConnPool.getInstance().getClient();
//	}
//
//	public Download(String url, String dirPath) throws IOException {
//		this(url, -1, dirPath, null);
//	}
//
//	public String download() throws IOException {
//		IOUtil.createFile(filePath, fileSize);
//		Long threadCount = (fileSize / unitSize)
//				+ (fileSize % unitSize != 0 ? 1 : 0);
//		long offset = 0;
//		CountDownLatch end = new CountDownLatch(threadCount.intValue());
//		if (fileSize <= unitSize) {// 如果远程文件尺寸小于等于unitSize
//			DownloadThread downloadThread = new DownloadThread(url, filePath,
//					offset, fileSize, end, httpClient);
//			ThreadPool.getInstance().submit(downloadThread);
//		} else {// 如果远程文件尺寸大于unitSize
//			for (int i = 1; i < threadCount; i++) {
//				DownloadThread downloadThread = new DownloadThread(url,
//						filePath, offset, unitSize, end, httpClient);
//				ThreadPool.getInstance().submit(downloadThread);
//				offset = offset + unitSize;
//			}
//			if (fileSize % unitSize != 0) {// 如果不能整除，则需要再创建一个线程下载剩余字节
//				DownloadThread downloadThread = new DownloadThread(url,
//						filePath, offset, fileSize - unitSize
//								* (threadCount - 1), end, httpClient);
//				ThreadPool.getInstance().submit(downloadThread);
//			}
//		}
//		try {
//			end.await();
//		} catch (InterruptedException e) {
//			log.error("DownLoadManager exception msg:{}",
//					org.apache.commons.lang3.exception.ExceptionUtils
//							.getStackTrace(e));
//			e.printStackTrace();
//		}
//		log.info("下载完成！{} ", filePath);
//		return filePath;
//	}
//
//	/**
//	 * 
//	 * 获取远程文件尺寸
//	 */
//
//	private long getRemoteFileSize(String remoteFileUrl) throws IOException {
//		long fileSize = 0;
//		HttpURLConnection httpConnection = (HttpURLConnection) new URL(
//				remoteFileUrl).openConnection();
//		httpConnection.setRequestMethod("HEAD");
//		int responseCode = httpConnection.getResponseCode();
//		if (responseCode >= 400) {
//			log.debug("Web服务器响应错误!");
//			return 0;
//		}
//		String sHeader;
//		for (int i = 1;; i++) {
//			sHeader = httpConnection.getHeaderFieldKey(i);
//			if (sHeader != null && sHeader.equals("Content-Length")) {
//				System.out.println("文件大小ContentLength:"
//						+ httpConnection.getContentLength());
//				fileSize = Long.parseLong(httpConnection
//						.getHeaderField(sHeader));
//				break;
//			}
//		}
//		return fileSize;
//	}
//}
