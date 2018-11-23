package com.example.administrator.renhua.entity;

import java.io.Serializable;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/8/17
 * @DESCRIPTION:
 */
public class FromInfo implements Serializable{
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    String itemCode="";
    public String getShiJianBianMa() {
        return ShiJianBianMa;
    }

    public void setShiJianBianMa(String shiJianBianMa) {
        ShiJianBianMa = shiJianBianMa;
    }

    String ShiJianBianMa;

    public String getXiangMuMingCheng() {
        return XiangMuMingCheng;
    }

    public void setXiangMuMingCheng(String xiangMuMingCheng) {
        XiangMuMingCheng = xiangMuMingCheng;
    }

    public String getShenQingRen() {
        return ShenQingRen;
    }

    public void setShenQingRen(String shenQingRen) {
        ShenQingRen = shenQingRen;
    }

    public String getLianXiRen() {
        return LianXiRen;
    }

    public void setLianXiRen(String lianXiRen) {
        LianXiRen = lianXiRen;
    }

    public String getShouJi() {
        return ShouJi;
    }

    public void setShouJi(String shouJi) {
        ShouJi = shouJi;
    }

    public String getZhengJianLeiXing() {
        return ZhengJianLeiXing;
    }

    public void setZhengJianLeiXing(String zhengJianLeiXing) {
        ZhengJianLeiXing = zhengJianLeiXing;
    }

    public String getZhengJianHaoMa() {
        return ZhengJianHaoMa;
    }

    public void setZhengJianHaoMa(String zhengJianHaoMa) {
        ZhengJianHaoMa = zhengJianHaoMa;
    }

    public String getDiZhi() {
        return DiZhi;
    }

    public void setDiZhi(String diZhi) {
        DiZhi = diZhi;
    }

    String XiangMuMingCheng;
    String ShenQingRen;
    String LianXiRen;
    String ShouJi;
    String ZhengJianLeiXing;
    String ZhengJianHaoMa;
    String DiZhi;
}
