package com.overcloud.pay.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.overcloud.pay.vo.OrderResultVo;

public interface AliPayService {
	
	 OrderResultVo placeOrder(Map<String, String> params);
	 
	 void verifyCallBack(Map requestParams) throws ClientProtocolException, IOException, NoSuchAlgorithmException;

}
