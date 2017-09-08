package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

import java.util.List;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class Data {
    private String EBusinessID;
    private String OrderCode;
    private String ShipperCode;
    private String LogisticCode;
    private boolean Success;
    private String Reason;
    private int  State;
    private String CallBack;
    private List<Traces> Traces;
    private String EstimatedDeliveryTime;
    private PickerInfo PickerInfo;
    private SenderInfo SenderInfo;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
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

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public String getCallBack() {
        return CallBack;
    }

    public void setCallBack(String callBack) {
        CallBack = callBack;
    }

    public List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces> getTraces() {
        return Traces;
    }

    public void setTraces(List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Traces> traces) {
        Traces = traces;
    }

    public String getEstimatedDeliveryTime() {
        return EstimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        EstimatedDeliveryTime = estimatedDeliveryTime;
    }

    public com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.PickerInfo getPickerInfo() {
        return PickerInfo;
    }

    public void setPickerInfo(com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.PickerInfo pickerInfo) {
        PickerInfo = pickerInfo;
    }

    public com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.SenderInfo getSenderInfo() {
        return SenderInfo;
    }

    public void setSenderInfo(com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.SenderInfo senderInfo) {
        SenderInfo = senderInfo;
    }
    /*EBusinessID	String	商户ID	O
    OrderCode	String	订单编号	O
    ShipperCode	String	快递公司编码	R
    LogisticCode	String	快递单号	R
    Success	Bool	成功与否：true,false	R
    Reason	String	失败原因	O
    State	String	物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件	R
    CallBack	String	订阅接口的Bk值	O
    Traces
        Trace	AcceptTime	String	时间	R
        AcceptStation	String	描述	R
        Remark	String	备注	O
    EstimatedDeliveryTime	String	预计到达时间yyyy-mm-dd	O
    PickerInfo	PersonName	String	快递员姓名	O
        PersonTel	String	快递员电话	O
        PersonCode	String	快递员工号	O
        StationName	String	网点名称	O
        StationAddress	String	网点地址	O
        StationTel	String	网点电话	O
    SenderInfo	PersonName	String	派件员姓名	O
        PersonTel	String	派件员电话	O
        PersonCode	String	派件员工号	O
        StationName	String	派件网点名称	O
        StationAddress	String	派件网点地址	O
        StationTel	String	派件网点电话	O*/

    @Override
    public String toString() {
        return "Data{" +
                "EBusinessID='" + EBusinessID + '\'' +
                ", OrderCode='" + OrderCode + '\'' +
                ", ShipperCode='" + ShipperCode + '\'' +
                ", LogisticCode='" + LogisticCode + '\'' +
                ", Success=" + Success +
                ", Reason='" + Reason + '\'' +
                ", State=" + State +
                ", CallBack='" + CallBack + '\'' +
                ", Traces=" + Traces +
                ", EstimatedDeliveryTime='" + EstimatedDeliveryTime + '\'' +
                ", PickerInfo=" + PickerInfo +
                ", SenderInfo=" + SenderInfo +
                '}';
    }
}
