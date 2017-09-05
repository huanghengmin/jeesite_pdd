package com.thinkgem.jeesite.modules.pdd.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Date;

/**
 * Created by huanghengmin on 2017/9/2.
 */
public class TimeTag extends SimpleTagSupport{
    private Long diff;
    private String type;
    private Date startDate;
    private Date endDate;

    public void setDiff(Long diff) {
        this.diff = diff;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String dateDiff(Date startTime, Date endTime) {
        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;
        try {
            //获得两个时间的毫秒时间差异
            diff = endTime.getTime() - startTime.getTime();
            long day = diff/nd;//计算差多少天
            long hour = diff%nd/nh;//计算差多少小时
            long min = diff%nd%nh/nm;//计算差多少分钟
            long sec = diff%nd%nh%nm/ns;//计算差多少秒
            //输出结果
            if(day>=0&&hour>=0&&min>=0&&sec>0) {
                return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
            }else {
                return "0天0小时0分钟0秒";
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public String dateDiff(Long diff) {
        long nd = 1000*24*60*60;//一天的毫秒数
        long nh = 1000*60*60;//一小时的毫秒数
        long nm = 1000*60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        try {
            //获得两个时间的毫秒时间差异
            long day = diff/nd;//计算差多少天
            long hour = diff%nd/nh;//计算差多少小时
            long min = diff%nd%nh/nm;//计算差多少分钟
            long sec = diff%nd%nh%nm/ns;//计算差多少秒
            //输出结果
            if(day>=0&&hour>=0&&min>=0&&sec>0) {
                return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
            }else {
                return "0天0小时0分钟0秒";
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (type != null) {
          /* 从属性中使用消息 */
            JspWriter out = getJspContext().getOut();
            if(type.equals("DATE")){
                out.println(dateDiff(startDate,endDate) );
            }else if(type.equals("SECOND")){
                out.println(dateDiff(diff));
            }

        }
    }
}
