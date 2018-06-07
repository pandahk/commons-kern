package cn.jszhan.commons.kern.exception;

import cn.jszhan.commons.kern.contants.ExcepEnum;

public class SystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6467529930054874242L;
	private String code;
    private String msg;

    public SystemException() {
    	super("");
    }


    public SystemException(ExcepEnum excepEnum) {
    	super();
        this.code = excepEnum.getCode();
        this.msg = excepEnum.getMsg();
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.code = message;
    }
    public SystemException(String code, String msg) {
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
		return "SystemException [code=" + code + ", msg=" + msg + "]";
	}


	
    
    
}
