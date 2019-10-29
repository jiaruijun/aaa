package com.overcloud.pay.vo;

public class SendResVO {

    private String code;
    private String msg;
    private String out_trade_no;// 商户请求的订单号
    private String qr_code;// 二维码串
    private String result;
    private String errorCode;

    private String errorDes;

    public String getCode() {
        return this.code;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDes() {
        return this.errorDes;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public String getQr_code() {
        return this.qr_code;
    }

    public String getResult() {
        return this.result;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorDes(String errorDes) {
        this.errorDes = errorDes;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
