package com.overcloud.pay.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.overcloud.pay.common.DateUtil;
import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.alipay.AlipayNotify;
import com.overcloud.pay.common.qrcode.QRCodeUtil;
import com.overcloud.pay.dao.OrderInfoDao;
import com.overcloud.pay.service.AliPayService;
import com.overcloud.pay.vo.BillMonthLogVO;
import com.overcloud.pay.vo.MonthlyGameCategoryVo;
import com.overcloud.pay.vo.OrderResultVo;
import com.overcloud.pay.vo.SendResVO;
import com.overcloud.pay.vo.SendVO;

@Service
public class AliPayServiceImpl implements AliPayService{
	
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());

	 	@Resource
		OrderInfoDao dao;
		private static long num = 1;
		
		private String getTradeNumber() {
			Date t = new Date();

			String s = DateFormatUtils.format(t, "yyMMddHHmmssSSSS");
			if (num > 999) {
				num = 1;
			}
			Long fl = Long.valueOf(s) * 1000;
			fl += num;
			num++;
			return fl.toString();
		}

	    @Override
	    public OrderResultVo placeOrder(Map<String, String> params) {
	        OrderResultVo resparam = new OrderResultVo();
	       
	        String amounts = params.get("amounts");
	        String billid = this.getTradeNumber();
				SendVO send = new SendVO();
		        send.setOut_trade_no(billid);
		        send.setSubject(SysConfig.getVal("monthName"));
		        send.setTotal_amount(amounts);
		        send.setBody("30天/"+params.get("amounts")+"元");
		        try {
		        	 Map<String, String> params2 =new HashMap<String, String> ();
		            SendResVO sendRes = Alipay.SendMonth(send);
		            String recode = sendRes.getCode();
		            this.logger.info("返回码" + recode);
		            this.logger.info("返回码" + recode);
		            this.logger.info("返回码" + recode);
		            this.logger.info("返回码" + recode);
		            this.logger.info("返回码" + recode);
		           
		            
		            // 生成二维码图片
		            String qrcode_save_dir = SysConfig.getVal("image_absolute_dir")+SysConfig.getVal("qrcode");
		            QRCodeUtil.main(sendRes.getQr_code(), qrcode_save_dir, billid);
		            // 返回信息
					params2.put("status", "0");
			    	params2.put("userId", params.get("userId"));
			    	params2.put("productName", "");
			    	params2.put("total_fee",  amounts);
			    	params2.put("orderNo", billid);
			    	MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
					params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
					params2.put("monthlyNo", monthlyGameCategoryVo.getId());
					params2.put("days", "30");
					params2.put("isPayMode", "1");//1是支付宝支付
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 30);
					Date date = cal.getTime();
					SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					params2.put("endTime", s.format(date));
					// 将订单信息进行插库
					dao.saveMonthlyLogInfo(params2);
		            
		            resparam.setResultCode("success");
		            resparam.setOrderId(billid);
		            resparam.setPicUrl(SysConfig.getVal("image_server") +SysConfig.getVal("qrcode")+"/"+billid + ".jpg");
		            this.logger.info("生成二维码图片地址是" + SysConfig.getVal("image_server") +SysConfig.getVal("qrcode")+"/"+billid + ".jpg");
		        }
		        catch (Exception e) {
		            this.logger.error(e.getMessage(), e);
		            resparam.setResultCode("1");
					resparam.setResultDesc("创建订单失败");
		        }
				
		        return resparam;
	       
	    }
	    
	    
	    
	    @Override
	    public void verifyCallBack(Map requestParams) throws ClientProtocolException, IOException, NoSuchAlgorithmException {
	        Map<String, String> params = new HashMap<String, String>();
	        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
	            String name = (String) iter.next();
	            String[] values = (String[]) requestParams.get(name);
	            String valueStr = "";
	            for (int i = 0; i < values.length; i++) {
	                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
	            }
	            params.put(name, valueStr);
	            System.out.println("*********************"+params.toString());
	        }
	        Boolean result = this.validateParams(params);
	        System.out.println("支付宝回调结束结果…………………………………………………………………………**********************"+result);
	        	

	    }
	    
	    public Boolean validateParams(Map<String, String> params) {
	    	String status = params.get("trade_status");

	    	Boolean aaBoolean =false;
	        if (status.equals("WAIT_BUYER_PAY")) { // 如果状态是正在等待用户付款
	        } else if (status.equals("TRADE_CLOSED")) { // 如果状态是未付款交易超时关闭，或支付完成后全额退款
	        } else if (status.equals("TRADE_SUCCESS") || status.equals("TRADE_FINISHED")) { // 如果状态是已经支付成功
	            //成功 更新状态
	    	if (AlipayNotify.verify(params)) {
	    		// 验证通过更改数据库的交易状态
	    		this.logger.info("支付宝交易号" + params.get("trade_no"));
	    		  //更改数据库的状态
	    		String out_trade_no = params.get("out_trade_no");  //中九交易流水号
                String trade_no = params.get("trade_no");//支付宝交易流水号
        		BillMonthLogVO billMonthLogVO = new BillMonthLogVO();
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        		billMonthLogVO.setOrderNo(out_trade_no);//中九流水号
        		billMonthLogVO.setOrderId(trade_no);//微信外围订单号
        		billMonthLogVO.setStatus("1");//1表示支付成功
//        			hnyxBillMonthLogVO.setStatusDesc(params.get("code"));
        				this.dao.updataBillMopnthLogOrder(billMonthLogVO);
        				billMonthLogVO = this.dao.getBillMonthLogInfo(out_trade_no);
        			if (billMonthLogVO!=null) {
        				Integer countOrderNo= this.dao.getCountOrderNo(billMonthLogVO);
        				if(countOrderNo==0){
        					this.dao.insertBillMonthInfo(billMonthLogVO);
        				}
        			}
	    		this.logger.info("支付宝回调结束");
	    		 aaBoolean=true;
	    	
	    		 
	        }
	        else {
	        	aaBoolean= false;
	        }
	    } else {
	    	aaBoolean= false;
	    }
	    return aaBoolean;
	    }


}
