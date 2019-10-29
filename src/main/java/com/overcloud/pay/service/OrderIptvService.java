package com.overcloud.pay.service;

import java.util.Map;

import com.overcloud.pay.vo.BossPayInfoVO;
import com.overcloud.pay.vo.IptvBossAuthMonthlyUserVO;
import com.overcloud.pay.vo.IptvCodeExchangeReturnVO;

public interface OrderIptvService {
	
	/**
	 * 拿广电code向帕科换取短code
	 * @param params
	 * @return
	 */
	IptvCodeExchangeReturnVO getIptvCodeExchange(Map<String, String>  params)throws Exception;
	
	
	/**
	 * IPTV 请求帕科鉴权接口
	 */
	
	IptvBossAuthMonthlyUserVO iptvBossMonthlyAuth(Map<String, String>  params)throws Exception;
	
	/**
	 * 广电IPtv下单接口
	 */
	
	BossPayInfoVO iptvPayInfo(Map<String, String>  params)throws Exception;
	

}
