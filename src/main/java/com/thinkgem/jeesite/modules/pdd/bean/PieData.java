package com.thinkgem.jeesite.modules.pdd.bean;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class PieData {
    private String name;
    private long number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public PieData(String name, long number) {
        this.name = name;
        this.number = number;
    }
}
