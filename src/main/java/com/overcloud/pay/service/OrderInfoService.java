package com.overcloud.pay.service;

import java.util.List;
import java.util.Map;

import com.overcloud.pay.vo.AuthMonthlyUserVO;
import com.overcloud.pay.vo.BodyVO;
import com.overcloud.pay.vo.BossAuthMonthlyUserVO;
import com.overcloud.pay.vo.BossIntegralPayInfoVO;
import com.overcloud.pay.vo.BossIntegralQueryReturnVO;
import com.overcloud.pay.vo.BossPayInfoVO;
import com.overcloud.pay.vo.BossQRCodePayInfoVO;
import com.overcloud.pay.vo.GsydbyOrderInfoVO;
import com.overcloud.pay.vo.MonthlyParInfoVO;
import com.overcloud.pay.vo.OrderReturnVO;
import com.overcloud.pay.vo.TOkenVO;

public interface OrderInfoService {
	
	/**
	 *进入大厅插入用户
	 */
	void insertUser(Map<String, String>  params);
	
	
	/**
	 * 从自有数据库查询该用户是否包月过
	 */
	AuthMonthlyUserVO getAuthInfo(Map<String, String>  params)throws Exception;
	
	
	
	
	//前端请求后台得到用户支付结果
	public OrderReturnVO getMonthOrderInfoCode(Map<String, String> params);
	
	//前端查询用户支付信息
	List<GsydbyOrderInfoVO> getGsydbyBillInfo(Map<String, String> params);

	
	/**
	 * 在boss平台查询该用户是否包月
	 * @param params
	 * @return
	 */
	BossAuthMonthlyUserVO getbossAuthInfo(Map<String, String>  params)throws Exception;
	/**
	 * 在boss新平台查询该用户是否包月
	 * @param params
	 * @return
	 */
	BossAuthMonthlyUserVO getbossNewAuthInfo(Map<String, String>  params)throws Exception;
	
	/**
	 * BOSS 家开请求中九正向订购退订同步接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	BodyVO zte9Forwardorder(Map<String, String>  params)throws Exception;
	

	
	/**
	 * 在boss平台获取用户token
	 * @param params
	 * @return
	 * @throws Exception
	 */
	TOkenVO bossGetToken(Map<String, String>  params);
	
	/**
	 * 前端请求Boss 支付接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	BossPayInfoVO bossPayInfo(Map<String, String>  params)throws Exception;
	
	/**
	 * 前端请求Boss 二维码支付接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	BossQRCodePayInfoVO bossQRCodePayInfo(Map<String, String>  params)throws Exception;
	
	
	/**
	 * BOSS家开请求中九回调二维码支付回调接口
	 */
	void zte9QRCodeReturnOrder(Map<String, String>  params);
	
	/**
	 * 前端请求 Boss退订接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	BossPayInfoVO bossUnsubscribePayInfo(Map<String, String>  params)throws Exception;
	
	//包月参数管理表格
    MonthlyParInfoVO getMonthlyPar();
	List<MonthlyParInfoVO> list();
	
	//查询价格
	String getPrice();
	
	
	/*
	 * 每个月4号凌晨1点15查询上个月订购的数据然后走鉴权查看是否续订
	 */
	void timingOrderAuthInfo()throws Exception;
	
	//前端请求后台得到用户二维码支付结果
	public OrderReturnVO getQRReturnUrlPayStatus(Map<String, String> params);
	
	
	
	/**
	 * 前端请求Boss 积分查询接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	BossIntegralQueryReturnVO bossIntegralQuery(Map<String, String>  params)throws Exception;
	/**
	 * 前端请求Boss 积分支付接口
	 * @param params
	 * @return
	 * @throws Exception
	 */
	BossIntegralPayInfoVO bossIntegralPayInfo(Map<String, String>  params)throws Exception;
}
