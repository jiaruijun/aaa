package com.overcloud.pay.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.overcloud.pay.entity.SpProduct;

@Repository
public interface HallDao {

	List<SpProduct> selectList();

}
