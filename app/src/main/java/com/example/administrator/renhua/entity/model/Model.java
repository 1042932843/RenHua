/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.entity.model;

import android.support.annotation.NonNull;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Model extends BaseModel implements Map<String, Object> {

    String string;
    Map<String, Object> attrs;

    public Model() {
        attrs = new LinkedTreeMap<>();
    }

    public Model(String string) {
        this();
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        try {
            Object o = attrs.get(name);
            if (o != null) {
                return (T) o;
            }
        } catch (ClassCastException ignored) {
        }
        return null;
    }

    public String getString(String name) {
        return getString(name, null);
    }

    public Boolean getBoolean(String name) {
        return getBoolean(name, null);
    }

    public Number getNumber(String name) {
        return getNumber(name, null);
    }

    public String getString(String name, String defVal) {
        Object o = get(name);
        if (o instanceof String) {
            return (String) o;
        } else if (o != null) {
            return o.toString();
        }
        return defVal;
    }

    public Boolean getBoolean(String name, Boolean defVal) {
        Object o = get(name);
        if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            return Boolean.valueOf(o.toString());
        }
        return defVal;
    }

    public Number getNumber(String name, Number defVal) {
        Object o = get(name);
        if (o instanceof Number) {
            return (Number) o;
        } else if (o instanceof String) {
            return new LazilyParsedNumber(o.toString());
        }
        return defVal;
    }

    public Model set(String name, Object value) {
        attrs.put(name, value);
        return this;
    }

    public Model remove(String name) {
        attrs.remove(name);
        return this;
    }

    @Override
    public void clear() {
        attrs.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return attrs.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return attrs.containsValue(value);
    }

    @NonNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return attrs.entrySet();
    }

    @Override
    @Deprecated
    public Object get(Object key) {
        return get(String.valueOf(key));
    }

    @Override
    public boolean isEmpty() {
        return attrs.isEmpty();
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return attrs.keySet();
    }

    @Override
    @Deprecated
    public Object put(String key, Object value) {
        return attrs.put(key, value);
    }

    @Override
    public void putAll(@NonNull Map<? extends String, ?> map) {
        attrs.putAll(map);
    }

    @Override
    public Object remove(Object key) {
        return attrs.remove(key);
    }

    @Override
    public int size() {
        return attrs.size();
    }

    @NonNull
    @Override
    public Collection<Object> values() {
        return attrs.values();
    }

    @Override
    public String toString() {
        if (string != null) {
            return string;
        }
        return super.toString();
    }

}
