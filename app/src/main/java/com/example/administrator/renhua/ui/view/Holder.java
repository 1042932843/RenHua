/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.view;

import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;

public abstract class Holder<T> {

    protected final View view;

    public Holder(@NonNull View view) {
        this.view = view;
        ButterKnife.bind(this, view);
    }

    public abstract void onBind(T item);

}