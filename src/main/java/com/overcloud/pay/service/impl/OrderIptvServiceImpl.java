package com.overcloud.pay.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.overcloud.pay.common.JsonPluginsUtil;
import com.overcloud.pay.common.SysConfig;
import com.overcloud.pay.common.encrypt.MD5Util;
import com.overcloud.pay.dao.OrderInfoDao;
import com.overcloud.pay.service.OrderIptvService;
import com.overcloud.pay.vo.BossPayInfoVO;
import com.overcloud.pay.vo.IptvBossAuthMonthlyUserVO;
import com.overcloud.pay.vo.IptvCodeExchangeReturnVO;
import com.overcloud.pay.vo.IptvCodeResultReturnVO;
import com.overcloud.pay.vo.MonthlyGameCategoryVo;

@Service
public class OrderIptvServiceImpl implements OrderIptvService{
	
	 private final Logger logger = LoggerFactory.getLogger(getClass());

	    private static long num = 1;


	    @Resource
	    OrderInfoDao dao;
	
	 /**
     * 根据广电长code换取帕科短code
     */
    @Override
    public IptvCodeExchangeReturnVO getIptvCodeExchange(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    	IptvCodeExchangeReturnVO iptvCodeExchangeReturnVO = new IptvCodeExchangeReturnVO();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SysConfig.getVal("pakeCodeZHUrl"));
        this.logger.info("根据广电长code换取帕科短codeurl：{}", stringBuffer.toString());
        HttpPost method = new HttpPost(stringBuffer.toString());
        JSONObject jsonParam1 = new JSONObject();
        jsonParam1.put("format", "json");
        jsonParam1.put("portalcode", "PCODE-t2gwbgzp");
        jsonParam1.put("sptoken", "4F673775CD9E51276730A9E8EA7EA9D2");
        jsonParam1.put("pageindex", "1");
        jsonParam1.put("orderinfo", "1");
        jsonParam1.put("tmtype", "1");
        jsonParam1.put("contenttype", params.get("contenttype"));
        jsonParam1.put("contentcode",params.get("contentcode"));

        StringEntity entity = new StringEntity(jsonParam1.toString(), "UTF-8");
        this.logger.info("根据广电长code换取帕科短code请求参数：{}", jsonParam1.toString());
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        method.setEntity(entity);
        HttpResponse result = new DefaultHttpClient().execute(method);

        String content = EntityUtils.toString(result.getEntity());
        this.logger.info("根据广电长code换取帕科短code返回的数据：{}", content);
        Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
        iptvCodeExchangeReturnVO.setResultCode(resultmap.get("resultcode"));
        iptvCodeExchangeReturnVO.setResultDesc(resultmap.get("resultdesc"));
//        if(resultmap.get("result")!=null&&!resultmap.get("result").equals("")){
//        	String result1 = resultmap.get("result").replaceAll("null", "\"\"");
//        	List<IptvCodeResultReturnVO> iptvCodeResultReturnVOs = JsonPluginsUtil.jsonToBeanList(result1, IptvCodeResultReturnVO.class);
//        	iptvCodeExchangeReturnVO.setResult(iptvCodeResultReturnVOs);
//        }
//        
        if(resultmap.get("result")!=null&&!resultmap.get("result").equals("")){
        List<IptvCodeResultReturnVO> list = new ArrayList<IptvCodeResultReturnVO>();
        list = JSON.parseArray(resultmap.get("result"), IptvCodeResultReturnVO.class);
        iptvCodeExchangeReturnVO.setResult(list);
        }
        return iptvCodeExchangeReturnVO;
    }
    
    
    /**
     * IPtv请求帕科鉴权接口
     */
    @Override
    public IptvBossAuthMonthlyUserVO iptvBossMonthlyAuth(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    	IptvBossAuthMonthlyUserVO iptvBossAuthMonthlyUserVO = new IptvBossAuthMonthlyUserVO();
    	StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(SysConfig.getVal("pakeAuthUrl")).append("?");
	    stringBuffer.append("mac=").append(params.get("mac"));
	    stringBuffer.append("&stbid=").append(params.get("stbid"));
	    stringBuffer.append("&userid=").append(params.get("userid"));
	    stringBuffer.append("&billingcode=").append(params.get("billingcode"));
	    stringBuffer.append("&contentid=").append(SysConfig.getVal("pakeZte9ShortCode"));
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String  shijianString = sdf.format(d);
	    String token = MD5Util.MD5Encode(sdf.format(d)+"PukkaAndGSYD", "UTF-8");
	    stringBuffer.append("&token=").append(token.toUpperCase());
	    stringBuffer.append("&timeshift=").append(shijianString);
	    stringBuffer.append("&spsign=").append("PukkaAndGSYD");
	    this.logger.info("IPtv请求帕科鉴权接口请求地址：{}", stringBuffer.toString());
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(stringBuffer.toString());
	    HttpResponse response = httpClient.execute(httpGet);
	    int code = response.getStatusLine().getStatusCode();
        if (code != HttpServletResponse.SC_OK && code != HttpServletResponse.SC_NOT_MODIFIED) {
        		this.logger.error("IPtv请求帕科鉴权接口数据异常：{}", code);
        }
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        this.logger.info("IPtv请求帕科鉴权接口返回的数据：{}", content);
        Map<String, String> resultmap = JsonPluginsUtil.jsonToMap(content);
        iptvBossAuthMonthlyUserVO.setResult(resultmap.get("result"));
        iptvBossAuthMonthlyUserVO.setAuthinfo(resultmap.get("authinfo"));
        iptvBossAuthMonthlyUserVO.setDescribe(resultmap.get("describe"));
        iptvBossAuthMonthlyUserVO.setProductlist(resultmap.get("productlist"));
        iptvBossAuthMonthlyUserVO.setProducturl(resultmap.get("producturl"));
        iptvBossAuthMonthlyUserVO.setContentid(SysConfig.getVal("pakeZte9ShortCode"));
        return iptvBossAuthMonthlyUserVO;
    }
    
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
     * 前端请求BOSS支付接口
     */
    @Override
    public BossPayInfoVO iptvPayInfo(Map<String, String> params) throws Exception {
        // TODO Auto-generated method stub
    			String billid = this.getTradeNumber();
        		BossPayInfoVO bossPayInfoVO = new BossPayInfoVO();
                Map<String, String> params2 = new HashMap<String, String>();
                params2.put("status", "0");
                params2.put("phone", params.get("idValue"));
                params2.put("userId", params.get("userId"));
                params2.put("productName", "");
                params2.put("total_fee", params.get("productPrice"));
                params2.put("orderNo", billid);
                MonthlyGameCategoryVo monthlyGameCategoryVo = dao.getMonthlyGameCategoryInfo();
                params2.put("monthlyName", monthlyGameCategoryVo.getMonthlyCategoryName());
                params2.put("monthlyNo", monthlyGameCategoryVo.getId());
                params2.put("action", params.get("action"));
		        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		        Calendar ca = Calendar.getInstance();    
		        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
			    String last = format.format(ca.getTime());
			    System.out.println("===============last:"+last+" 23:59:59");
				params2.put("endTime", last+" 23:59:59");
                params2.put("deviceId", params.get("deviceId"));
				params2.put("isParent", "1");
				params2.put("payStatus", "1");
				params2.put("productId", params.get("productId"));
                dao.saveMonthlyLogInfo(params2);
                bossPayInfoVO.setOrderNumber(billid);
                bossPayInfoVO.setResultcode("0");
                bossPayInfoVO.setResultdesp("下单成功");
                bossPayInfoVO.setReturnUrl(SysConfig.getVal("zte9ReturnUrl")+billid);
        return bossPayInfoVO;
    }

}
