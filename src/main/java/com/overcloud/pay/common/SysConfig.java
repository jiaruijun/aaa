package com.overcloud.pay.common;

import java.text.MessageFormat;
import java.util.Properties;

/**
 * 整个应用通用的常量, 用于获取config.properties中的值
 */
public class SysConfig {

    public static Properties pro = null;

    static {
        try {
            Properties pro = new Properties();
            pro.load(SysConfig.class.getClassLoader().getResourceAsStream("userdefined.properties"));
            SysConfig.pro = pro;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getInt(String key, int defaultValue) {
        int result = 0;
        try {
            result = Integer.parseInt(getVal(key));
            return result;
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getVal(String key) {
        return pro.getProperty(key);
    }

    public static String getVal(String key, Object... args) {
        String val = pro.getProperty(key);
        return MessageFormat.format(val, args);
    }
    
    
}
