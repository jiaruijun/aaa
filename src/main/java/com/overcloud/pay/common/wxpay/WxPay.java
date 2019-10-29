package com.overcloud.pay.common.wxpay;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.qrcode.QRCodeUtil;

@RestController
@RequestMapping("/wxpay")
public class WxPay {
	private static long num = 1;
	
	public String weixin_pay() throws Exception {  
        // 账号信息  
        String appid = PayConfigUtil.APP_ID;  // appid  
        //String appsecret = PayConfigUtil.APP_SECRET; // appsecret  
        String mch_id = PayConfigUtil.MCH_ID; // 商业号  
        String key = PayConfigUtil.API_KEY; // key  
  
        String currTime = PayCommonUtil.getCurrTime();  
        String strTime = currTime.substring(8, currTime.length());  
        String strRandom = PayCommonUtil.buildRandom(4) + "";  
        String nonce_str = strTime + strRandom;  
          
        String order_price = "10"; // 价格   注意：价格的单位是分  
        String body = "goodssssss";   // 商品名称  
        String out_trade_no = "11338"; // 订单号  
          
        // 获取发起电脑 ip  
        String spbill_create_ip = PayConfigUtil.CREATE_IP;  
        // 回调接口   
        String notify_url = PayConfigUtil.NOTIFY_URL;  
        String trade_type = "NATIVE";  
          
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
        packageParams.put("appid", appid);  
        packageParams.put("mch_id", mch_id);  
        packageParams.put("nonce_str", nonce_str);  
        packageParams.put("body", body);  
        String billid = this.getTradeNumber();
        packageParams.put("out_trade_no", billid);  
        packageParams.put("total_fee", order_price);  
        packageParams.put("spbill_create_ip", spbill_create_ip);  
        packageParams.put("notify_url", notify_url);  
        packageParams.put("trade_type", trade_type);  
  
        String sign = PayCommonUtil.createSign("UTF-8", packageParams,key);  
        packageParams.put("sign", sign);  
          
        String requestXML = PayCommonUtil.getRequestXml(packageParams);  
        System.out.println(requestXML);  
   
        String resXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);  
  
          
        Map map = XMLUtil.doXMLParse(resXml);  
        //String return_code = (String) map.get("return_code");  
        //String prepay_id = (String) map.get("prepay_id");  
        String urlCode = (String) map.get("code_url");  
          
        return urlCode;  
} 
	
	@RequestMapping(value = "/pay")
	public String weixin_pay1() throws Exception {  
		// 生成二维码图片
		String billid = this.getTradeNumber();
		
		String aaString = weixin_pay();
        String qrcode_save_dir = SysConfig.getVal("qr_absolute_dir");
        QRCodeUtil.main(aaString, qrcode_save_dir, billid);
		return null;
	}
	/*public static String QRfromGoogle(String chl) throws Exception {  
	    int widhtHeight = 300;  
	    String EC_level = "L";  
	    int margin = 0;  
	    chl = UrlEncode(chl);  
	    String QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight  
	            + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl=" + chl;  
	  
	    return QRfromGoogle;  
	} */ 
	
	// 特殊字符处理  
	/*public static String UrlEncode(String src)  throws UnsupportedEncodingException {  
	    return URLEncoder.encode(src, "UTF-8").replace("+", "%20");  
	} */ 
	
	@RequestMapping(value = "/notify")
	public void weixin_notify(HttpServletRequest request,HttpServletResponse response) throws Exception{  
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
        String key = PayConfigUtil.API_KEY; // key  
  
//        logger.info(packageParams);  
        //判断签名是否正确  
        if(PayCommonUtil.isTenpaySign("UTF-8", packageParams,key)) {  
            //------------------------------  
            //处理业务开始  
            //------------------------------  
            String resXml = "";  
            if("SUCCESS".equals((String)packageParams.get("result_code"))){  
                // 这里是支付成功  
                //////////执行自己的业务逻辑////////////////  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");  
                  
                String total_fee = (String)packageParams.get("total_fee");  
                  
                System.out.println("mch_id:"+mch_id);
                System.out.println("openid:"+openid);
                System.out.println("is_subscribe:"+is_subscribe);
                System.out.println("out_trade_no:"+out_trade_no);
                System.out.println("total_fee:"+total_fee);
                  
                //////////执行自己的业务逻辑////////////////  
                  
                System.out.println("支付成功");
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.  
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";  
                  
            } else {  
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

}
