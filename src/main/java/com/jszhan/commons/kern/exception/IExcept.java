package com.jszhan.commons.kern.exception;

/****
 * 自定义的异常需要实现的接口
 * 
 * @author zhoujunhui
 *
 */
public interface IExcept {
	/****
	 * 得到错误的描述，一般来说提供给开发人员查看或记录到log
	 * 
	 * @return 错误的描述
	 */
	public String getDesc();

	/***
	 * 得到错误值
	 * 
	 * @return 错误值
	 */
	public int getErrorValue();

	/***
	 * 得到错误编码
	 * 
	 * @return 错误编码
	 */
	public String getErrorCode();

	/***
	 * 得到错误的国际化提示信息可以提供给客户查看
	 * 
	 * @param errBean
	 *            错误信息参数
	 * @return 国际化提示信息
	 */
	public String getErrMsg(Object errBean);

	/****
	 * 返回错误信息
	 * 
	 * @return 错误信息
	 */
	public String getErrMsg();

}
