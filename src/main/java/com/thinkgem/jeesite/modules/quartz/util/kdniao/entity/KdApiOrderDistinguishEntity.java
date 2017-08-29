package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

import java.util.List;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class KdApiOrderDistinguishEntity {
    private String EBusinessID;//电商用户ID
    private boolean  Success;//成功与否
    private String LogisticCode;//物流单号
    private int Code;//失败原因
    private List<Shippers> Shippers;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Shippers> getShippers() {
        return Shippers;
    }

    public void setShippers(List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Shippers> shippers) {
        Shippers = shippers;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }
}
