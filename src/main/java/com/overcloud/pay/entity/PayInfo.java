package com.overcloud.pay.entity;

import java.util.Date;

public class PayInfo {

    private Long id;
    private String channel;
    private String mac;
    private String partnerId;
    private String productId;
    private String userId;
    private String subject;
    private String body;
    private Double total_fee;
    private String orderId;
    private String tradeNo;
    private Date createDateTime;
    private Date modifyDateTime;
    private String tradeStatus;
    private String gmtPayment;
    private String payType;
    private Integer timeOut;
    private Date outDateTime;
    private Integer send = 0;

    private String partnerOrderId;

    private String notifyUrl;

    public String getBody() {
        return this.body;
    }

    public String getChannel() {
        return this.channel;
    }

    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public String getGmtPayment() {
        return this.gmtPayment;
    }

    public Long getId() {
        return this.id;
    }

    public String getMac() {
        return this.mac;
    }

    public Date getModifyDateTime() {
        return this.modifyDateTime;
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Date getOutDateTime() {
        return this.outDateTime;
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public String getPartnerOrderId() {
        return this.partnerOrderId;
    }

    public String getPayType() {
        return this.payType;
    }

    public String getProductId() {
        return this.productId;
    }

    public Integer getSend() {
        return this.send;
    }

    public String getSubject() {
        return this.subject;
    }

    public Integer getTimeOut() {
        return this.timeOut;
    }

    public Double getTotal_fee() {
        return this.total_fee;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public String getTradeStatus() {
        return this.tradeStatus;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOutDateTime(Date outDateTime) {
        this.outDateTime = outDateTime;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public void setTotal_fee(Double total_fee) {
        this.total_fee = total_fee;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
