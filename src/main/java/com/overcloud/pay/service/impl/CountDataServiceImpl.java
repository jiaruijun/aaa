package com.overcloud.pay.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.overcloud.pay.dao.CountDataDao;
import com.overcloud.pay.service.CountDataService;
import com.overcloud.pay.vo.CountDataVO;

@Service
public class CountDataServiceImpl implements CountDataService{
	

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static long num = 1;

	@Resource
	CountDataDao dao;
	
	
	
	
	
	/**
	 * 查询该系统下的统计数据
	 */
	@Override
	public void selectCountData(){
		// TODO Auto-generated method stub
		try {
			CountDataVO  countDataVO = new CountDataVO();
			countDataVO = dao.getCountData();
			Integer countUserPay = dao.getCountUserPay();
			
			if(Integer.valueOf(countDataVO.getClickRens())==0){
				countDataVO.setTotal_fee("0");
			}else {
				double a= countUserPay;
				double b= Integer.valueOf(countDataVO.getClickRens());
				double c = a/b;
				double d = c*100;
				System.out.println(c);
			    BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
				this.logger.info("算出的转化率为" + d);
				countDataVO.setTotal_fee(String.valueOf(bg));
			}
			
			
			countDataVO.setDingg(String.valueOf(countUserPay));
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Date day = new Date();
		    long ms = day.getTime() - 1*24*3600*1000L;
		    Date prevDay = new Date(ms);
		    String createTime = sdf.format(prevDay);
		    this.logger.info("得到的前一天日期为" + sdf.format(prevDay));
		    countDataVO.setCreateTime(createTime);
		    
		    StringBuffer stringBuffer = new StringBuffer();
		    stringBuffer.append("http://47.97.204.145:8088/countData_api/disney/insertCountData").append("?");
		    stringBuffer.append("createTime=").append(countDataVO.getCreateTime());
		    stringBuffer.append("&clickRens=").append(countDataVO.getClickRens());
		    stringBuffer.append("&clicks=").append(countDataVO.getClicks());
		    stringBuffer.append("&dingg=").append(countDataVO.getDingg());
		    stringBuffer.append("&total_fee=").append(URLEncoder.encode(countDataVO.getTotal_fee(), "UTF-8"));
		    stringBuffer.append("&productID=").append("1");
		    stringBuffer.append("&productName=").append(URLEncoder.encode(URLEncoder.encode("甘肃移动酷乐嘉年华","UTF-8"),"UTF-8"));
		    stringBuffer.append("&cpId=").append("CP007");
		    this.logger.info("发送地区统计数据请求地址：{}", stringBuffer.toString());
		    HttpClient httpClient = new DefaultHttpClient();
		     HttpGet httpGet = new HttpGet(stringBuffer.toString());
		     HttpResponse response = httpClient.execute(httpGet);
		     int code = response.getStatusLine().getStatusCode();
		        if (code != HttpServletResponse.SC_OK && code != HttpServletResponse.SC_NOT_MODIFIED) {
		            this.logger.error("发送地区统计数据异常：{}", code);
		        }
	        String content = EntityUtils.toString(response.getEntity(), "utf-8");
	        this.logger.info("发送地区统计数据返回：{}", content);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
	}

	

}
