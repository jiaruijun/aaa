package com.overcloud.pay.vo;

public class CountDataVO {
	private String createTime;//创建日期
	private String clickRens;//UV
	private String clicks;//PV
	private String dingg;//订购
	private String total_fee;//转化率
	private String productID;//产品编码
	private String productName;//产品名称
	private String cpId;//地区对应的CPID
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getClickRens() {
		return clickRens;
	}
	public void setClickRens(String clickRens) {
		this.clickRens = clickRens;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getDingg() {
		return dingg;
	}
	public void setDingg(String dingg) {
		this.dingg = dingg;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
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
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	
	
	

}
