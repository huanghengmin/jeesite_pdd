package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class PickerInfo {
    private String PersonName;
    private String PersonTel;
    private String PersonCode;
    private String StationName;
    private String StationAddress;
    private String StationTel;



   /* PersonName	String	快递员姓名	O
    PersonTel	String	快递员电话	O
    PersonCode	String	快递员工号	O
    StationName	String	网点名称	O
    StationAddress	String	网点地址	O
    StationTel	String	网点电话	O*/

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getPersonTel() {
        return PersonTel;
    }

    public void setPersonTel(String personTel) {
        PersonTel = personTel;
    }

    public String getPersonCode() {
        return PersonCode;
    }

    public void setPersonCode(String personCode) {
        PersonCode = personCode;
    }

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public String getStationAddress() {
        return StationAddress;
    }

    public void setStationAddress(String stationAddress) {
        StationAddress = stationAddress;
    }

    public String getStationTel() {
        return StationTel;
    }

    public void setStationTel(String stationTel) {
        StationTel = stationTel;
    }
}
