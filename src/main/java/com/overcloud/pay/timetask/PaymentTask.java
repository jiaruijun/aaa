package com.overcloud.pay.timetask;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.overcloud.pay.service.OrderInfoService;

@Component
public class PaymentTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    OrderInfoService service;
    
    @Scheduled(cron = "0 15 1 4 * ?")
    public void timingOrderAuthInfo() {
        this.logger.info("每个月4号1点15定时查询上个月支付的数据,然后鉴权查询是否续订");
        this.logger.info(new Date().toString());
        try {
        	this.service.timingOrderAuthInfo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
}
