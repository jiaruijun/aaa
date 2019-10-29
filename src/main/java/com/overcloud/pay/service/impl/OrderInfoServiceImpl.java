package com.overcloud.pay.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.overcloud.pay.common.JsonPluginsUtil;
import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.qrcode.QRCodeUtil;
import com.overcloud.pay.dao.IUserRecordDao;
import com.overcloud.pay.dao.OrderInfoDao;
import com.overcloud.pay.entity.MonthBill;
import com.overcloud.pay.service.OrderInfoService;
import com.overcloud.pay.vo.AuthMonthlyUserVO;
import com.overcloud.pay.vo.BillMonthLogVO;
import com.overcloud.pay.vo.BodyVO;
import com.overcloud.pay.vo.BossAuthMonthlyUserVO;
import com.overcloud.pay.vo.BossIntegralPayInfoVO;
import com.overcloud.pay.vo.BossIntegralQueryReturnVO;
import com.overcloud.pay.vo.BossPayInfoVO;
import com.overcloud.pay.vo.BossQRCodePayInfoVO;
import com.overcloud.pay.vo.GsydbyOrderInfoVO;
import com.overcloud.pay.vo.MonthlyGameCategoryVo;
import com.overcloud.pay.vo.MonthlyParInfoVO;
import com.overcloud.pay.vo.OrderReturnVO;
import com.overcloud.pay.vo.TOkenVO;
import com.overcloud.pay.vo.Zte9ForwardorderVO;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static long num = 1;

    @Resource
    OrderInfoDao dao;
    @Resource
    IUserRecordDao userRecordDao;

    /**
     * 生成订单号
     *
     * @return
     */
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


    /**
     * 请求自有数据库查询该用户是否包月接口
     */
    @Override
    public AuthMonthlyUserVO getAuthInfo(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
        AuthMonthlyUserVO authMonthlyUserVO = new AuthMonthlyUserVO();
        try {
            authMonthlyUserVO = dao.getUserAuthInfo(params);
            if (authMonthlyUserVO == null) {
                authMonthlyUserVO = new AuthMonthlyUserVO();
                authMonthlyUserVO.setResultCode("1");
                authMonthlyUserVO.setResultDesc("该用户没有包月或者已经到期");
            } else if (authMonthlyUserVO != null && authMonthlyUserVO.getUserId().equals(params.get("userId"))) {
                authMonthlyUserVO.setResultCode("0");
                authMonthlyUserVO.setResultDesc("该用户是包月用户");
            }
        } catch (Exception e) {
            // TODO: handle exception
            authMonthlyUserVO.setResultCode("2");
            authMonthlyUserVO.setResultDesc("查询失败");
            e.printStackTrace();

        }
        return authMonthlyUserVO;
    }


    /**
     * 进入大厅插入用户数据 接口
     */
    @Override
    public void insertUser(Map<String, String> params) {
        params.put("status", "1");
        this.dao.saveUserInfo(params);
        this.dao.saveVisitInfo(params);
    }


    /**
     * 前端请求后台根据OrderId  拿到支付结果
     */
    @Override
    public OrderReturnVO getMonthOrderInfoCode(Map<String, String> params) {
        // TODO Auto-generated method stub
        OrderReturnVO orderReturnVO = new OrderReturnVO();
//		MonthlyInfoVO  monthlyInfoVO= dao.getOrderInfoCode(params);
        //  包月还是单点   0包月  1 单点
        String isMonthly = params.get("isMonthly");
        BillMonthLogVO hnyxBillMonthLogVO = dao.getMonthOrderInfoCode(params);
        if (hnyxBillMonthLogVO != null) {
            orderReturnVO.setOrderCode(hnyxBillMonthLogVO.getStatus());
            orderReturnVO.setOrderDesc(hnyxBillMonthLogVO.getStatusDesc());
        } else {
            orderReturnVO.setOrderCode("b");
            orderReturnVO.setOrderDesc("该订单不存在");
        }
        return orderReturnVO;
    }


    @Override
    public List<GsydbyOrderInfoVO> getGsydbyBillInfo(Map<String, String> params) {
        // TODO Auto-generated method stub
        // 根据用户id查询消费订单
        String phone = params.get("phone");
        GsydbyOrderInfoVO gsydbyOrderInfoVO = new GsydbyOrderInfoVO();
        gsydbyOrderInfoVO.setPhone(phone);
        // String userId = params.get("userId");
        List<GsydbyOrderInfoVO> cqgdbyOrderInfoVOs = dao.getGsydbyBillInfo(gsydbyOrderInfoVO);

        return cqgdbyOrderInfoVOs;

    }


    /**
     * 请求BOss 查看该用户是否订购
     */
    @Override
    public BossAuthMonthlyUserVO getbossAuthInfo(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
        BossAuthMonthlyUserVO bossAuthMonthlyUserVO = new BossAuthMonthlyUserVO();

        Integer counDeviceId = dao.counDeviceId(params.get("deviceId"));
        this.logger.info("查看该deviceId是否存在在数据库中" + params.get("deviceId") + "------" + counDeviceId);
        if (counDeviceId != 0) {
            bossAuthMonthlyUserVO.setResultcode("1000");
            return bossAuthMonthlyUserVO;
        }


        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SysConfig.getVal("bossAuthUrl"));
        this.logger.info("业务鉴权请求甘肃移动家开平台url：{}", stringBuffer.toString());
        HttpPost method = new HttpPost(stringBuffer.toString());
        JSONObject jsonParam1 = new JSONObject();
        JSONObject jsonParam2 = new JSONObject();
        JSONObject jsonParam3 = new JSONObject();
        jsonParam1.put("idType", params.get("idType"));
        String idValue = params.get("idValue");
        if (idValue != null && idValue.length() == 13) {
            boolean aa = idValue.startsWith("86");
            if (aa) {
                this.logger.info("支付用户编号携带86的" + idValue);
                String bbString = idValue.replaceFirst("86", "");
                this.logger.info("支付用户编号携带86处理过后的" + bbString);
                params.put("idValue", bbString);
            }
        }
        jsonParam1.put("idValue", params.get("idValue"));
        jsonParam1.put("deviceType", params.get("deviceType"));
        jsonParam1.put("deviceId", params.get("deviceId"));
        jsonParam1.put("productIds", params.get("productIds"));
        jsonParam1.put("contentId", params.get("contentId"));

        jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        jsonParam2.put("systemtime", dateString);
        jsonParam2.put("version", params.get("version"));
        jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
        jsonParam2.put("access_token", params.get("access_token"));
        jsonParam3.put("body", jsonParam1.toString());
        jsonParam3.put("header", jsonParam2.toString());
        StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
        this.logger.info("boss鉴权请求参数：{}", jsonParam3.toString());
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);
        HttpResponse result = new DefaultHttpClient().execute(method);

        String content = EntityUtils.toString(result.getEntity());
        this.logger.info("业务鉴权请求甘肃移动家开平台url返回的数据：{}", content);
        Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
        Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
        Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));

        bossAuthMonthlyUserVO.setInresponseto(resultmapheader.get("inresponseto"));
        bossAuthMonthlyUserVO.setSystemtime(resultmapheader.get("systemtime"));
        bossAuthMonthlyUserVO.setVersion(resultmapheader.get("version"));
        bossAuthMonthlyUserVO.setResultcode(resultmapheader.get("resultcode"));
        bossAuthMonthlyUserVO.setResultdesp(resultmapheader.get("resultdesp"));
        bossAuthMonthlyUserVO.setIsVerified(resultmapbody.get("isVerified"));
        bossAuthMonthlyUserVO.setOrderNumber(resultmapbody.get("orderNumber"));
        bossAuthMonthlyUserVO.setValidFrom(resultmapbody.get("validFrom"));
        bossAuthMonthlyUserVO.setValidTo(resultmapbody.get("validTo"));

        return bossAuthMonthlyUserVO;
    }
    /**
     * 请求BOss新平台 查看该用户是否订购
     */
    @Override
    public BossAuthMonthlyUserVO getbossNewAuthInfo(Map<String, String> params) throws Exception {
    	// TODO Auto-generated method stub
    	BossAuthMonthlyUserVO bossAuthMonthlyUserVO = new BossAuthMonthlyUserVO();
    	
    	Integer counDeviceId = dao.counDeviceId(params.get("deviceId"));
    	this.logger.info("查看该deviceId是否存在在数据库中" + params.get("deviceId") + "------" + counDeviceId);
    	if (counDeviceId != 0) {
    		bossAuthMonthlyUserVO.setResultcode("1000");
    		return bossAuthMonthlyUserVO;
    	}
    	
    	
    	StringBuffer stringBuffer = new StringBuffer();
    	stringBuffer.append(SysConfig.getVal("bossNewAuthUrl"));
    	this.logger.info("业务鉴权请求甘肃移动家开平台url：{}", stringBuffer.toString());
    	HttpPost method = new HttpPost(stringBuffer.toString());
    	JSONObject jsonParam1 = new JSONObject();
    	JSONObject jsonParam2 = new JSONObject();
    	JSONObject jsonParam3 = new JSONObject();
    	jsonParam1.put("idType", params.get("idType"));
    	String idValue = params.get("idValue");
    	if (idValue != null && idValue.length() == 13) {
    		boolean aa = idValue.startsWith("86");
    		if (aa) {
    			this.logger.info("支付用户编号携带86的" + idValue);
    			String bbString = idValue.replaceFirst("86", "");
    			this.logger.info("支付用户编号携带86处理过后的" + bbString);
    			params.put("idValue", bbString);
    		}
    	}
    	jsonParam1.put("idValue", params.get("idValue"));
    	jsonParam1.put("deviceType", params.get("deviceType"));
    	jsonParam1.put("deviceId", params.get("deviceId"));
    	jsonParam1.put("productIds", params.get("productIds"));
    	jsonParam1.put("contentId", params.get("contentId"));
    	
    	jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
    	Date currentTime = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    	String dateString = formatter.format(currentTime);
    	jsonParam2.put("systemtime", dateString);
    	jsonParam2.put("version", params.get("version"));
    	jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
    	jsonParam2.put("access_token", params.get("access_token"));
    	jsonParam3.put("body", jsonParam1.toString());
    	jsonParam3.put("header", jsonParam2.toString());
    	StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
    	this.logger.info("boss鉴权请求参数：{}", jsonParam3.toString());
    	entity.setContentEncoding("UTF-8");
    	entity.setContentType("application/json");
    	method.setEntity(entity);
    	HttpResponse result = new DefaultHttpClient().execute(method);
    	
    	String content = EntityUtils.toString(result.getEntity());
    	this.logger.info("业务鉴权请求甘肃移动家开平台url返回的数据：{}", content);
    	Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
    	Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
    	Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));
    	
    	bossAuthMonthlyUserVO.setInresponseto(resultmapheader.get("inresponseto"));
    	bossAuthMonthlyUserVO.setSystemtime(resultmapheader.get("systemtime"));
    	bossAuthMonthlyUserVO.setVersion(resultmapheader.get("version"));
    	bossAuthMonthlyUserVO.setResultcode(resultmapheader.get("resultcode"));
    	bossAuthMonthlyUserVO.setResultdesp(resultmapheader.get("resultdesp"));
    	bossAuthMonthlyUserVO.setIsVerified(resultmapbody.get("isVerified"));
    	bossAuthMonthlyUserVO.setOrderNumber(resultmapbody.get("orderNumber"));
    	bossAuthMonthlyUserVO.setValidFrom(resultmapbody.get("validFrom"));
    	bossAuthMonthlyUserVO.setValidTo(resultmapbody.get("validTo"));
    	
    	return bossAuthMonthlyUserVO;
    }

    
    
    /**
     * BOSS  家开请求中九正向订购接口
     */
    @Override
    public BodyVO zte9Forwardorder(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    	BodyVO bodyVO = new BodyVO();
    	Zte9ForwardorderVO bossAuthMonthlyUserVO = new Zte9ForwardorderVO();
//    	Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(params.get("body"));
//    	if(){
//    		
//    	}
    	
    	bossAuthMonthlyUserVO.setResult("0");
    	bossAuthMonthlyUserVO.setDesc("OK");
    	bodyVO.setBody(bossAuthMonthlyUserVO);
        return bodyVO;
    }


    /**
     * 请求boss  获取用户token  在鉴权接口 和  支付接口需要
     */
    @Override
    public TOkenVO bossGetToken(Map<String, String> params) {
        // TODO Auto-generated method stub
        TOkenVO tOkenVO = new TOkenVO();
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(SysConfig.getVal("bossGettokenUrl"));
            this.logger.info("获取token请求甘肃移动家开平台url：{}", stringBuffer.toString());
            HttpPost method = new HttpPost(stringBuffer.toString());
            JSONObject jsonParam1 = new JSONObject();
//			jsonParam1.put("Powerkey", "m722s566b872o629");
            StringEntity entity = new StringEntity(jsonParam1.toString(), "UTF-8");
            this.logger.info("boss获取token请求参数：{}", jsonParam1.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            if(params.get("powerkey")==null||"".equals(params.get("powerkey"))){
            	method.addHeader("Powerkey", "m722s566b872o629");
            }else {
            	method.addHeader("Powerkey", params.get("powerkey"));
			}
            HttpResponse result = new DefaultHttpClient().execute(method);
            String content = EntityUtils.toString(result.getEntity());
            this.logger.info("获取token请求甘肃移动家开平台url返回的数据：{}", content);
            Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
            tOkenVO.setResult(resultmap.get("result"));
            tOkenVO.setDesc(resultmap.get("desc"));
            tOkenVO.setData(resultmap.get("data"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return tOkenVO;
    }


    /**
     * 前端请求BOSS支付接口
     */
    @Override
    public BossPayInfoVO bossPayInfo(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
        BossPayInfoVO bossPayInfoVO = new BossPayInfoVO();
        synchronized (this) {
        	String idValue = params.get("idValue");
			if (idValue!=null&&idValue.length()==13) {
				 boolean aa = idValue.startsWith("86");
				if(aa){
					this.logger.info("支付用户编号携带86的"+idValue);
					String bbString = idValue.replaceFirst("86", "");
					this.logger.info("支付用户编号携带86处理过后的"+bbString);
					params.put("idValue", bbString);
				}
			}
			Integer  countBlack = dao.getBlackList(params);
			if(countBlack!=0){
				bossPayInfoVO.setResultcode("1020");
				bossPayInfoVO.setResultdesp("该用户不允许订购");
				this.logger.info("黑名单用户不允许订购"+idValue);
				return  bossPayInfoVO;
			}
            StringBuffer stringBuffer = new StringBuffer();
            String billid = this.getTradeNumber();
            stringBuffer.append(SysConfig.getVal("bossPayUrl"));
            this.logger.info("请求甘肃移动家开平台支付url：{}", stringBuffer.toString());
            HttpPost method = new HttpPost(stringBuffer.toString());
            JSONObject jsonParam1 = new JSONObject();
            JSONObject jsonParam2 = new JSONObject();
            JSONObject jsonParam3 = new JSONObject();
           
            jsonParam1.put("userAuthorizationCode", params.get("userAuthorizationCode"));
            jsonParam1.put("idType", params.get("idType"));
//            String idValue = params.get("idValue");
//            if (idValue != null && idValue.length() == 13) {
//                boolean aa = idValue.startsWith("86");
//                if (aa) {
//                    this.logger.info("支付用户编号携带86的" + idValue);
//                    String bbString = idValue.replaceFirst("86", "");
//                    this.logger.info("支付用户编号携带86处理过后的" + bbString);
//                    params.put("idValue", bbString);
//                }
//            }
            jsonParam1.put("idValue", params.get("idValue"));
            jsonParam1.put("deviceType", params.get("deviceType"));
            jsonParam1.put("deviceId", params.get("deviceId"));
            jsonParam1.put("productId", params.get("productId"));
            jsonParam1.put("chargeType", "2");
            if (params.get("productPrice") != null && !params.get("productPrice").equals("")) {
                jsonParam1.put("productPrice", String.valueOf((Integer.valueOf(params.get("productPrice"))) * 1000));
            } else {
                jsonParam1.put("productPrice", "");
            }

            jsonParam1.put("orderType", "06");
            jsonParam1.put("orderChannel", "04");
            jsonParam1.put("effectiveTime", "-1");

            jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(currentTime);
            jsonParam2.put("systemtime", dateString);
            jsonParam2.put("version", params.get("version"));
            jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
            jsonParam2.put("access_token", params.get("access_token"));
            jsonParam3.put("body", jsonParam1.toString());
            jsonParam3.put("header", jsonParam2.toString());
            StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
            this.logger.info("boss请求支付请求参数：{}", jsonParam3.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            HttpResponse result = new DefaultHttpClient().execute(method);

            String content = EntityUtils.toString(result.getEntity());
            this.logger.info("请求甘肃移动家开平台支付url返回的数据：{}", content);
            Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
            Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
            Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));

            bossPayInfoVO.setInresponseto(resultmapheader.get("inresponseto"));
            bossPayInfoVO.setSystemtime(resultmapheader.get("systemtime"));
            bossPayInfoVO.setVersion(resultmapheader.get("version"));
            bossPayInfoVO.setResultcode(resultmapheader.get("resultcode"));
            bossPayInfoVO.setResultdesp(resultmapheader.get("resultdesp"));

            bossPayInfoVO.setOrderNumber(resultmapbody.get("orderNumber"));
            bossPayInfoVO.setOrderTime(resultmapbody.get("orderTime"));
            bossPayInfoVO.setValidFrom(resultmapbody.get("validFrom"));
            bossPayInfoVO.setValidTo(resultmapbody.get("validTo"));

            if (resultmapheader.get("resultcode").equals("1000")) {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "1");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee", params.get("productPrice"));
                params2.put("orderNo", billid);
                params2.put("orderId", resultmapbody.get("orderNumber"));
                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
                params2.put("monthlyNo", monthlyGameCategoryVo.getId());
                params2.put("action", params.get("action"));
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 30);
//                Date date = cal.getTime();
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                params2.put("endTime", s.format(date));
              //获取当前月最后一天
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
			    String last = format.format(ca.getTime());
			    System.out.println("===============last:"+last+" 23:59:59");
				params2.put("endTime", last+" 23:59:59");
                params2.put("resultcode", resultmapheader.get("resultcode"));
                params2.put("resultdesp", resultmapheader.get("resultdesp"));
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "1");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
                dao.insertBillMonthInfos(params2);
                if(params.get("action")!=null&&!params.get("action").equals("")){
					this.logger.info("甘肃移动音乐回调订单action存在插入带Filter表中>>>action值"+params.get("action"));
					this.dao.insFilter(params.get("idValue"));
					this.logger.info("甘肃移动音乐回调订单action存在插入带Filter表中》》》userId值"+params.get("action"));
				}
            } else {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "2");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee", params.get("productPrice"));
                params2.put("orderNo", billid);
                params2.put("orderId", resultmapbody.get("orderNumber"));
                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
                params2.put("monthlyNo", monthlyGameCategoryVo.getId());
                params2.put("action", params.get("action"));
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 30);
//                Date date = cal.getTime();
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                params2.put("endTime", s.format(date));
              //获取当前月最后一天
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
			    String last = format.format(ca.getTime());
			    System.out.println("===============last:"+last+" 23:59:59");
				params2.put("endTime", last+" 23:59:59");
                params2.put("resultcode", resultmapheader.get("resultcode"));
                params2.put("resultdesp", resultmapheader.get("resultdesp"));
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "1");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
            }
        }
        return bossPayInfoVO;
    }
    
    public static String getTransactionID(String pixStr, int length) {
		/*
		 * if (length<=pixStr.length()+20) { return new
		 * Random().nextInt(length)+""; }
		 */
		if (length < 10) {
			return new Random().nextInt(length) + "";
		}
		long currentTimeMillis = System.currentTimeMillis();
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyMMddHHmmss"); // ʽ
																								// ȡ
		String mydate = format.format(currentTimeMillis);
		// java.util.Random();
		// String mydate = Tools.getDate("yyyyMMddHHmmss");
		int tmp = length - mydate.length();
		int pixsize = 0;
		
		if (pixStr != null) {
			pixsize = pixStr.length();
		}
		tmp = tmp - pixsize;
		String ret = "";
		String tmpStr = "0123456789";
		for (int i = 0; i < tmp; i++) {
			ret += tmpStr.charAt(((int) (Math.random() * tmpStr.length())));
		}

		return pixStr + mydate + ret;

	}
    
    
    /**
     * 前端请求BOSS二维码支付接口
     */
    @Override
    public BossQRCodePayInfoVO bossQRCodePayInfo(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    	BossQRCodePayInfoVO bossQRCodePayInfoVO = new BossQRCodePayInfoVO();
        synchronized (this) {
        	String idValue = params.get("idValue");
			if (idValue!=null&&idValue.length()==13) {
				 boolean aa = idValue.startsWith("86");
				if(aa){
					this.logger.info("支付用户编号携带86的"+idValue);
					String bbString = idValue.replaceFirst("86", "");
					this.logger.info("支付用户编号携带86处理过后的"+bbString);
					params.put("idValue", bbString);
				}
			}
			Integer  countBlack = dao.getBlackList(params);
			if(countBlack!=0){
				bossQRCodePayInfoVO.setResultcode("1020");
				bossQRCodePayInfoVO.setResultdesp("该用户不允许订购");
				this.logger.info("黑名单用户不允许订购"+idValue);
				return  bossQRCodePayInfoVO;
			}
            StringBuffer stringBuffer = new StringBuffer();
            String billid = this.getTransactionID("Z", 15);
            stringBuffer.append(SysConfig.getVal("bossQRCodePayUrl")+"?qrOrderId="+billid);
            this.logger.info("请求甘肃移动家开平台二维码支付url：{}", stringBuffer.toString());
            HttpPost method = new HttpPost(stringBuffer.toString());
            JSONObject jsonParam1 = new JSONObject();
            JSONObject jsonParam2 = new JSONObject();
            JSONObject jsonParam3 = new JSONObject();
           
//            jsonParam1.put("userAuthorizationCode", params.get("userAuthorizationCode"));
            jsonParam1.put("idType", params.get("idType"));
            jsonParam1.put("idValue", params.get("idValue"));
            jsonParam1.put("deviceType", params.get("deviceType"));
            jsonParam1.put("deviceId", params.get("deviceId"));
            jsonParam1.put("productId", params.get("productId"));
//            MonthlyParInfoVO monthlyPar = dao.getMonthlyPar();
//            jsonParam1.put("productId", monthlyPar.getDescribe());
            jsonParam1.put("chargeType", "2");
//            if (params.get("productPrice") != null && !params.get("productPrice").equals("")) {
//                jsonParam1.put("productPrice", String.valueOf((Integer.valueOf(params.get("productPrice"))) * 1000));
//            } else {
//                jsonParam1.put("productPrice", "");
//            }

            jsonParam1.put("orderType", "06");
            jsonParam1.put("orderChannel", "04");
            jsonParam1.put("effectiveTime", "-1");
            jsonParam1.put("months", params.get("months"));
            jsonParam1.put("qrOrderId", billid);

            jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(currentTime);
            jsonParam2.put("systemtime", dateString);
            jsonParam2.put("version", params.get("version"));
            jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
            jsonParam2.put("access_token", params.get("access_token"));
            jsonParam3.put("body", jsonParam1.toString());
            jsonParam3.put("header", jsonParam2.toString());
            StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
            this.logger.info("boss二维码请求支付请求参数：{}", jsonParam3.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            HttpResponse result = new DefaultHttpClient().execute(method);

            String content = EntityUtils.toString(result.getEntity());
            this.logger.info("请求甘肃移动家开平台支付url返回的数据：{}", content);
            Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
            Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
            Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));

            bossQRCodePayInfoVO.setInresponseto(resultmapheader.get("inresponseto"));
            bossQRCodePayInfoVO.setSystemtime(resultmapheader.get("systemtime"));
            bossQRCodePayInfoVO.setVersion(resultmapheader.get("version"));
            bossQRCodePayInfoVO.setResultcode(resultmapheader.get("resultcode"));
            bossQRCodePayInfoVO.setResultdesp(resultmapheader.get("resultdesp"));

            bossQRCodePayInfoVO.setUrl(resultmapbody.get("url"));
            bossQRCodePayInfoVO.setQrOrderId(resultmapbody.get("qrOrderId"));
            	
            Integer priceInteger  = Integer.valueOf(params.get("productPrice"))* Integer.valueOf(params.get("months"));
            
            if (resultmapheader.get("resultcode").equals("1000")) {
            	
            	
            	String urlCode = (String) bossQRCodePayInfoVO.getUrl();
    			String qrcode_save_dir = SysConfig.getVal("image_absolute_dir")+SysConfig.getVal("qrcode");
    			 this.logger.info("**********测试生成图片时间速度是否过慢开始************");
    				QRCodeUtil.main(urlCode, qrcode_save_dir, billid);
    				this.logger.info("**********测试生成图片时间速度是否过慢结束************");
            	
    				bossQRCodePayInfoVO.setQrurl(SysConfig.getVal("image_server") +SysConfig.getVal("qrcode")+"/"+billid + ".jpg");
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "0");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee", String.valueOf(priceInteger));
                params2.put("orderNo", billid);
                params2.put("orderId", resultmapbody.get("qrOrderId"));
//                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", "酷乐嘉年华");
                params2.put("monthlyNo", "");
                params2.put("action", params.get("action"));
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 30);
//                Date date = cal.getTime();
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                params2.put("endTime", s.format(date));
              //获取当前月最后一天
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
			    String last = format.format(ca.getTime());
			    System.out.println("===============last:"+last+" 23:59:59");
				params2.put("endTime", last+" 23:59:59");
                params2.put("resultcode", resultmapheader.get("resultcode"));
                params2.put("resultdesp", resultmapheader.get("resultdesp"));
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "2");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
//                dao.insertBillMonthInfos(params2);
//                if(params.get("action")!=null&&!params.get("action").equals("")){
//					this.logger.info("甘肃移动音乐二维码支付回调回调订单action存在插入带Filter表中>>>action值"+params.get("action"));
//					this.dao.insFilter(params.get("idValue"));
//					this.logger.info("甘肃移动音乐二维码支付回调订单action存在插入带Filter表中》》》userId值"+params.get("action"));
//				}
            } else {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "2");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee", String.valueOf(priceInteger));
                params2.put("orderNo", billid);
                params2.put("orderId", resultmapbody.get("orderNumber"));
//                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", "酷乐嘉年华");
                params2.put("monthlyNo", "");
                params2.put("action", params.get("action"));
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 30);
//                Date date = cal.getTime();
//                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                params2.put("endTime", s.format(date));
              //获取当前月最后一天
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
			    String last = format.format(ca.getTime());
			    System.out.println("===============last:"+last+" 23:59:59");
				params2.put("endTime", last+" 23:59:59");
                params2.put("resultcode", resultmapheader.get("resultcode"));
                params2.put("resultdesp", resultmapheader.get("resultdesp"));
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "2");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
            }
        }
        return bossQRCodePayInfoVO;
    }
    
	@Override
	public void zte9QRCodeReturnOrder(Map<String, String> params) {
		// TODO Auto-generated method stub
		 MonthlyParInfoVO monthlyPar = dao.getMonthlyPar();
//		if(SysConfig.getVal("bossProductId").equals(params.get("productId"))){
		 if("".equals(params.get("productId"))||params.get("productId")==null){
			BillMonthLogVO billMonthLogVO = new BillMonthLogVO();
			billMonthLogVO.setOrderNo(params.get("qrOrderId"));
			billMonthLogVO.setOrderId(params.get("orderNumber"));
			billMonthLogVO.setStatus("2");
			billMonthLogVO.setEndTime(params.get("validTo"));
			billMonthLogVO.setResultdesp(params.get("exception"));
			dao.updataBillMopnthLogOrder(billMonthLogVO);
		}else if(params.get("productId").contains(monthlyPar.getProductId())){
			BillMonthLogVO billMonthLogVO = new BillMonthLogVO();
			billMonthLogVO.setOrderNo(params.get("qrOrderId"));
			billMonthLogVO.setOrderId(params.get("orderNumber"));
			billMonthLogVO.setStatus("1");
			billMonthLogVO.setEndTime(params.get("validTo"));
			dao.updataBillMopnthLogOrder(billMonthLogVO);
			billMonthLogVO = this.dao.getBillMonthLogInfo(params.get("qrOrderId"));
			if(billMonthLogVO!=null&&"0".equals(billMonthLogVO.getStatus())){
				dao.insertBillMonthQRCodeInfos(billMonthLogVO);
			}
		}
		 
	}
	
	 /**
     * 前端请求后台根据OrderId  拿到二维码支付结果
     */
    @Override
    public OrderReturnVO getQRReturnUrlPayStatus(Map<String, String> params) {
        // TODO Auto-generated method stub
        OrderReturnVO orderReturnVO = new OrderReturnVO();
        BillMonthLogVO hnyxBillMonthLogVO = dao.getMonthOrderInfoCode(params);
        if (hnyxBillMonthLogVO != null) {
            orderReturnVO.setOrderCode(hnyxBillMonthLogVO.getStatus());
            orderReturnVO.setOrderDesc(hnyxBillMonthLogVO.getStatusDesc());
        } else {
            orderReturnVO.setOrderCode("b");
            orderReturnVO.setOrderDesc("该订单不存在");
        }
        return orderReturnVO;
    }
    

    
    
    /**
	 *前端请求BOSS退订接口
	 */
	@Override
	public BossPayInfoVO bossUnsubscribePayInfo(Map<String, String> params) throws Exception{
		// TODO Auto-generated method stub
		BossPayInfoVO bossPayInfoVO = new BossPayInfoVO();
		synchronized(this) { 
		StringBuffer stringBuffer = new StringBuffer();
		String billid = this.getTradeNumber();
		 stringBuffer.append(SysConfig.getVal("bossPayUrl"));
		 this.logger.info("请求甘肃移动家开平台退订url：{}", stringBuffer.toString());
		 	HttpPost method = new HttpPost(stringBuffer.toString());  
			JSONObject jsonParam1 = new JSONObject();
			JSONObject jsonParam2 = new JSONObject();
			JSONObject jsonParam3 = new JSONObject();
			
			String idValue = params.get("idValue");
			jsonParam1.put("userAuthorizationCode", params.get("userAuthorizationCode"));
			jsonParam1.put("idType", params.get("idType"));
			if (idValue!=null&&idValue.length()==13) {
				 boolean aa = idValue.startsWith("86");
				if(aa){
					this.logger.info("退订用户编号携带86的"+idValue);
					String bbString = idValue.replaceFirst("86", "");
					this.logger.info("退订用户编号携带86处理过后的"+bbString);
					params.put("idValue", bbString);
				}
			}
			jsonParam1.put("idValue", params.get("idValue"));
			jsonParam1.put("deviceType", params.get("deviceType"));
			jsonParam1.put("deviceId", params.get("deviceId"));
			List<MonthlyParInfoVO> list = dao.getProductlist();
			if(list!=null && list.size()>0 && list.get(0)!=null){
				   jsonParam1.put("productId", list.get(0).getProductId());
				   if(list.get(0).getPrice()!=null&&!list.get(0).getPrice().equals("")){
						jsonParam1.put("productPrice", String.valueOf((Integer.valueOf(list.get(0).getPrice()))*1000)); 
					}else {
						jsonParam1.put("productPrice", "");
					}
			}
//			if(params.get("productPrice")!=null&&!params.get("productPrice").equals("")){
//				jsonParam1.put("productPrice", String.valueOf((Integer.valueOf(params.get("productPrice")))*1000)); 
//			}else {
//				jsonParam1.put("productPrice", "");
//			}
			jsonParam1.put("chargeType", "2");
			jsonParam1.put("orderType", "07");
			jsonParam1.put("orderChannel", "04"); 
			jsonParam1.put("effectiveTime", "-1");
			
			jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			  String dateString = formatter.format(currentTime);
			jsonParam2.put("systemtime", dateString);
			jsonParam2.put("version", params.get("version"));
			jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
			jsonParam2.put("access_token", params.get("access_token"));
			jsonParam3.put("body", jsonParam1.toString());
			jsonParam3.put("header", jsonParam2.toString());
			StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
			 this.logger.info("boss请求退订请求参数：{}", jsonParam3.toString());
			entity.setContentEncoding("UTF-8");    
			entity.setContentType("application/json");
			method.setEntity(entity);  
			HttpResponse result = new DefaultHttpClient().execute(method);
			
			 String content = EntityUtils.toString(result.getEntity());  
			 this.logger.info("请求甘肃移动家开平台退订url返回的数据：{}", content);
			 Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
			 Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
			 Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));
			 
			 bossPayInfoVO.setInresponseto(resultmapheader.get("inresponseto"));
			 bossPayInfoVO.setSystemtime(resultmapheader.get("systemtime"));
			 bossPayInfoVO.setVersion(resultmapheader.get("version"));
			 bossPayInfoVO.setResultcode(resultmapheader.get("resultcode"));
			 bossPayInfoVO.setResultdesp(resultmapheader.get("resultdesp"));
			 
			 bossPayInfoVO.setOrderNumber(resultmapbody.get("orderNumber"));
			 bossPayInfoVO.setOrderTime(resultmapbody.get("orderTime"));
			 bossPayInfoVO.setValidFrom(resultmapbody.get("validFrom"));
			 bossPayInfoVO.setValidTo(resultmapbody.get("validTo"));
			 
			 if(resultmapheader.get("resultcode").equals("1000")){
					Map<String, String> params2 =new HashMap<String, String> ();
					params2.put("orderNo", billid);
			    	params2.put("transactionID", resultmapbody.get("orderNumber"));
					params2.put("status", "1");
			    	params2.put("phone", params.get("idValue"));
			    	params2.put("userId", params.get("userId"));
			    	params2.put("productID", params.get("productId"));
			    	params2.put("productName", "酷乐嘉年华");
			    	params2.put("total_fee",  params.get("productPrice"));
					params2.put("monthlyName", "游戏大厅");
					params2.put("monthlyNo", "");
					params2.put("resultcode", resultmapheader.get("resultcode"));
					params2.put("resultdesp", resultmapheader.get("resultdesp"));
					
					
					dao.saveUnsubscribePayInfo(params2);
			 }
		}
			 return bossPayInfoVO;
	}



    //包月参数管理表格
    @Override
    public MonthlyParInfoVO getMonthlyPar() {
        // TODO Auto-generated method stub
        MonthlyParInfoVO monthlyPar = dao.getMonthlyPar();
        return monthlyPar;
    }


    @Override
    public List<MonthlyParInfoVO> list() {
        // TODO Auto-generated method stub
        List<MonthlyParInfoVO> list = dao.list();
        return list;
    }


    //查询价格
    @Override
    public String getPrice() {
        // TODO Auto-generated method stub
        return this.dao.getPrice();
    }
    
    
    @Override
	public void timingOrderAuthInfo() throws Exception {
		// TODO Auto-generated method stub
		List<MonthBill> getMonthInfos = dao.getMonthInfos();
		this.logger.info("续订插入中九-业务鉴权请求甘肃移动家开平台插入数量统计总共数量"+String.valueOf(getMonthInfos.size()));
		int i=0;
		for(MonthBill monthBill:getMonthInfos){
			i=i+1;
			this.logger.info("续订插入中九-业务鉴权请求甘肃移动家开平台插入数量统计"+String.valueOf(i));
			System.out.println("续订插入中九-业务鉴权请求甘肃移动家开平台插入数量统计"+String.valueOf(i));
			 StringBuffer stringBuffer = new StringBuffer();
			 stringBuffer.append(SysConfig.getVal("bossAuthUrl"));
			 this.logger.info("续订插入中九-业务鉴权请求甘肃移动家开平台url："+stringBuffer.toString());
			 System.out.println("续订插入中九-业务鉴权请求甘肃移动家开平台url："+stringBuffer.toString());
			 	HttpPost method = new HttpPost(stringBuffer.toString());  
				JSONObject jsonParam1 = new JSONObject();
				JSONObject jsonParam2 = new JSONObject();
				JSONObject jsonParam3 = new JSONObject();
				Map<String, String>  tokenMap = new HashMap<String, String>();
				List<MonthlyParInfoVO> list = dao.getProductlist();
				if(list!=null && list.size()>0 && list.get(0)!=null){
					   tokenMap.put("powerkey", list.get(0).getPowerKey());
					}
				TOkenVO tOkenVO = this.bossGetToken(tokenMap);
				
				if(tOkenVO.getResult()!=null&&tOkenVO.getResult().equals("1000")){
				jsonParam1.put("idType", "0");
				jsonParam1.put("idValue", monthBill.getPhone());
				jsonParam1.put("deviceType", "3");
				jsonParam1.put("deviceId", monthBill.getDeviceId());
				if(list!=null && list.size()>0 && list.get(0)!=null){
					   jsonParam1.put("productIds", list.get(0).getProductId());
					}
				jsonParam1.put("contentId", "");
				jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
				String dateString = formatter.format(currentTime);
				jsonParam2.put("systemtime", dateString);
				jsonParam2.put("version", "106");
				jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
				jsonParam2.put("access_token", tOkenVO.getData());
				jsonParam3.put("body", jsonParam1.toString());
				jsonParam3.put("header", jsonParam2.toString());
				StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
				 this.logger.info("续订插入中九-boss鉴权请求参数：{}"+jsonParam3.toString());
				 System.out.println("续订插入中九-boss鉴权请求参数：{}"+jsonParam3.toString());
				entity.setContentEncoding("UTF-8");    
	           entity.setContentType("application/json");    
	           method.setEntity(entity);  
	           HttpResponse result = new DefaultHttpClient().execute(method);
				
				String content = EntityUtils.toString(result.getEntity());  
				 this.logger.info("续订插入中九-业务鉴权请求甘肃移动家开平台url返回的数据："+content);
				 System.out.println("续订插入中九-业务鉴权请求甘肃移动家开平台url返回的数据："+content);
				 Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
				 Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
				 Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));
				 if(resultmapheader.get("resultcode")!=null&&resultmapheader.get("resultcode").equals("1000")){
					 String billid = this.getTradeNumber();
						Map<String, String> params2 =new HashMap<String, String> ();
						params2.put("status", "1");
				    	params2.put("phone", monthBill.getPhone());
				    	params2.put("userId", monthBill.getUserId());
				    	params2.put("productName", "");
				    	params2.put("total_fee",  list.get(0).getPrice());
				    	params2.put("orderNo", billid);
				    	params2.put("orderId", resultmapbody.get("orderNumber"));
//				    	MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
						params2.put("monthlyName", "酷乐佳年华");
						params2.put("monthlyNo", "");
						params2.put("productID", list.get(0).getProductId());
//						Calendar cal = Calendar.getInstance();
//						cal.add(Calendar.DATE, 30);
//						Date date = cal.getTime();
//						SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						 //获取当前月最后一天
				        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				        Calendar ca = Calendar.getInstance();    
				        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
					    String last = format.format(ca.getTime());
					    System.out.println("===============last:"+last+" 23:59:59");
						params2.put("endTime", last+" 23:59:59");
						params2.put("resultcode", resultmapheader.get("resultcode"));
						params2.put("resultdesp", resultmapheader.get("resultdesp"));
						params2.put("deviceId", monthBill.getDeviceId());
						params2.put("isParent", "2");
						params2.put("payStatus", "1");
						params2.put("productId", "productId");
						Integer  countBYPPhone =dao.getCountBYPPone(monthBill.getPhone());
						if(countBYPPhone==0){
							dao.saveMonthlyLogInfo(params2);
							dao.insertBillMonthInfos(params2);
						}
				 }
				}
		}
		
		
	}
    
    
    
    /**
     * 前端请求BOSS积分查询接口
     */
    @Override
    public BossIntegralQueryReturnVO bossIntegralQuery(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    	BossIntegralQueryReturnVO bossIntegralQueryReturnVO = new BossIntegralQueryReturnVO();
        synchronized (this) {
        	String idValue = params.get("idValue");
			if (idValue!=null&&idValue.length()==13) {
				 boolean aa = idValue.startsWith("86");
				if(aa){
					this.logger.info("请求用户编号携带86的"+idValue);
					String bbString = idValue.replaceFirst("86", "");
					this.logger.info("请求用户编号携带86处理过后的"+bbString);
					params.put("idValue", bbString);
				}
			}
		
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(SysConfig.getVal("bossIntegralQueryUrl"));
            this.logger.info("请求甘肃移动家开平台积分查询url：{}", stringBuffer.toString());
            HttpPost method = new HttpPost(stringBuffer.toString());
            JSONObject jsonParam1 = new JSONObject();
            JSONObject jsonParam2 = new JSONObject();
            JSONObject jsonParam3 = new JSONObject();
           
            jsonParam1.put("idValue", params.get("idValue"));


            jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(currentTime);
            jsonParam2.put("systemtime", dateString);
            jsonParam2.put("version", params.get("version"));
            jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
            jsonParam2.put("access_token", params.get("access_token"));
            jsonParam3.put("body", jsonParam1.toString());
            jsonParam3.put("header", jsonParam2.toString());
            StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
            this.logger.info("boss积分查询接口请求参数：{}", jsonParam3.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            HttpResponse result = new DefaultHttpClient().execute(method);

            String content = EntityUtils.toString(result.getEntity());
            this.logger.info("请求甘肃移动家开积分查询接口返回的数据：{}", content);
            Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
            Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
            Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));
            
            bossIntegralQueryReturnVO.setTotalPoint(resultmapbody.get("totalPoint"));
            bossIntegralQueryReturnVO.setInresponseto(resultmapheader.get("inresponseto"));
            bossIntegralQueryReturnVO.setSystemtime(resultmapheader.get("systemtime"));
            bossIntegralQueryReturnVO.setVersion(resultmapheader.get("version"));
            bossIntegralQueryReturnVO.setResultcode(resultmapheader.get("resultcode"));
            bossIntegralQueryReturnVO.setResultdesp(resultmapheader.get("resultdesp"));
        }
        return bossIntegralQueryReturnVO;
    }
    
    
    /**
     * 前端请求BOSS积分支付接口
     */
    @Override
    public BossIntegralPayInfoVO bossIntegralPayInfo(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    	BossIntegralPayInfoVO bossIntegralPayInfoVO = new BossIntegralPayInfoVO();
        synchronized (this) {
        	String idValue = params.get("idValue");
			if (idValue!=null&&idValue.length()==13) {
				 boolean aa = idValue.startsWith("86");
				if(aa){
					this.logger.info("支付用户编号携带86的"+idValue);
					String bbString = idValue.replaceFirst("86", "");
					this.logger.info("支付用户编号携带86处理过后的"+bbString);
					params.put("idValue", bbString);
				}
			}
			Integer  countBlack = dao.getBlackList(params);
			if(countBlack!=0){
				bossIntegralPayInfoVO.setResultcode("1020");
				bossIntegralPayInfoVO.setResultdesp("该用户不允许订购");
				this.logger.info("黑名单用户不允许订购"+idValue);
				return  bossIntegralPayInfoVO;
			}
            StringBuffer stringBuffer = new StringBuffer();
            String billid = this.getTradeNumber();
            stringBuffer.append(SysConfig.getVal("bossIntegralPayUrl"));
            this.logger.info("请求甘肃移动家开平台二维码支付url：{}", stringBuffer.toString());
            HttpPost method = new HttpPost(stringBuffer.toString());
            JSONObject jsonParam1 = new JSONObject();
            JSONObject jsonParam2 = new JSONObject();
            JSONObject jsonParam3 = new JSONObject();
           
            jsonParam1.put("idType", params.get("idType"));
            jsonParam1.put("idValue", params.get("idValue"));
            jsonParam1.put("deviceType", params.get("deviceType"));
            jsonParam1.put("deviceId", params.get("deviceId"));
            jsonParam1.put("productId", params.get("productId"));
//            MonthlyParInfoVO monthlyPar = dao.getMonthlyPar();
//            jsonParam1.put("productId", monthlyPar.getDescribe());
            jsonParam1.put("chargeType", "2");

            jsonParam1.put("orderType", "06");
            jsonParam1.put("orderChannel", "04");
            jsonParam1.put("effectiveTime", "-1");
            jsonParam1.put("months", params.get("months"));

            jsonParam2.put("msgid", SysConfig.getVal("boosZte9msgid"));
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(currentTime);
            jsonParam2.put("systemtime", dateString);
            jsonParam2.put("version", params.get("version"));
            jsonParam2.put("sourceid", SysConfig.getVal("bossSourceid"));
            jsonParam2.put("access_token", params.get("access_token"));
            jsonParam3.put("body", jsonParam1.toString());
            jsonParam3.put("header", jsonParam2.toString());
            StringEntity entity = new StringEntity(jsonParam3.toString(), "UTF-8");
            this.logger.info("boss积分请求支付请求参数：{}", jsonParam3.toString());
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            method.setEntity(entity);
            HttpResponse result = new DefaultHttpClient().execute(method);

            String content = EntityUtils.toString(result.getEntity());
            this.logger.info("请求甘肃移动家开平台积分支付url返回的数据：{}", content);
            Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
            Map<String, String> resultmapheader = JsonPluginsUtil.jsonToMap(resultmap.get("header"));
            Map<String, String> resultmapbody = JsonPluginsUtil.jsonToMap(resultmap.get("body"));

            bossIntegralPayInfoVO.setInresponseto(resultmapheader.get("inresponseto"));
            bossIntegralPayInfoVO.setSystemtime(resultmapheader.get("systemtime"));
            bossIntegralPayInfoVO.setVersion(resultmapheader.get("version"));
            bossIntegralPayInfoVO.setResultcode(resultmapheader.get("resultcode"));
            bossIntegralPayInfoVO.setResultdesp(resultmapheader.get("resultdesp"));

            bossIntegralPayInfoVO.setOrderNumber(resultmapbody.get("orderNumber"));
            bossIntegralPayInfoVO.setOrderTime(resultmapbody.get("orderTime"));
            bossIntegralPayInfoVO.setValidFrom(resultmapbody.get("validFrom"));
            bossIntegralPayInfoVO.setValidTo(resultmapbody.get("validTo"));
            	
            Integer priceInteger  = Integer.valueOf(params.get("productPrice"))* Integer.valueOf(params.get("months"));
            
            if (resultmapheader.get("resultcode").equals("1000")) {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "1");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee", String.valueOf(priceInteger));
                params2.put("orderNo", billid);
                params2.put("orderId", resultmapbody.get("orderNumber"));
                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
                params2.put("monthlyNo", monthlyGameCategoryVo.getId());
                params2.put("action", params.get("action"));
				params2.put("endTime", resultmapbody.get("validTo"));
                params2.put("resultcode", resultmapheader.get("resultcode"));
                params2.put("resultdesp", resultmapheader.get("resultdesp"));
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "3");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
                dao.insertBillMonthInfos(params2);
            } else {
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "2");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee",String.valueOf(priceInteger));
                params2.put("orderNo", billid);
                params2.put("orderId", resultmapbody.get("orderNumber"));
                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
                params2.put("monthlyNo", monthlyGameCategoryVo.getId());
                params2.put("action", params.get("action"));
				params2.put("endTime", resultmapbody.get("validTo"));
                params2.put("resultcode", resultmapheader.get("resultcode"));
                params2.put("resultdesp", resultmapheader.get("resultdesp"));
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "3");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
            }
        }
        return bossIntegralPayInfoVO;
    }




  
    
}
