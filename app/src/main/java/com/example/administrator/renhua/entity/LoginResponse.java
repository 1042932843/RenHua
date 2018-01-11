/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.entity;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 用户登录
 */
public class LoginResponse {

    @PrimaryKey(AssignType.BY_MYSELF)
    public String id;
    public String uuid;
    public String username;
    public String phone;
    public String idcard;
    public String zName;
    public String cName;

}
