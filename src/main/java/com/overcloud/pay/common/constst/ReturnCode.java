package com.overcloud.pay.common.constst;

/**
 * 返回码定义
 * 
 * @since
 * @version 1.0
 * @author
 */
public enum ReturnCode {

    SUCCESS("0", "操作成功"),
    COMMON_ERR_SERVER("1", "应用异常"),
    COMMON_ERR_DB("2", "数据库异常"),
    COMMON_ERR_PARM("3", "参数异常");

    private String code;

    private String message;

    private ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
