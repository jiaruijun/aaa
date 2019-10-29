package com.overcloud.pay.common.constst;

/**
 * 展现结果集 CODE MSG
 * 
 * @since 2015-12-10 下午12:22:25
 * @version 1.0
 * @author JPF
 */
public class ViewResult {

    private String code;
    private String message;

    public ViewResult(String code, String message) {
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
