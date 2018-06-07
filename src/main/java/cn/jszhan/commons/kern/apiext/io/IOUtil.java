package cn.jszhan.commons.kern.apiext.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jszhan.commons.kern.Conf;


/***
 * 文件加载、操作等辅助类
 * 
 * @author andy.zhou
 *
 */
public abstract class IOUtil {
	private static Logger logger = LoggerFactory.getLogger(IOUtil.class);
	/**
	 * 转换输入流为字符串
	 * 
	 * @param in
	 *            输入流
	 * @return java.lang.String 转换后的字符串
	 * @throws IOException
	 *             转换出错
	 */
	public static String slurp(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * 属性文件转为属性
	 * 
	 * @param filePath
	 *            文件路径
	 * @param classz
	 *            要加载属性文件同jar包的类
	 * @return Properties 属性对象
	 */
	@SuppressWarnings("rawtypes")
	public static Properties fileToProperties(String filePath, Class classz) {
		Properties returnPro = new Properties();
		InputStream inputFile = null;
		try {
			inputFile = fileToInputStream(filePath, classz);
			returnPro.load(inputFile);
		} catch (FileNotFoundException e) {
			logger.error("找不到文件{}", filePath);
		} catch (IOException e) {
			logger.error("读取属性文件{}错误", filePath);
		} finally {
			if (inputFile != null) {
				try {
					inputFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnPro;
	}

	/****
	 * 通过class加载文件
	 * 
	 * @param filePath
	 *            文件所在路径
	 * @param classz
	 *            class
	 * @return
	 */
	public static InputStream fileToInputStream(String filePath, Class classz) {
		InputStream inputFile = null;
		if (classz != null) {
			inputFile = classz.getResourceAsStream(filePath);//  /commons.properties
		} else {
			inputFile = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(filePath);//  commons.properties
		}
		return inputFile;
	}

	/***
	 * classpath的属性文件转为属性
	 * 
	 * @param filePath
	 * @return
	 */
	public static Properties fileToProperties(String filePath) {
		return fileToProperties(filePath, null);
	}

	/**
	 * 合并目录与文件名
	 * 
	 * @param folderPath
	 *            目录路径
	 * @param fileName
	 *            文件名
	 * @return String 合并后的文件路径
	 */
	public static String mergeFolderAndFilePath(String folderPath, String fileName) {
		if (StringUtils.isBlank(folderPath)) {
			return fileName;
		}
		if (StringUtils.isBlank(fileName)) {
			return folderPath;
		}
		if (!folderPath.endsWith(File.separator) && !folderPath.endsWith("/")) {
			return folderPath + File.separator + fileName;
		} else {
			return folderPath + fileName;
		}
	}

	/***
	 * 得到指定Class下的文件的目录
	 * 
	 * @param classStr
	 *            指定的class
	 * @param filePath
	 *            文件的相对路径
	 * @return 目录
	 */
	@SuppressWarnings("rawtypes")
	public static String getDirForFilePath(Class classStr, String filePath) {
		URL url = classStr.getResource(filePath);
		int lastIndex = url.getPath().lastIndexOf("/");
		if (lastIndex > 0) {
			return url.getPath().substring(0, lastIndex);
		}
		return null;
	}

	/***
	 * 得到此项目下的文件目录路径
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 真实路径
	 */
	public static String getDirForCommonUtilFilePath(String filePath) {
		return getDirForFilePath(IOUtil.class, filePath);
	}

	/***
	 * 把InputStream复制到OutputStream
	 * 
	 * @param from
	 *            输入流
	 * @param to
	 *            输出流
	 * @return 流字节数
	 * @throws IOException
	 *             操作异常
	 */
	public static long copyInToOut(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[1024];
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}

}
