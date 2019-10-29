package com.overcloud.pay.common.constst;

/**
 * 定义支付常量
 * 
 * @since 2015-12-9 下午2:25:36
 * @version 1.0
 * @author JPF
 */
public class ReturnMsgCode {

    // 请求北京联通开始
    public static final String REQUEST_SUCCESS = "10000";
    public static final String REQUEST_FAIL = "10001";

    // 下订单处理
    public static final String PLACE_ORDER_SUCCESS = "20000";
    public static final String PLACE_ORDER_FAIL = "20001";
    public static final String PLACE_ORDER_TIMEOUT = "20002";

    // 订单查询处理
    public static final String QUERY_ORDER_SUCCESS = "30000";
    public static final String QUERY_ORDER_FAIL = "30001";
    
    // 处理参数
    public static final String PARAMS_ERROR = "60000";
    public static final String PARAMS_APPID_ERROR = "60001";
    public static final String PARAMS_SESSION_ERROR = "60002";
    public static final String PARAMS_UID_ERROR = "60003";
    public static final String PARAMS_SIGN_ERROR = "60004";
    public static final String PARAMS_APPKEY_ERROR = "60005";

    // 消息管理
    public static final String REQUEST_SUCCESS_MSG = "请求成功";
    public static final String REQUEST_FAIL_MSG = "请求失败";

    public static final String PLACE_ORDER_SUCCESS_MSG = "预下订单成功";
    public static final String PLACE_ORDER_FAIL_MSG = "预下订单失败";
    public static final String PLACE_ORDER_TIMEOUT_MSG = "预下订单超时";

    public static final String QUERY_ORDER_SUCCESS_MSG = "订单查询成功";
    public static final String QUERY_ORDER_FAIL_MSG = "订单查询失败";
    
 // 参数信息处理
    public static final String PARAMS_ERROR_MSG = "参数信息错误";
    public static final String PARAMS_APPID_ERROR_MSG = "APPID参数信息错误";
    public static final String PARAMS_SESSION_ERROR_MSG = "TOKEN或SESSION参数信息错误";
    public static final String PARAMS_UID_ERROR_MSG = "UID参数信息错误";
    public static final String PARAMS_SIGN_ERROR_MSG = "SIGN参数信息错误";
    public static final String PARAMS_APPKEY_ERROR_MSG = "游戏识别码参数信息错误";
    
    
    /**
     * 返回结果对应返回代码 和相关的返回信息
     */
    // 反回失败
    public static final String FAIL_RESULT = "fail";
    public static final String FAIL_CODE = "001";
    public static final String FAIL_PARAMS_ERRORMSG = "参数信息错误";
    public static final String FAIL_PAY_CODE = "002";
    public static final String FAIL_PAY_ERRORMSG = "请求支付二维码错误码";
    // 返回成功
    public static final String SUCCESS = "success";
    public static final String SUCCESS_CODE = "000";
    // ************************支付状态***************
    public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
    public static final String TRADE_CLOSED = "TRADE_CLOSED";
    public static final String WAIT_BUYER_MONTH_PAY = "05";
    public static final String TRADE_MONTH_SUCCESS = "00";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String TRADE_PENDING = "TRADE_PENDING";
    public static final String TRADE_FINISHED = "TRADE_FINISHED";
    public static final String TRADE_WAIT = "TRADE_WAIT";

}
