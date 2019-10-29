package com.overcloud.pay.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.DBUtil;
import com.overcloud.pay.common.DateTimeUtil;
import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.http.WebParameterUtils;
import com.overcloud.pay.service.CountDataService;
import com.overcloud.pay.vo.AuthMonthlyUserVO;

@RestController
@RequestMapping("/count")
public class CountDataController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CountDataService service;
	
    /**
     * 查询该系统下的PVUV等统计数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectCountData")
    public void UserAndMonthlyAuth(HttpServletRequest request) {
    	this.logger.info("查询该系统下的PVUV等统计数据");
    	 Map<String, String>  params = WebParameterUtils.getParameterMap(request);
    	 AuthMonthlyUserVO res = new AuthMonthlyUserVO();
    	 String ipAddr = RequestUtil.getIpAddr(request);
    	 params.put("IP", ipAddr);
    	 DBUtil.setIpParam(params, ipAddr);
    	 params.put("createDateTime", DateTimeUtil.formatDateOrTime(DateTimeUtil.TIME_STANDARDPATTERN));
    	 try {
    		 this.service.selectCountData();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

}
