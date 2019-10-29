package com.overcloud.pay.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.response.ResCode;
import com.overcloud.pay.common.response.ResView;
import com.overcloud.pay.entity.SpProduct;
import com.overcloud.pay.service.HallPayService;

@RestController
@RequestMapping("/hall")
public class HallPayController {

	@Resource
	HallPayService hallPayService;

	@RequestMapping("/getPayList")
	public void getException(HttpServletResponse response, HttpServletRequest request) {

		// 获取大厅充值选项列表
        
		List<SpProduct> list = hallPayService.selectlist();
		System.out.println("于"+new Date()+"回调此接口");
		ResView responseView = null;
		responseView= new ResView(ResCode.AJAX_SUCCESS,list);
		RequestUtil.ajaxResponse(response, responseView);

	}
}
