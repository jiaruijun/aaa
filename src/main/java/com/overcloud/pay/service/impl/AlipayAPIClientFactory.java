package com.overcloud.pay.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.overcloud.pay.common.SysConfig;

public class AlipayAPIClientFactory {
    /** API调用客户端 */
    private static AlipayClient alipayClient;

    /**
     * 获得API调用客户端
     * 
     * @return
     */
    public static AlipayClient getAlipayClient() {

        if (null == alipayClient) {
            alipayClient = new DefaultAlipayClient(SysConfig.getVal("alipay_gateway"), SysConfig.getVal("appId"),
                    SysConfig.getVal("PrivateKey"), "json", SysConfig.getVal("charset"),
                    SysConfig.getVal("alipay_public_key"));
        }
        return alipayClient;
    }
}
