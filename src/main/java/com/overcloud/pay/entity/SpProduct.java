package com.overcloud.pay.entity;


public class SpProduct {

	 private String prodId;  //产品编号
	 private String prodName;  //产品名称
	 private String spId;   //提供商编号
	 private String pfId;   //平台编号
	 public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getSpId() {
		return spId;
	}
	public void setSpId(String spId) {
		this.spId = spId;
	}
	public String getPfId() {
		return pfId;
	}
	public void setPfId(String pfId) {
		this.pfId = pfId;
	}
	public String getBossId() {
		return bossId;
	}
	public void setBossId(String bossId) {
		this.bossId = bossId;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	
	public String getProdInfo() {
		return prodInfo;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public void setProdInfo(String prodInfo) {
		this.prodInfo = prodInfo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	private String bossId;   //能力平台编号
	 private String prodType;  //计费类型 1单点 2包月
	 private Integer price;   //金额
	 private String prodInfo; //Boss给的计费信息
	 private String remark; //备注说明
	  
	  
}
