/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.entity;

/**
 * 村镇
 */
public class Town {

    public String z_id;
    public String c_id;
    public String z_name;

    public Town(String z_id, String c_id, String z_name) {
        this.z_id = z_id;
        this.c_id = c_id;
        this.z_name = z_name;
    }

    public String getZ_id() {
        return z_id;
    }

    public void setZ_id(String z_id) {
        this.z_id = z_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getZ_name() {
        return z_name;
    }

    public void setZ_name(String z_name) {
        this.z_name = z_name;
    }
}
