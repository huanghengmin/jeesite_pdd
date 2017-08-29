package com.thinkgem.jeesite.modules.pdd.bean;

import java.util.List;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class PieBean {
    private String title;

    private String divId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private List<PieData> data;

    public PieBean(String title, String divId, List<PieData> data) {
        this.title = title;
        this.divId = divId;
        this.data = data;
    }

    public List<PieData> getData() {
        return data;
    }

    public void setData(List<PieData> data) {
        this.data = data;
    }

    public String getDivId() {
        return divId;
    }

    public void setDivId(String divId) {
        this.divId = divId;
    }
}
