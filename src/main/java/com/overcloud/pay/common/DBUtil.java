package com.overcloud.pay.common;

import java.util.Map;
import java.util.UUID;

import com.overcloud.pay.common.http.IPDataHandler;



public class DBUtil {

    public static String keyGenerator() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 设置IP,城市等信息
     * 
     * @param paramMap
     * @param ipAddr
     */
    public static void setIpParam(Map<String, String> paramMap, String ipAddr) {
        paramMap.put("IP", ipAddr);
        String area = IPDataHandler.findGeography(ipAddr);// 根据IP地址查找城市名称
        String[] areaArr = area.split("\\t");
        int size = areaArr.length;
        if (size >= 3) {
            paramMap.put("country", areaArr[0]);
            paramMap.put("province", areaArr[1]);
            paramMap.put("city", areaArr[2]);
        }
        else if (size == 2) {
            paramMap.put("country", areaArr[0]);
            paramMap.put("province", areaArr[1]);
        }
        else if (size == 1) {
            paramMap.put("country", areaArr[0]);
        }
        else {// 未解析出地域,设置未知
            paramMap.put("country", "未知");
        }
    }
}
