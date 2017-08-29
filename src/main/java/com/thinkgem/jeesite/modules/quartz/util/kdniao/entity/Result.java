package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class Result {
    private String EBusinessID;
    private String UpdateTime;
    private boolean Success;
    private String Reason;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
