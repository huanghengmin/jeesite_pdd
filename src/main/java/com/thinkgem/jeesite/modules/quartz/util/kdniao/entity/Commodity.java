package com.thinkgem.jeesite.modules.quartz.util.kdniao.entity;

/**
 * Created by huanghengmin on 2017/8/27.
 */
public class Commodity {
    private String GoodsName;
    private String GoodsCode;
    private int Goodsquantity;
    private double GoodsPrice;
    private double GoodsWeight;
    private String GoodsDesc;
    private double GoodsVol;


   /* GoodsName	String	商品名称	O
    GoodsCode	String	商品编码	O
    Goodsquantity	Int	件数	O
    GoodsPrice	Double	商品价格	O
    GoodsWeight	Double	商品重量kg	O
    GoodsDesc	String	商品描述	O
    GoodsVol	Double	商品体积m3	O*/

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getGoodsCode() {
        return GoodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        GoodsCode = goodsCode;
    }

    public int getGoodsquantity() {
        return Goodsquantity;
    }

    public void setGoodsquantity(int goodsquantity) {
        Goodsquantity = goodsquantity;
    }

    public double getGoodsPrice() {
        return GoodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        GoodsPrice = goodsPrice;
    }

    public double getGoodsWeight() {
        return GoodsWeight;
    }

    public void setGoodsWeight(double goodsWeight) {
        GoodsWeight = goodsWeight;
    }

    public String getGoodsDesc() {
        return GoodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        GoodsDesc = goodsDesc;
    }

    public double getGoodsVol() {
        return GoodsVol;
    }

    public void setGoodsVol(double goodsVol) {
        GoodsVol = goodsVol;
    }
}
