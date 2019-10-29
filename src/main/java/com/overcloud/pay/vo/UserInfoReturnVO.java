package com.overcloud.pay.vo;

public class UserInfoReturnVO {
	
	private String code;
	
	private String msg;
	
	private String custInfo;
	
	private String isMonthly; // 0包月  1 没有包月
	private String status;//0 正常 1不正常

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCustInfo() {
		return custInfo;
	}

	public void setCustInfo(String custInfo) {
		this.custInfo = custInfo;
	}

	public String getIsMonthly() {
		return isMonthly;
	}

	public void setIsMonthly(String isMonthly) {
		this.isMonthly = isMonthly;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
