/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.kit;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsonKit implements ConfigKit {

    static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            synchronized (JsonKit.class) {
                if (gson == null) {
                    GsonBuilder builder = new GsonBuilder();
                    // 注册序列化过滤器 过滤已_下划线开头的字段
                    builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getName().startsWith("_");
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    });
                    // 注册反序列化适配器 日期或日期时间解析
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        @Override
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            if (json.isJsonPrimitive()) {
                                String date = json.getAsString();
                                String pattern = null;
                                if (StringKit.matchesDate(date)) {
                                    pattern = DATE_FORMAT;
                                } else if (StringKit.matchesDateTime(date)) {
                                    pattern = DATE_TIME_FORMAT;
                                }
                                if (pattern != null) {
                                    try {
                                        return new SimpleDateFormat(pattern, Locale.getDefault()).parse(date);
                                    } catch (ParseException e) {
                                        LogKit.e("JsonKit.gson", e);
                                    }
                                }
                            }
                            return null;
                        }
                    });
                    // 注册序列化数值适配器 最多输出两位小数
                    builder.registerTypeHierarchyAdapter(Number.class, new JsonSerializer<Number>() {
                        @Override
                        public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
                            if (src instanceof Float) {
                                src = new BigDecimal(src.floatValue());
                            } else if (src instanceof Double) {
                                src = new BigDecimal(src.doubleValue());
                            }
                            if (src instanceof BigDecimal && ((BigDecimal) src).scale() > 2) {
                                src = ((BigDecimal) src).setScale(2, BigDecimal.ROUND_DOWN);
                            }
                            return new JsonPrimitive(src);
                        }
                    });
                    gson = builder.create();
                }
            }
        }
        return gson;
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return gson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            LogKit.e("JsonKit.fromJson", e);
        }
        return null;
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) {
        try {
            return gson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            LogKit.e("JsonKit.fromJson", e);
        }
        return null;
    }

    public static String toJson(Object src) {
        if (src instanceof JSONObject || src instanceof JSONArray || src instanceof JsonElement) {
            return src.toString();
        }
        return gson().toJson(src);
    }

}
