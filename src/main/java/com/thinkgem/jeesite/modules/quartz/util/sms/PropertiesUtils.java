package com.thinkgem.jeesite.modules.quartz.util.sms;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * 用户数据库访问的类
 *
 * @version 1.0
 * @作者Administrator
 * @createTime 2011-12-5 上午11:55:18
 */
public class PropertiesUtils {

    public static Properties getProperties() {
        Properties properties = null;
        try {
            InputStream in = new BufferedInputStream(PropertiesUtils.class.getResourceAsStream("/sms.properties"));
            properties = new Properties();
            properties.load(new InputStreamReader(in,"UTF-8"));
        } catch (Exception ex) {
            return null;
        }
        return properties;
    }
}
