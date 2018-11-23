package com.example.administrator.renhua.entity;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class GovernmentForthTypes {

    String serviceCode;//事项编号
    String name;//事项名称
    String orgName;//事项相关部门
    String approveTime;//事项时间

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    String CODE;

    public String getITEM_ID() {
        return ITEM_ID;
    }

    public void setITEM_ID(String ITEM_ID) {
        this.ITEM_ID = ITEM_ID;
    }

    String ITEM_ID;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(String approveTime) {
        this.approveTime = approveTime;
    }
}
