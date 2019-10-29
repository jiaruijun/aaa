package com.overcloud.pay.dao;

import org.springframework.stereotype.Repository;

import com.overcloud.pay.vo.CountDataVO;

@Repository
public interface CountDataDao {
	
	//查询该系统前一天的PVUV
	CountDataVO getCountData();
	
	//查询系统前一天的订购量
	Integer getCountUserPay();

}
