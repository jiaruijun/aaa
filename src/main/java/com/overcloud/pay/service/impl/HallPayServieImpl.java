package com.overcloud.pay.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.overcloud.pay.dao.HallDao;
import com.overcloud.pay.entity.SpProduct;
import com.overcloud.pay.service.HallPayService;

@Service
public class HallPayServieImpl  implements HallPayService{

	@Resource
	HallDao hallDao;
	
	@Override
	public List<SpProduct> selectlist() {
		// 查询大厅的充值选项
		
		List<SpProduct> list=	hallDao.selectList();
		
		return list;
	}

}
