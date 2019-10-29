package com.overcloud.pay.dao;

import org.springframework.stereotype.Repository;

import com.overcloud.pay.entity.PercentList;
import com.overcloud.pay.entity.UserControllers;

@Repository
public interface IUserRecordDao {
	
	UserControllers selectContrller();
	
	Integer selectCountByUserId(String string);
	
	PercentList selectPercentList(String type);
	
	Integer counUserIdPay(String userId);

}
