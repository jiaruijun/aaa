package com.overcloud.pay.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.http.WebParameterUtils;
import com.overcloud.pay.service.AliPayService;
import com.overcloud.pay.vo.OrderResultVo;


@RestController
@RequestMapping("/gansuAli")
public class AlipayInfoController {
	
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	    @Resource
	    AliPayService payInfoService;
	
	    
	    /**
	     * 预下订单，获取商品二维码
	     * 
	     * @param request
	     * @return
	     */
	    @RequestMapping(value = "/order")
	    public OrderResultVo placeOrder(HttpServletRequest request) {
	        this.logger.debug("获取二维码");
	        Map<String, String> params = WebParameterUtils.getParameterMap(request);
	        OrderResultVo res = this.payInfoService.placeOrder(params);
	        return res;
	    }
	    
	    @RequestMapping(value = "/callback")
	    public void getCallBack(HttpServletResponse response, HttpServletRequest request) {
	        Map requestParams = request.getParameterMap();
	        this.logger.info("开始处理回调信息");
	        try {
	            this.payInfoService.verifyCallBack(requestParams);

	            RequestUtil.ajaxResponse(response, "success");

	        }
	        catch (Exception e) {
	            RequestUtil.ajaxResponse(response, "fail");
	        }

	    }

}
