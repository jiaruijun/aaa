package com.overcloud.pay.common.http;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebParameter工具类
 * 
 * @since
 * @version 1.0
 * @author
 */
public class WebParameterUtils {

    private static Logger logger = LoggerFactory.getLogger(WebParameterUtils.class);

    public static String getHttpBodyString(HttpServletRequest request) {
        String inputLine;
        String str = "";
        try {
            BufferedReader br = request.getReader();
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        }
        catch (Exception e) {

        }
        return str;
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = null;
        String method = request.getMethod();
        if (method.equals("POST")) {
            map = getParameters(request);
            // 普通的表单提交获取参数
            if (map == null || map.isEmpty()) {
                map = postParamters(request);
            }
        }
        else {
            map = getParameters(request);
        }
        logger.info(">>请求参数：" + map);
        return map;
    }

    private static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> paramters = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = request.getParameterMap();
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object objvalue = entry.getValue();
            String value = "";
            if (objvalue instanceof String[]) {
                for (String tmp : (String[]) objvalue) {
                    value += tmp + ",";
                }
                value = value.substring(0, value.length() - 1);
            }
            else {
                value = objvalue.toString();
            }

            paramters.put(key, value);
        }
        return paramters;
    }

    private static Map<String, String> postParamters(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String post = request.getReader().readLine();

            if (StringUtils.isNotEmpty(post)) {
                JSONObject jsonObj = JSONObject.fromObject(post);
                if (jsonObj != null) {
                    Iterator<?> objkey = jsonObj.keys();
                    while (objkey.hasNext()) {
                        String key = objkey.next().toString();
                        String value = (String) jsonObj.get(key);
                        map.put(key, value);
                    }
                }
                return map;
            }
        }
        catch (Exception e) {
            // 不用处理异常
        }
        return null;
    }
}
