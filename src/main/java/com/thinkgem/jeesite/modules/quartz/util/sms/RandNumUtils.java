package com.thinkgem.jeesite.modules.quartz.util.sms;

import org.junit.Test;

/**
 * Created by huanghengmin on 2017/8/26.
 */
public class RandNumUtils {
    public static int getRandNum(int min, int max) {
        int randNum = min + (int)(Math.random() * ((max - min) + 1));
        return randNum;
    }
    @Test
    public void rand(){
        System.out.println("随机数为" + getRandNum(1,999999));
    }
}
