package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class Traces {
    private String AcceptTime;
    private String AcceptStation;
    private String Remark;

    public String getAcceptTime() {
        return AcceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        AcceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return AcceptStation;
    }

    public void setAcceptStation(String acceptStation) {
        AcceptStation = acceptStation;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    @Override
    public String toString() {
        return "Traces{" +
                "AcceptTime='" + AcceptTime + '\'' +
                ", AcceptStation='" + AcceptStation + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
