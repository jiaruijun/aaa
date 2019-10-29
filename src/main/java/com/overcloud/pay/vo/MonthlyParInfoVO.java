package com.overcloud.pay.vo;

import java.util.Date;

public class MonthlyParInfoVO {

	
	
	private int id;
	private String productName;
	private String price;
	private Date createDateTime;
	private Date modifyDateTime;
	private String productId;
	private String describe;
	private String powerKey;
	private String isCancel;
	private String customphone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Date getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	public Date getModifyDateTime() {
		return modifyDateTime;
	}
	public void setModifyDateTime(Date modifyDateTime) {
		this.modifyDateTime = modifyDateTime;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getPowerKey() {
		return powerKey;
	}
	public void setPowerKey(String powerKey) {
		this.powerKey = powerKey;
	}
	public String getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}
	public String getCustomphone() {
		return customphone;
	}
	public void setCustomphone(String customphone) {
		this.customphone = customphone;
	}
	
}
