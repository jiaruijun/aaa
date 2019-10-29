package com.overcloud.pay.common.response;

public enum ResCode {

	
    AJAX_SUCCESS(0, "操作成功"),

    Pro_NULL(1001, "productId不可为空"),

    AJAX_EXCEPTION(1002, "订购失败"),

    AJAX_NOTFOUND(0, "已订购此产品"),

    AJAX_PARAM_ERR(1003, "尚未订购此产品"),

    AJAX_VERTIFY_FAIL(1004, "userId不能为空"),

    AJAX_NEEDLOGIN(1005, "pfId不能为空"),

    AJAX_UNAME_ERR(1006, "userId不存在"),

    AJAX_PASS_ERR(1007, "操作标识符Action不能为空"),

    AJAX_DUPLICATE_ERR(1008, "productId不合法"),

    AJAX_REDIRECT(1009, "请传正确的Action"),
    
    PASSWD_ERROR(0, "订购成功"),
    
    QUIT_PAYS(0,"退订成功"),
    QUIT_PAYD(1010,"退订失败"),
    
    PAY_SUCCESS(0, "充值成功"), 
    
    PAY_ERROR(1011, "充值失败"),
	PWD_NEED(1012,"请输入童锁密码"),
	PWD_ERROR(1013,"童锁密码不正确"),
	PAY_BY(1014,"超过充值月额度"),
	MONEY_SHORT(1015,"游戏币不足"),
	BILL_SUCCESS(0,"消费成功"),
	BILL_FAIL(1019,"消费失败"),
	SP_NOTNULL(1016,"SPID不能为空"),
	URL_NOTNULL(1017,"ReturnURL不能为空"),
	SSO_SUCCESS(0,"SSO认证成功"),
	SS0_FAIL(1018,"SSO请求失败"),
	USERTOKRN_NULL(1019,"userToken不能为空"),
	PFID_NULL(1020,"pfId不能为空"),
	SPID_NULL(1021,"spId不能为空"),
	PFID_ERROR(1022,"pfid不合法"),
	SPID_ERROR(1023,"spid不合法"),
	BILL_ERROR(1024,"此订单不合法"),
	USER_BLACK(1025,"黑名单用户"),
	HTTP_FAIL(1026,"HTTP请求boss失败"),
	UNKNOW_FAIL(1027,"未知错误"),
	LIMIT_ERROR(1028,"限额参数不合法");
    private int id;

    private String msg;

    private ResCode(int id, String msg) {
        this.msg = msg;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
