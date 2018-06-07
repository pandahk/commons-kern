package cn.jszhan.commons.kern.apiext.auth;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 采用base64加密解密
 * 
 * @author cxl
 * @datetime 2014-11-19
 */
public class BASE64 {
	private final static Logger logger = LoggerFactory.getLogger(BASE64.class);
	private static String o;
	private static byte[] c;

	/**
	 * BASE64加密
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64(byte[] pArray) {
		try {

			o = new Base64().encodeAsString(pArray);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("base64加密异常");
		}
		return o;
	}

	/**
	 * BASE64解密
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] decodeBase64(String pArray) {
		try {
			c = new Base64().decode(pArray);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("base64解密异常");
		}
		return c;
	}

}
