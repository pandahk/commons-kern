package com.snowstone.commons.kern.exception;

/***
 * 如果需要有动态参数则用此配置
 * 
 * @author zhoujunhui
 *
 */
public interface IDynaMsg {
	/***
	 * 把动态的参数模板用对象去填充
	 * 
	 * @param msg
	 *            模板化的提示信息
	 * @param ctx
	 *            要充值的对象
	 * @return 充值好的提示信息
	 */
	public String packMsg(String msg, Object ctx);
}
