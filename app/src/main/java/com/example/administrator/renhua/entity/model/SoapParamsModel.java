/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.entity.model;

import java.util.Arrays;

public class SoapParamsModel extends BaseModel {

    public String beanName;
    public String methodName;
    public String[] paramType;
    public Object[] paramValue;

    public SoapParamsModel beanName(String beanName) {
        this.beanName = beanName;
        return this;
    }

    public SoapParamsModel methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public SoapParamsModel params(Object... params) {
        Arrays.fill(paramType = new String[(paramValue = params).length], "String");
        for (int i = 0; i < paramType.length; i++) {
            if (params[i] == null) {
                params[i] = "";
            } else if (!(params[i] instanceof String)) {
                params[i] = String.valueOf(params[i]);
            }
        }
        return this;
    }

}
