package com.overcloud.pay.common.response;



public class ResponseView {
	

    /**
     * 返回状态吗
     */
    private int code;

    /**
     * 状态消息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 是否成功
     */
    private boolean success = true;

    public ResponseView(int code, String msg) {
        this.code = code;
        this.message = msg;
        if (code != ResponseCode.AJAX_SUCCESS.getId()) {
            this.success = false;
        }
    }

    public ResponseView(int code, String msg, boolean success, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
        this.success = success;
    }

    public ResponseView(ResponseCode ajaxSuccess) {
        this(ajaxSuccess.getId(), ajaxSuccess.getMsg());
    }
    
    public ResponseView(ResponseCode ajaxSuccess, Object data) {
        this(ajaxSuccess.getId(), ajaxSuccess.getMsg(), true, data);
    }

    public int getCode() {
        return this.code;
    }

    public Object getData() {
        return this.data;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
