package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class AddService {
    private String Name;
    private String Value;
    private String CustomerID;


   /* Name	String	增值服务名称	O
    Value	String	增值服务值	O
    CustomerID	String	客户标识(选填)	O*/

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }
}
