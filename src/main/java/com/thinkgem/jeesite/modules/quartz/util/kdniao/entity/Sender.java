package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class Sender {
    private String Company;
    private String Name;
    private String Tel;
    private String Mobile;
    private String PostCode;
    private String ProvinceName;
    private String CityName;
    private String ExpAreaName;
    private String Address;



 /*   Company	String	收件人公司	O
    Name	String	收件人	O
    Tel	String	电话	O
    Mobile	String	手机	O
    PostCode	String	收件人邮编	O
    ProvinceName	String	收件省（如广东省，不要缺少“省”）	O
    CityName	String	收件市（如深圳市，不要缺少“市”）	O
    ExpAreaName	String	收件区（如福田区，不要缺少“区”或“县”）	O
    Address	String	收件人详细地址	O*/

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String postCode) {
        PostCode = postCode;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getExpAreaName() {
        return ExpAreaName;
    }

    public void setExpAreaName(String expAreaName) {
        ExpAreaName = expAreaName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
