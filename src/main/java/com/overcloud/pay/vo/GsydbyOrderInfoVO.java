package com.overcloud.pay.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GsydbyOrderInfoVO {
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String orderNo;
	private String orderId;
	private String createTime;
	private String total_fee;
	
	private String userId;
	private String productID;
	private String status;
	private String uploadFlag;
	private String ipAddress;
	private String endTime;
	private String updateTime;
	private String isRenew;
	private String productName;
	private String monthlyName;
	private String monthlyNo;
	private String catvid;
	private String customID;
	
	private String phone;
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
	public String getCreateTime() {
		if (this.createTime != null) {
            try {
            	createTime= this.sdf.format(sdf.parse(this.createTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUploadFlag() {
		return uploadFlag;
	}
	public void setUploadFlag(String uploadFlag) {
		this.uploadFlag = uploadFlag;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getEndTime() {
		if (this.endTime != null) {
            try {
            	endTime= this.sdf.format(sdf.parse(this.endTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getUpdateTime() {
		if (this.updateTime != null) {
            try {
            	updateTime= this.sdf.format(sdf.parse(this.updateTime));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getIsRenew() {
		return isRenew;
	}
	public void setIsRenew(String isRenew) {
		this.isRenew = isRenew;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMonthlyName() {
		return monthlyName;
	}
	public void setMonthlyName(String monthlyName) {
		this.monthlyName = monthlyName;
	}
	public String getMonthlyNo() {
		return monthlyNo;
	}
	public void setMonthlyNo(String monthlyNo) {
		this.monthlyNo = monthlyNo;
	}
	public String getCatvid() {
		return catvid;
	}
	public void setCatvid(String catvid) {
		this.catvid = catvid;
	}
	public String getCustomID() {
		return customID;
	}
	public void setCustomID(String customID) {
		this.customID = customID;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	

}
