package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

import java.util.List;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class RequestResult {
    private String EBusinessID;
    private String UpdateTime;
    private Boolean Success;
    private String Reason;
    private String EstimatedDeliveryTime;

   /* EBusinessID	String	用户ID	R
    UpdateTime	String	时间	R
    Success	Bool	成功与否：true，false	R
    Reason	String	失败原因	O
    EstimatedDeliveryTime	String	订单预计到货时间yyyy-mm-dd（即将上线）	O*/

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

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getEstimatedDeliveryTime() {
        return EstimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        EstimatedDeliveryTime = estimatedDeliveryTime;
    }
}
