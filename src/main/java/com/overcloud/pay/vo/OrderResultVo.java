package com.overcloud.pay.vo;

/**
 * 返回下订单结果
 * 
 * @since 2015-11-27 下午4:59:06
 * @version 1.0
 * @author JPF
 */
public class OrderResultVo {
    private String resultCode;
    private String resultDesc;
    private String sign;
    private String orderNo;//中九流水号
    private String orderId;//支付方流水号
    private String picUrl;




    public String getPicUrl() {
        return this.picUrl;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public String getSign() {
        return this.sign;
    }



    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


}
