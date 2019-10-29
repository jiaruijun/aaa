package com.overcloud.pay.vo;

import java.util.List;

public class IptvCodeExchangeReturnVO {
	
	private String resultCode;
	private String resultDesc;
	private List<IptvCodeResultReturnVO> result;
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public List<IptvCodeResultReturnVO> getResult() {
		return result;
	}
	public void setResult(List<IptvCodeResultReturnVO> result) {
		this.result = result;
	}
	
	

}
