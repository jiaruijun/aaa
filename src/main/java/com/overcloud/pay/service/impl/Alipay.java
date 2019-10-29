package com.overcloud.pay.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.constst.ReturnMsgCode;
import com.overcloud.pay.common.encrypt.RSAUtils;
import com.overcloud.pay.entity.PayInfo;
import com.overcloud.pay.vo.SendResVO;
import com.overcloud.pay.vo.SendVO;

/**
 * 进行支付的功能实现
 * 
 * @since 2015-11-28 上午9:44:01
 * @version 1.0
 * @author JPF
 */
public class Alipay {
    private static final Logger logger = LoggerFactory.getLogger(Alipay.class);

    public static DecimalFormat df = new DecimalFormat("#0.00");

    /**
     * 进行解密
     * 
     * @param key
     * @return
     */
    public static String getDecKey(String key) {
        return RSAUtils.decrypt(key, SysConfig.getVal("ServerprivateKey"));
    }

    public static String getMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md5.digest(str.getBytes("UTF-8"));
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = md5Bytes[i] & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    public static String getTime(Date time) {
        return DateFormatUtils.format(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTradStatus(PayInfo payInfo) {
        if (ReturnMsgCode.WAIT_BUYER_PAY.equals(payInfo.getTradeStatus())) {
            return ReturnMsgCode.TRADE_WAIT;
        }
        else if (ReturnMsgCode.TRADE_CLOSED.equals(payInfo.getTradeStatus())) {
            return ReturnMsgCode.TRADE_CLOSED;
        }
        else if (ReturnMsgCode.TRADE_SUCCESS.equals(payInfo.getTradeStatus())
                || ReturnMsgCode.TRADE_PENDING.equals(payInfo.getTradeStatus())
                || ReturnMsgCode.TRADE_FINISHED.equals(payInfo.getTradeStatus())) {
            return ReturnMsgCode.TRADE_SUCCESS;
        }
        else if (payInfo.getTimeOut() != null && payInfo.getTimeOut() == 1) {
            return ReturnMsgCode.TRADE_CLOSED;
        }
        else {
            return ReturnMsgCode.TRADE_WAIT;
        }
    }


    public static SendResVO Send(SendVO send) throws Exception {
        logger.info("请求支付宝接口下订单");
        String notify_url = SysConfig.getVal("month_notify_url");
        // 业务参数生成（biz_content）
        StringBuilder sb = new StringBuilder();
        sb.append("{\"out_trade_no\":\"" + send.getOut_trade_no() + "\",");// 中九订单号
        sb.append("\"subject\":\"" + send.getSubject() + "\",");// 商品标题
        if (StringUtils.isNotEmpty(send.getBody())) {
            sb.append("\"body\":\"" + send.getBody() + "\",");// 商品标题
        }
        sb.append("\"total_amount\":\"" + send.getTotal_amount() + "\"}");// 商品金额
        String biz_content = sb.toString();
        // 使用SDK，构建群发请求模型
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent(biz_content);
        request.setNotifyUrl(notify_url);
        AlipayTradePrecreateResponse response = null;
        try {
            // 使用SDK，调用交易下单接口
            response = alipayClient.execute(request);
            if (null != response && response.isSuccess()) {
                if (response.getCode().equals("10000")) {
                    SendResVO res = new SendResVO();
                    res.setCode(response.getCode());
                    res.setMsg(response.getMsg());
                    res.setOut_trade_no(response.getOutTradeNo());
                    res.setQr_code(response.getQrCode());
                    return res;
                }
                else {
                    // 打印错误码
                    logger.info("错误码：" + response.getSubCode());
                    logger.info("错误描述：" + response.getSubMsg());
                    SendResVO res = new SendResVO();
                    res.setResult("fail");
                    res.setErrorDes("请求支付宝获取二维码失败");
                    return res;
                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    public static SendResVO SendMonth(SendVO send) throws Exception {
    	logger.info("请求支付宝接口下订单");
    	String notify_url = SysConfig.getVal("notify_url");
    	// 业务参数生成（biz_content）
    	StringBuilder sb = new StringBuilder();
    	sb.append("{\"out_trade_no\":\"" + send.getOut_trade_no() + "\",");// 中九订单号
    	sb.append("\"subject\":\"" + send.getSubject() + "\",");// 商品标题
    	if (StringUtils.isNotEmpty(send.getBody())) {
    		sb.append("\"body\":\"" + send.getBody() + "\",");// 商品标题
    	}
    	sb.append("\"total_amount\":\"" + send.getTotal_amount() + "\"}");// 商品金额
    	String biz_content = sb.toString();
    	// 使用SDK，构建群发请求模型
    	AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    	AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
    	request.setBizContent(biz_content);
    	request.setNotifyUrl(notify_url);
    	AlipayTradePrecreateResponse response = null;
    	try {
    		// 使用SDK，调用交易下单接口
    		response = alipayClient.execute(request);
    		if (null != response && response.isSuccess()) {
    			if (response.getCode().equals("10000")) {
    				SendResVO res = new SendResVO();
    				res.setCode(response.getCode());
    				res.setMsg(response.getMsg());
    				res.setOut_trade_no(response.getOutTradeNo());
    				res.setQr_code(response.getQrCode());
    				return res;
    			}
    			else {
    				// 打印错误码
    				logger.info("错误码：" + response.getSubCode());
    				logger.info("错误描述：" + response.getSubMsg());
    				SendResVO res = new SendResVO();
    				res.setResult("fail");
    				res.setErrorDes("请求支付宝获取二维码失败");
    				return res;
    			}
    		}
    	}
    	catch (Exception e) {
    		// TODO: handle exception
    	}
    	return null;
    }
    public static SendResVO SendPay(SendVO send) throws Exception {
        logger.info("请求支付宝接口下订单");
        String notify_url = SysConfig.getVal("pay_notify_url");
        logger.info("回调地址是"+notify_url);
        // 业务参数生成（biz_content）
        StringBuilder sb = new StringBuilder();
        sb.append("{\"out_trade_no\":\"" + send.getOut_trade_no() + "\",");// 中九订单号
        sb.append("\"subject\":\"" + send.getSubject() + "\",");// 商品标题
        if (StringUtils.isNotEmpty(send.getBody())) {
            sb.append("\"body\":\"" + send.getBody() + "\",");// 商品标题
        }
        sb.append("\"total_amount\":\"" + send.getTotal_amount() + "\"}");// 商品金额
        String biz_content = sb.toString();
        // 使用SDK，构建群发请求模型
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent(biz_content);
        request.setNotifyUrl(notify_url);
        AlipayTradePrecreateResponse response = null;
        try {
            // 使用SDK，调用交易下单接口
            response = alipayClient.execute(request);
            if (null != response && response.isSuccess()) {
                if (response.getCode().equals("10000")) {
                    SendResVO res = new SendResVO();
                    res.setCode(response.getCode());
                    res.setMsg(response.getMsg());
                    res.setOut_trade_no(response.getOutTradeNo());
                    res.setQr_code(response.getQrCode());
                    return res;
                }
                else {
                    // 打印错误码
                    logger.info("错误码：" + response.getSubCode());
                    logger.info("错误描述：" + response.getSubMsg());
                    SendResVO res = new SendResVO();
                    res.setResult("fail");
                    res.setErrorDes("请求支付宝获取二维码失败");
                    return res;
                }
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    
    
    
    
}
