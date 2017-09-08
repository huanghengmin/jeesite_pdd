package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

import java.util.List;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class ResponseData {
    private String EBusinessID;
    private int Count;
    private String PushTime;
    private List<Data> Data;
    public static final String type_101 = "101";

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getPushTime() {
        return PushTime;
    }

    public void setPushTime(String pushTime) {
        PushTime = pushTime;
    }

    public List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Data> getData() {
        return Data;
    }

    public void setData(List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Data> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "EBusinessID='" + EBusinessID + '\'' +
                ", Count=" + Count +
                ", PushTime='" + PushTime + '\'' +
                ", Data=" + Data +
                '}';
    }
}
