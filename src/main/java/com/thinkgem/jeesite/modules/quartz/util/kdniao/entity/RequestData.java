package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

import java.util.List;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class RequestData {
    private String CallBack;
    private String MemberID;
    private String CustomerName;
    private String CustomerPwd;
    private String SendSite;
    private String ShipperCode;
    private String LogisticCode;
    private String OrderCode;
    private String MonthCode;
    private String PayType;
    private String ExpType;
    private String Cost;
    private String OtherCost;
    private String StartDate;
    private String EndDate;
    private Double Weight;
    private Integer Quantity;
    private Double Volume;
    private String Remark;
    private Integer IsNotice;
    private Receiver Receiver;
    private Sender Sender;
    private List<AddService> AddService;
    private List<Commodity> Commodity;





    /*CallBack	String	用户自定义回调信息	O
    MemberID	String	会员标识(备用字段)	O
    CustomerName	String	电子面单客户账号(与快递网点申请)	O
    CustomerPwd	String	电子面单密码	O
    SendSite	String	收件网点标识	O
    ShipperCode	String	快递公司编码	R
    LogisticCode	String	快递单号	R
    OrderCode	String	订单编号	O
    MonthCode	String	月结编码	O
    PayType	Int	邮费支付方式:1-现付，2-到付，3-月结，4-第三方支付	O
    ExpType	String	快递类型：1-标准快件	O
    Cost	Double	寄件费（运费）	O
    OtherCost	Double	其他费用	O*/



    /*StartDate	String	上门取货时间段:"yyyy-MM-dd HH:mm:ss"格式化，本文中所有时间格式相同	O
    EndDate	String	O
    Weight	Double	物品总重量kg	O
    Quantity	Int	件数/包裹数	O
    Volume	Double	物品总体积m3	O
    Remark	String	备注	O
    IsNotice	Int	是否分发到快递公司：1-不分发；0-分发.默认为0	O*/

    public String getCallBack() {
        return CallBack;
    }

    public void setCallBack(String callBack) {
        CallBack = callBack;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerPwd() {
        return CustomerPwd;
    }

    public void setCustomerPwd(String customerPwd) {
        CustomerPwd = customerPwd;
    }

    public String getSendSite() {
        return SendSite;
    }

    public void setSendSite(String sendSite) {
        SendSite = sendSite;
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

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getMonthCode() {
        return MonthCode;
    }

    public void setMonthCode(String monthCode) {
        MonthCode = monthCode;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getExpType() {
        return ExpType;
    }

    public void setExpType(String expType) {
        ExpType = expType;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getOtherCost() {
        return OtherCost;
    }

    public void setOtherCost(String otherCost) {
        OtherCost = otherCost;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public Double getWeight() {
        return Weight;
    }

    public void setWeight(Double weight) {
        Weight = weight;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public Double getVolume() {
        return Volume;
    }

    public void setVolume(Double volume) {
        Volume = volume;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Integer getIsNotice() {
        return IsNotice;
    }

    public void setIsNotice(Integer isNotice) {
        IsNotice = isNotice;
    }

    public com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Receiver getReceiver() {
        return Receiver;
    }

    public void setReceiver(com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Receiver receiver) {
        Receiver = receiver;
    }

    public com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Sender getSender() {
        return Sender;
    }

    public void setSender(com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Sender sender) {
        Sender = sender;
    }

    public List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.AddService> getAddService() {
        return AddService;
    }

    public void setAddService(List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.AddService> addService) {
        AddService = addService;
    }

    public List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Commodity> getCommodity() {
        return Commodity;
    }

    public void setCommodity(List<com.thinkgem.jeesite.modules.quartz.util.kdniao.entity.Commodity> commodity) {
        Commodity = commodity;
    }
}
