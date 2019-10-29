package com.overcloud.pay.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.overcloud.pay.common.AddressUtils;
import com.overcloud.pay.dao.OrderInfoDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.overcloud.pay.dao.IUserRecordDao;
import com.overcloud.pay.entity.PercentList;
import com.overcloud.pay.entity.UserControllers;
import com.overcloud.pay.service.UserRecordService;
import com.overcloud.pay.vo.UserActionVo;

@Service
public class UserRecordServiceImpl implements UserRecordService{
    @Resource
    IUserRecordDao userRecordDao;
    @Resource
    OrderInfoDao dao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserActionVo getUserAction(Map<String, String> paramMap) {
        // 判断用户action
        UserActionVo userVo = new UserActionVo();
        userVo.setAction("99");
        // 读取contrl表中第一条记录的enbale字段
        UserControllers controller = userRecordDao.selectContrller();
        if (controller == null) {
            this.logger.info("controller==nullreturn 99");
            userVo.setAction("99");
            return userVo; 
        }
        
        AddressUtils addressUtils = new AddressUtils();
        String ip = paramMap.get("ipAddr");
        String address = "";
        try {
            address = addressUtils.getAddresses("ip=" + ip, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.logger.info("该IP的地区为"+address);
        if(address==null||"".equals(address)||"null".equals(address)){
          	 this.logger.info("该IP获取的地址为空return 99");
               userVo.setAction("99");
               return userVo; 
          }
        if("兰州".equals(address)){
        	 this.logger.info("该IP获取的地址为兰州return 99");
             userVo.setAction("99");
             return userVo; 
        }
        
        String userId= paramMap.get("userId");
    	if(userId.contains("139")||userId.contains("0931")){
    		this.logger.info("包含139 和0931return 99");
            userVo.setAction("99");
            return userVo;
		}
    	
//    	Date bdate = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(bdate);
//        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
//        	this.logger.info("今天为周六日 不执行工作时间判断");
//        }else {
//        	this.logger.info("今天为工作日 执行工作时间判断");
//        
//        
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
//		//初始化
//		Date nowTime =null;
//		Date beginTime = null;
//		Date endTime = null;
//		try {
//		//格式化当前时间格式
//		nowTime = df.parse(df.format(new Date()));
//		//定义开始时间
//		beginTime = df.parse("08:30");
//		//定义结束时间
//		endTime = df.parse("18:30");
//		} catch (Exception e) {
//		e.printStackTrace();
//		}
//		//调用判断方法
//		boolean flag = belongCalendar(nowTime, beginTime, endTime);
//		//输出为结果
//		if(flag){
//			 	this.logger.info("时间为工作时间 return 99");
//	            userVo.setAction("99");
//	            return userVo;
//		}
//        };
    	
    	
    	

        Integer counUserIdPay  = userRecordDao.counUserIdPay(paramMap.get("userId"));
        this.logger.info("查看该用户最近一个月有没有支付过"+paramMap.get("userId")+"------"+counUserIdPay);
        if (counUserIdPay!=0) {
            this.logger.info("最近一个月 支付过 返回 return 99");
            userVo.setAction("99");
            return userVo;
        }

        this.logger.info("aaaction controller enable " + controller.getEnable() + " type " + controller.getType());
        if ("0".equals(controller.getEnable())) {
            userVo.setAction("99");
            this.logger.info("enable==0return 99");
            return userVo;
        }

        if ( paramMap.get("userId").length() < 8 ) {
            userVo.setAction("99");
            this.logger.info("aaactionuserId长度13return 99");
            return userVo;
        }

        // 判断filter表中是否存在userid的记录（查询userid字段）
        String userIdString=paramMap.get("userId");
        if (userIdString!=null&&userIdString.length()==13) {
			 boolean aa = userIdString.startsWith("86");
			if(aa){
				this.logger.info("支付用户编号携带86的"+userIdString);
				String bbString = userIdString.replaceFirst("86", "");
				this.logger.info("支付用户编号携带86处理过后的"+bbString);
				paramMap.put("userId", bbString);
			}
		}
        Integer userNumberCount = 0;
        userNumberCount = userRecordDao.selectCountByUserId(paramMap.get("userId"));
        this.logger.info("aaaction 通过userId查询出来的记录数是" + userNumberCount);
        if (userNumberCount > 0) {
            userVo.setAction("99");
            this.logger.info("aaactionuserNumberCount>0return99");
            return userVo;
        }

        if (paramMap.get("ipAddr").contains("192.16") || paramMap.get("ipAddr").startsWith("10.")) {
            userVo.setAction("99");
            this.logger.info("aaaipAddr10.161.return99或者10.开头");
            return userVo;
        }

        // 判断filter表中是否存在host地址的记录（同样查询userid字段,插入的时候讲IP地址作为userid字段插入)
        Integer ipNumberCount = 0;
        ipNumberCount = userRecordDao.selectCountByUserId(paramMap.get("ipAddr"));
        this.logger.info("aaaction 通过ip查询出来的记录数是" + ipNumberCount);
        if (ipNumberCount > 0) {
            userVo.setAction("99");
            this.logger.info("ipNumberCount>0return99");
            return userVo;
        }

        // 读取contrl表中第一条记录的type字段
        String type = controller.getType();

        String actionString= paramMap.get("action");
        if ( !type.equals(actionString) ){
            this.logger.info("aaaction type not macth" + actionString);
            userVo.setAction("99");
            return userVo;
        }

        // 根据type的值，查询percentList表，并返回一个Percent对象
        PercentList percentList = userRecordDao.selectPercentList(type);
        this.logger.info("aaaction percent " + percentList.getPercent());
        if(percentList == null){
            userVo.setAction("99");
            this.logger.info("percentList==nullreturn99");
            return userVo;
        }
        Integer percent = Integer.valueOf(percentList.getPercent());
        if (this.check(percent)) {
            this.logger.info("aaaction check return true");
            userVo.setAction("1");
            return userVo;
        }

        return userVo;
    }

 

//    public boolean check(int percent) {
//        int r = (int) (Math.random() * 100);
//
//        return (r % (100 / percent) == 0);
//    }
    
    
    
    /**
	     * 判断时间是否在时间段内
	     * @param nowTime
	     * @param beginTime
	     * @param endTime
	     * @return
	     */
		public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
		//设置当前时间
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		//设置开始时间
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginTime);
		//设置结束时间
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		//处于开始时间之后，和结束时间之前的判断
		if (date.after(begin) && date.before(end)) {
		return true;
		} else {
		return false;
		}
		}
    
    
	public boolean check(int percent) {
		int r = (int) (Math.random() * 100) + 1;
		boolean res = ( r <= percent );
		this.logger.info("check percent " + percent + " " + res);
		return res;
	}


}
