package com.overcloud.pay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.response.ResponseCode;
import com.overcloud.pay.common.response.ResponseView;

@SuppressWarnings("all")
public class BaseController {
	@ExceptionHandler
	public void resolveException(Exception ex, HttpServletResponse response, HttpServletRequest request){
		ex.printStackTrace();
		ResponseView view = new ResponseView(ResponseCode.AJAX_EXCEPTION);
		view.setMessage(ex.getMessage());
		RequestUtil.ajaxResponse(response, view);
	}

}
