package com.overcloud.pay.vo;

import java.util.Date;

public class MonthlyInfoVO {
	
	//包月成功记录表
		private String orderNo; //中九包月订单流水号
		private String orderId;//BOSS订单号
		private String userId; //用户编号
		private String productID;//产品编号 BOSS分配的价格参数
		private String productName;//产品名称
		private String ipAddress;//IP地址
		private double total_fee;//实际消费金额
		private String isRenew;//是否自动续订 1是 2否
		private Integer status;  //状态 0创建 1成功 2退订
		private Integer uploadFlag;  //帐单上传标识 0未上传 1已上传
		private Date createTime;  //创建时间
		private Date endTime;   //有效时间
		private Date updateTime;  //更新时间
		private String monthlyName;//包月名称
		private String monthlyNo;//包月编号
		private String catvid;//广电号
		private String customID;//客户编号
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
		public String getProductID() {
			return productID;
		}
		public void setProductID(String productID) {
			this.productID = productID;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getIpAddress() {
			return ipAddress;
		}
		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}
		public String getIsRenew() {
			return isRenew;
		}
		public void setIsRenew(String isRenew) {
			this.isRenew = isRenew;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public Integer getUploadFlag() {
			return uploadFlag;
		}
		public void setUploadFlag(Integer uploadFlag) {
			this.uploadFlag = uploadFlag;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Date getEndTime() {
			return endTime;
		}
		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
		public Date getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
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
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
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
		public double getTotal_fee() {
			return total_fee;
		}
		public void setTotal_fee(double total_fee) {
			this.total_fee = total_fee;
		}
		 


}
