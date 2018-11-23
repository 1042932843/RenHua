/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.entity.model;

import com.google.gson.JsonElement;

public class SoapResponseModel {

    public Exception exception;
    public JsonElement result;
    public JsonElement message;
    public JsonElement stateCode;

    public boolean hasException() {
        return exception != null;
    }

    public boolean hasResult() {
        return result != null;
    }

    public boolean hasPrimitiveResult() {
        return hasResult() && result.isJsonPrimitive();
    }

    public boolean hasObjectResult() {
        return hasResult() && result.isJsonObject();
    }

    public boolean hasArrayResult() {
        return hasResult() && result.isJsonArray();
    }

    public boolean hasMessage() {
        return message != null;
    }

    public boolean hasPrimitiveMessage() {
        return hasMessage() && message.isJsonPrimitive();
    }

    public boolean hasObjectMessage() {
        return hasResult() && message.isJsonObject();
    }

    public boolean hasArrayMessage() {
        return hasResult() && message.isJsonArray();
    }

    public boolean hasStateCode() {
        return stateCode != null;
    }

    public boolean hasPrimitiveStateCode() {
        return hasStateCode() && stateCode.isJsonPrimitive();
    }

    public boolean hasObjectStateCode() {
        return hasResult() && stateCode.isJsonObject();
    }

    public boolean hasArrayStateCode() {
        return hasResult() && stateCode.isJsonArray();
    }

}
