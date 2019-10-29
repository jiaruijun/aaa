package com.overcloud.pay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.overcloud.pay.entity.MonthBill;
import com.overcloud.pay.vo.AuthMonthlyUserVO;
import com.overcloud.pay.vo.BillMonthLogVO;
import com.overcloud.pay.vo.GsydbyOrderInfoVO;
import com.overcloud.pay.vo.MonthlyGameCategoryVo;
import com.overcloud.pay.vo.MonthlyParInfoVO;

@Repository
public interface OrderInfoDao {
	
	//查询用户是否包月
	AuthMonthlyUserVO getUserAuthInfo(Map<String, String> params);
	
	//获取包月包信息
    MonthlyGameCategoryVo getMonthlyGameCategoryInfo();
    
  //插入订单Log表中
  	void saveMonthlyLogInfo(Map<String, String> params);
  	
  //修改包月支付订单log 表中的 记录
  	void updataBillMopnthLogOrder(BillMonthLogVO hnyxBillMonthLogVO);
  	
  //根据订单编号 获取湖南有线包月支付log 表中的记录
  	BillMonthLogVO getBillMonthLogInfo(String orderNo);
  	
  	
  //从包月支付记录表中获取的 数据 插入到 包月成功记录表中
  	void insertBillMonthInfo(BillMonthLogVO hnyxBillMonthLogVO);
  	
  	//通过map 插入到支付成功记录表中
  	void insertBillMonthInfos(Map<String, String> params);
  	
  	
  //前端根据OrderNo查询后台支付结果
  	BillMonthLogVO getMonthOrderInfoCode(Map<String, String> params);
  	
  	
  	//进入大厅插入用户信息
  	void saveUserInfo(Map<String, String> params);
  	
  	//进入大厅插入用户访问数
  	void saveVisitInfo(Map<String, String> params);
  	
  	
  	//获取该条订单是否插入
  	Integer getCountOrderNo(BillMonthLogVO hnyxBillMonthLogVO);
  	
  	
  //前端根据用户信息查询支付记录
  	List<GsydbyOrderInfoVO> getGsydbyBillInfo(GsydbyOrderInfoVO cqgdbyOrderInfoVO);
  	
    //包月参数管理表格
  	MonthlyParInfoVO getMonthlyPar();
  	List<MonthlyParInfoVO> list();
  	
  	//查询价格
    String getPrice();

    //查询该deviceId是否存在在数据库中
    Integer counDeviceId(String userId);
    
    
  //给insFilter表中添加字段
  	void insFilter(String userId);
  	
  	
  	/**
  	 * 获取产品列表
  	 * @return
  	 */
  	List<MonthlyParInfoVO> getProductlist();
  	
	//退订插入退订成功表中
  	void saveUnsubscribePayInfo(Map<String, String> params);
  	
  	
  	 /**
     * 查询上个月订购的数据
     * @return
     */
    List<MonthBill> getMonthInfos();
    
    /**
     * 查询该手机号本月是否订购过
     * @param phone
     * @return
     */
    Integer getCountBYPPone(String phone);
    
    
    /**
     * 查询该用户是不是黑名单用户
     */
    Integer getBlackList(Map<String, String> params);
    
	//二维码支付通过实体对象 插入到支付成功记录表中
  	void insertBillMonthQRCodeInfos(BillMonthLogVO billMonthLogVO);
  	
  	
  //前端根据OrderNo查询二维码支付结果
  	BillMonthLogVO getQRReturnUrlPayStatus(Map<String, String> params);


}
