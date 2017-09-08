package com.snowstone.commons.kern.exception;

/***
 * 整个项目的异常基类，errorCode不允许修改且必需是ExceptAll所枚举的异常编码。<br>
 * errorMessage可以修改，如果不修改且是ExceptAll枚举的desc字段。
 * 
 * @author andy.zhou
 *
 */
@SuppressWarnings("serial")
public class ProjectException extends Exception {
	private IExcept except;

	public ProjectException(IExcept except) {
		super(except.getDesc());
		this.except = except;
	}

	public ProjectException(IExcept except, Throwable cause) {
		super(except.getDesc(), cause);
		this.except = except;
	}

	/****
	 * 当有自定义的错误原因时可用此构造函数
	 * 
	 * @param except
	 *            异常
	 * @param errMsg
	 *            错误信息
	 */
	public ProjectException(IExcept except, String errMsg) {
		super(errMsg);
		this.except = except;
	}

	/****
	 * 得到固定格式的异常信息
	 * 
	 * @param errBean
	 *            带参数Bean时要传入的实例
	 * @return 异常信息
	 */
	public String getMessage(Object errBean) {
		String errmsg = null;
		if (errBean == null) {
			errmsg = except.getErrMsg();
		} else {
			errmsg = except.getErrMsg(errBean);
		}
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] es = this.getStackTrace();
		for (int i = 0; i < es.length; i++) {
			sb.append(es[i] + "\n");
		}
		return String.format("错误代码: [code=%s],[value=%s],[message=%s],原始异常产生原因:\n%s", except.getErrorCode(),
				except.getErrorValue(), errmsg, sb.toString());
	}

	/***
	 * 得到异常信息
	 */
	public String getMessage() {
		return getMessage(null);
	}

	/***
	 * 得到异常对象
	 * 
	 * @return 异常对象
	 */
	public IExcept getExcept() {
		return this.except;
	}
}
