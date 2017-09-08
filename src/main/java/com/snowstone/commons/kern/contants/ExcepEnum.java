package com.snowstone.commons.kern.contants;


public enum ExcepEnum {
	//系统
    SYSTEM_ERROR("999","系统错误,请联系管理员");

    private String code;
    private String msg;

    ExcepEnum(String code, String msg) {
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
}
