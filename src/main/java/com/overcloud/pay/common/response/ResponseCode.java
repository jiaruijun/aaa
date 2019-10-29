package com.overcloud.pay.common.response;

public enum ResponseCode {

    AJAX_SUCCESS(1, "操作成功"),

    AJAX_FAIL(2, "操作失败"),

    AJAX_EXCEPTION(3, "系统异常"),

    AJAX_NOTFOUND(4, "没有对应资源"),

    AJAX_PARAM_ERR(5, "参数异常"),

    AJAX_VERTIFY_FAIL(6, "验证失败"),

    AJAX_NEEDLOGIN(7, "请先登录"),

    AJAX_UNAME_ERR(8, "用户名不合法"),

    AJAX_PASS_ERR(9, "密码不合法"),

    AJAX_DUPLICATE_ERR(10, "重复"),

    AJAX_REDIRECT(11, "跳转到URL"),
    
    PASSWD_ERROR(12, "密码错误"),
    
    PAYINOVER_ERROR(13, "充值超额"), 
    
    DEVICE_NOTEXIST(14, "不存在");

    private int id;

    private String msg;

    private ResponseCode(int id, String msg) {
        this.msg = msg;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}