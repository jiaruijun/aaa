package com.overcloud.pay.common.response;



public class ResView {
	

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

  

    public ResView(int code, String msg) {
        this.code = code;
        this.message = msg;
      
    }
    

	public ResView(int code, String msg, boolean success, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
      
    }

    public ResView(ResCode ajaxSuccess) {
        this(ajaxSuccess.getId(), ajaxSuccess.getMsg());
    }
    
    public ResView(ResCode ajaxSuccess, Object data) {
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

    

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

  
}
