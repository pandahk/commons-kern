package cn.jszhan.commons.kern.exception;

import cn.jszhan.commons.kern.contants.ExcepEnum;

public class BizException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6695332611000126256L;
	private String code;
    private String msg;

    public BizException() {
    	super("");
    }


    public BizException(ExcepEnum bizEnum) {
    	super(bizEnum.toString());
        this.code = bizEnum.getCode();
        this.msg = bizEnum.getMsg();
    }

    public BizException(String code, String msg) {
    	super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


	@Override
	public String toString() {
		return "BizException [code=" + code + ", msg=" + msg + "]";
	}
    
    
}
