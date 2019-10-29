package com.overcloud.pay.service.impl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.qrcode.QRCodeUtil;
import com.overcloud.pay.common.wxpay.HttpUtil;
import com.overcloud.pay.common.wxpay.PayCommonUtil;
import com.overcloud.pay.common.wxpay.XMLUtil;
import com.overcloud.pay.dao.OrderInfoDao;
import com.overcloud.pay.service.WxPayService;
import com.overcloud.pay.vo.BillMonthLogVO;
import com.overcloud.pay.vo.MonthlyGameCategoryVo;
import com.overcloud.pay.vo.OrderResultVo;

@Service
public class WxPayServiceImpl implements WxPayService{
	
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
//		MonthlyInfoVO monthPayInfo = new MonthlyInfoVO();
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;
		String billid = this.getTradeNumber();
		String amounts = params.get("amounts");
		
		//属于包月还是单点  0包月 1单点
		String isMonthly = params.get("isMonthly");

		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		packageParams.put("appid", SysConfig.getVal("wxappId"));
		packageParams.put("mch_id",  SysConfig.getVal("wxpartnerKey"));
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", SysConfig.getVal("monthName"));
		packageParams.put("out_trade_no", billid);
		int a = (int) (Float.parseFloat(amounts) * 100);
		packageParams.put("total_fee", String.valueOf(a));
		packageParams.put("spbill_create_ip", SysConfig.getVal("spbill_create_ip"));
		packageParams.put("notify_url", SysConfig.getVal("month_notify_url"));
		packageParams.put("trade_type", "NATIVE");
		// 将数据存入数据库
			
			
	    	
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, SysConfig.getVal("api_key"));
			packageParams.put("sign", sign);

			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(SysConfig.getVal("ufdoder_url"), requestXML);
			Map map = null;
			try {
				map = XMLUtil.doXMLParse(resXml);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.logger.error(e.getMessage(), e);
				resparam.setResultCode("1");
				resparam.setResultDesc("创建订单失败");
				return resparam;
			}
			String urlCode = (String) map.get("code_url");
			String qrcode_save_dir = SysConfig.getVal("image_absolute_dir")+SysConfig.getVal("qrcode");
			try {
				QRCodeUtil.main(urlCode, qrcode_save_dir, billid);
				
				Map<String, String> params2 =new HashMap<String, String> ();
				params2.put("status", "0");
		    	params2.put("userId", params.get("userId"));
		    	params2.put("productName", "");
		    	params2.put("total_fee",  amounts);
		    	params2.put("orderNo", billid);
		    	MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
				params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
				params2.put("monthlyNo", monthlyGameCategoryVo.getId());
				params2.put("days", "30");
				params2.put("isPayMode", "0");//0是微信支付
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 30);
				Date date = cal.getTime();
				SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				params2.put("endTime", s.format(date));
				dao.saveMonthlyLogInfo(params2);
				resparam.setResultDesc("创建订单成功");
				resparam.setResultCode("success");
				resparam.setOrderNo(billid);
				resparam.setPicUrl(SysConfig.getVal("image_server") +SysConfig.getVal("qrcode")+"/"+billid + ".jpg");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.logger.error(e.getMessage(), e);
				resparam.setResultCode("1");
				resparam.setResultDesc("创建订单失败");
			}
			
			return resparam;

	}
	
	
    @Override
    public void verifyCallBack(Map requestParams,HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        
        Boolean result = false;
        
        //读取参数  
        InputStream inputStream ;  
        StringBuffer sb = new StringBuffer();  
        inputStream = request.getInputStream();  
        String s ;  
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        while ((s = in.readLine()) != null){  
            sb.append(s);  
        }  
        in.close();  
        inputStream.close();  
  
        //解析xml成map  
        Map<String, String> m = new HashMap<String, String>();  
        m = XMLUtil.doXMLParse(sb.toString());  
          
        //过滤空 设置 TreeMap  
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();        
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            String parameterValue = m.get(parameter);  
              
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.trim();  
            }  
            packageParams.put(parameter, v);  
        }  
          
        // 账号信息  
        String key = SysConfig.getVal("api_key"); // key  
  
//        logger.info(packageParams);  
        //判断签名是否正确  
        if(PayCommonUtil.isTenpaySign("UTF-8", packageParams,key)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            String resXml = "";  
//            MonthPayInfo info = new MonthPayInfo();;
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
            	result = true;
            	System.out.println("微信回调中九传递的参数"+packageParams.toString());
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                String trade_no = (String)packageParams.get("transaction_id");  
                System.out.println(">>>>>>>>>>>>>>>订单号"+trade_no);
                System.out.println("*********************外围订单号"+out_trade_no);
                String total_fee = (String)packageParams.get("total_fee"); 
                System.out.println("mch_id:"+mch_id);
                System.out.println("openid:"+openid);
                System.out.println("is_subscribe:"+is_subscribe);
                System.out.println("out_trade_no:"+out_trade_no);
                System.out.println("total_fee:"+total_fee);
                  //更改数据库的状态
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

                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("支付成功");
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.  
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";  
                  
            } else {  
            	result = false;
                System.out.println("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";  
            }  
            //------------------------------  
            //处理业务完毕  
            //------------------------------  
            BufferedOutputStream out = new BufferedOutputStream(  
                    response.getOutputStream());  
            out.write(resXml.getBytes());  
            out.flush();  
            out.close();  
        } else{  
            System.out.println("通知签名验证失败");
        }  
          
    
     

    }

}
