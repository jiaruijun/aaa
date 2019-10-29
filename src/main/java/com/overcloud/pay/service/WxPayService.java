package com.overcloud.pay.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.overcloud.pay.vo.OrderResultVo;

public interface WxPayService {
	
	  //微信生成二维码
	  OrderResultVo placeOrder(Map<String, String> params);
	  
	  //微信支付成功回调
	  void verifyCallBack(Map requestParams,HttpServletRequest request,HttpServletResponse response) throws Exception;

}
