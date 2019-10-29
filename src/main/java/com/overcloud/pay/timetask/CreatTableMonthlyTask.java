package com.overcloud.pay.timetask;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.overcloud.pay.service.CountDataService;

@Component
public class CreatTableMonthlyTask {

    private static Logger logger = LoggerFactory.getLogger(CreatTableMonthlyTask.class);

    @Resource
    private CountDataService countDataService;

    /**
     * 定时每天凌晨2点查询前一天的统计数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void createTable() {
        try {
            this.countDataService.selectCountData();
            logger.info("查询前一天统计数据执行成功");
        }
        catch (Exception e) {
            logger.error("查询前一天统计数据执行失败", e);
        }
    }
}
