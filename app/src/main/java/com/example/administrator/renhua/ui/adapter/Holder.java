/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.adapter;

import android.content.Context;
import android.view.View;

import butterknife.ButterKnife;

public class Holder<T> {

    protected View view;

    protected T model;

    int position = -1;

    public Holder(Context context, int resource) {
        this(View.inflate(context, resource, null));
    }

    public Holder(View view) {
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public Holder onBind(T model, int position) {
        this.model = model;
        this.position = position;
        return this;
    }

    public View getView() {
        return view;
    }

    public T getModel() {
        return model;
    }

    public int getPosition() {
        return position;
    }

}
