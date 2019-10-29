package com.overcloud.pay.service;

import java.util.Map;

import com.overcloud.pay.vo.UserActionVo;

public interface UserRecordService {
	
	UserActionVo getUserAction(Map<String, String> paramMap);

}
