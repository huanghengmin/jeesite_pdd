package com.thinkgem.jeesite.modules.quartz.util.sms;

import org.junit.Test;

/**
 * Created by huanghengmin on 2017/8/26.
 */
public class RandNumUtils {
    public static String getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return String.valueOf(randNum);
    }
    @Test
    public void rand(){
        System.out.println("随机数为" + String.valueOf(getRandNum(1,999999)));
    }
}
