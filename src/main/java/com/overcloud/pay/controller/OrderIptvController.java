package com.overcloud.pay.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.DBUtil;
import com.overcloud.pay.common.DateTimeUtil;
import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.http.WebParameterUtils;
import com.overcloud.pay.service.OrderIptvService;
import com.overcloud.pay.vo.BossPayInfoVO;
import com.overcloud.pay.vo.IptvBossAuthMonthlyUserVO;
import com.overcloud.pay.vo.IptvCodeExchangeReturnVO;
import com.overcloud.pay.vo.ReturnInfoVO;

@RestController
@RequestMapping("/gsydIptv")
public class OrderIptvController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	 @Resource
	 OrderIptvService service;
	 
	 
	 
	 /**
	     * 甘肃IPTV根据广电长code获取帕克短code
	     * @param request
	     * @return
	     */
	    @RequestMapping(value = "/getPKShortCode")
	    public IptvCodeExchangeReturnVO getPKShortCode(HttpServletResponse response,HttpServletRequest request) {
	    	response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Headers", "Authentication");
	    	this.logger.info("甘肃IPTV根据广电长code获取帕克短code");
	    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
	    	 IptvCodeExchangeReturnVO res = new IptvCodeExchangeReturnVO();
	    	 String ipAddr = RequestUtil.getIpAddr(request);
	    	 params.put("IP", ipAddr);
	    	 DBUtil.setIpParam(params, ipAddr);
	    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
	    	 try {
	    		 res = this.service.getIptvCodeExchange(params);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return res;
	    }
	    
	    
	    /**
	     * 甘肃IPTV根据请求帕科鉴权接口
	     * @param request
	     * @return
	     */
	    @RequestMapping(value = "/iptvBossMonthlyAuth")
	    public IptvBossAuthMonthlyUserVO iptvBossMonthlyAuth(HttpServletResponse response,HttpServletRequest request) {
	    	response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Headers", "Authentication");
	    	this.logger.info("甘肃IPTV根据请求帕科鉴权接口");
	    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
	    	 IptvBossAuthMonthlyUserVO res = new IptvBossAuthMonthlyUserVO();
	    	 String ipAddr = RequestUtil.getIpAddr(request);
	    	 params.put("IP", ipAddr);
	    	 DBUtil.setIpParam(params, ipAddr);
	    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
	    	 try {
	    		 res = this.service.iptvBossMonthlyAuth(params);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return res;
	    }
	    
	    
	    /**
	     * Iptv下单接口
	     * @param request
	     * @return
	     */
	    @RequestMapping(value = "/IptvPayInfo")
	    public BossPayInfoVO IptvPayInfo(HttpServletResponse response,HttpServletRequest request) {
	    	response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Headers", "Authentication");
	    	this.logger.info("广电IPTV下单接口");
	    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
	    	 BossPayInfoVO res = new BossPayInfoVO();
	    	 String ipAddr = RequestUtil.getIpAddr(request);
	    	 params.put("IP", ipAddr);
	    	 DBUtil.setIpParam(params, ipAddr);
	    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
	    	 try {
	    		 res = this.service.iptvPayInfo(params);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return res;
	    }
	    
	    /**
	     * Iptv支付回调通知中九接口
	     * @param request
	     * @return
	     */
	    @RequestMapping(value = "/iptvReturnUrl/{orderId}")
	    public ReturnInfoVO iptvReturnUrl(HttpServletResponse response,HttpServletRequest request,@PathVariable String orderId) {
	    	response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Headers", "Authentication");
	    	this.logger.info("Iptv支付回调通知中九接口");
	    	this.logger.info("Iptv支付回调通知中九接口订单号"+orderId);
	    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
	    	 ReturnInfoVO res = new ReturnInfoVO();
	    	 String ipAddr = RequestUtil.getIpAddr(request);
	    	 params.put("IP", ipAddr);
	    	 DBUtil.setIpParam(params, ipAddr);
	    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
	    	 try {
//	    		 res = this.service.iptvPayInfo(params);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return res;
	    }
	    
	    
	    
	

}
