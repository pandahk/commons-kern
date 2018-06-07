/**   
 * @Title: ExceptAll.java
 * @Package rjzjh.tech.common.Exception
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 周俊辉  
 * @date 2010-10-29 下午01:26:42
 * @version V1.0   
 */
package cn.jszhan.commons.kern.exception;



/***
 * 自定义异常编码与其解释（测试用）
 * 
 * @author andy.zhou
 *
 */
public enum ExceptAll implements IExcept {
	// 系统通用型错误(包括Java相关错误)
	no("没有异常", 1000),

	project_undefined("未定义异常", 1001),

	project_user("自定义异常", 1002),

	Project_default("系统错误", 1003),

	project_noservice("错误的服务，没有定义的服务", 1004),

	project_clienterror("客户端连接错误", 1005),

	project_streamclose("流关闭错误", 1006),

	project_nonull("不允许为空值", 1007),

	project_overflow("数据值超出许可范围", 1008),

	project_overarry("数据元素越界", 1009),

	project_formatnofit("格式不匹配", 1010),

	project_datenofitformate("时间格式不对", 1011),

	param_error("参数错误", 1012),


	Param_typenofit("参数类型不匹配", 1014),

	Param_lengthover("参数长度越界", 1015),

	conn_nocontrol("输入参数没有控制信息", 1016);

	private String desc;
	private int value;
	private IDynaMsg dynaMsg = null;

	private ExceptAll(String desc, int value) {
		this.desc = desc;
		this.value = value;
	}

	private ExceptAll(String desc, int value, IDynaMsg dynaMsg) {
		this.desc = desc;
		this.value = value;
		this.dynaMsg = dynaMsg;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	@Override
	public int getErrorValue() {
		return this.value;
	}

	@Override
	public String getErrorCode() {
		return this.name();
	}


	@Override
	public String getErrMsg() {
		return getErrMsg(null);
	}

	@Override
	public String getErrMsg(Object errBean) {
		// TODO Auto-generated method stub
		return null;
	}


}
