package com.overcloud.pay.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.http.RequestUtil;
import com.overcloud.pay.common.http.WebParameterUtils;
import com.overcloud.pay.common.response.ResponseCode;
import com.overcloud.pay.common.response.ResponseView;
import com.overcloud.pay.service.UserRecordService;
import com.overcloud.pay.vo.UserActionVo;

@RestController	
@RequestMapping("/user")
public class UserRecordController {
	
private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private UserRecordService  service;
	
	
    /**
	 * 获取信息
	 * @param request
	 * @param response
	 * @return 
	 */
	@ResponseBody
    @RequestMapping(value = "/action")
    public void getUserAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseView view = new ResponseView(ResponseCode.AJAX_SUCCESS);
    	Map<String, String> paramMap = WebParameterUtils.getParameterMap(request);
    	String ipAddr = RequestUtil.getIpAddr(request);
    	this.logger.info("请求的ip是"+ipAddr);
    	this.logger.info("甘肃移动大厅请求action接口");
    	paramMap.put("ipAddr", ipAddr);
    	UserActionVo userVo	=this.service.getUserAction(paramMap);
           
    //	UserActionVo userVo=   new com.overcloud.pay.entity.UserActionVo();
    	//userVo.setAction("99");
     	userVo.setUserId(paramMap.get("userId"));
        view.setData(userVo);
  
        RequestUtil.ajaxResponse(response, view);
    }
}
