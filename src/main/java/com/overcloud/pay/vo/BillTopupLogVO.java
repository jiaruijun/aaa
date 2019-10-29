package com.overcloud.pay.vo;

public class BillTopupLogVO {
	private String orderNo;
	private String userId;
	private String money;
	private String bossOrderNo;
	private String status;
	private String ipAddress;
	private String statusDesc;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getBossOrderNo() {
		return bossOrderNo;
	}
	public void setBossOrderNo(String bossOrderNo) {
		this.bossOrderNo = bossOrderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	

}
