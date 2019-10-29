package com.overcloud.pay.vo;

/**
 * 请求支付宝下订单请求参数
 * 
 * @since 2015-11-28 上午10:53:01
 * @version 1.0
 * @author JPF
 */
public class SendVO {

    private String out_trade_no;// 商户订单号，中九生成
    private String total_amount;// 商品金额
    private String subject;// 商品标题
    private String body;// 商品描述

    public String getBody() {
        return this.body;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getTotal_amount() {
        return this.total_amount;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

}
