/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.pgyersdk.crash.PgyCrashManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 */
public class HolderListAdapter<T> extends ArrayAdapter<T> {

    final HolderBuilder builder;

    public HolderListAdapter(@NonNull Context context, @NonNull HolderBuilder builder) {
        this(context, builder, new ArrayList<T>());
    }

    public HolderListAdapter(@NonNull Context context, @NonNull HolderBuilder builder, @NonNull List<T> list) {
        super(context, 0, list);
        this.builder = builder;
        try {
            Field field = ArrayAdapter.class.getDeclaredField("mInflater");
            field.setAccessible(true);
            field.set(this, null);
        } catch (Exception e) {
            Log.e("HolderListAdapter", e.getMessage(), e);
            PgyCrashManager.reportCaughtException(context, e);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        try {
            if (view == null) {
                view = builder.inflateView(getContext(), position);
            }
            Holder holder = null;
            Object tag = view.getTag();
            if (tag != null) {
                try {
                    holder = (Holder) tag;
                } catch (ClassCastException e) {
                }
            }
            if (holder == null) {
                holder = builder.createHolder(view, position);
                view.setTag(holder);
            }
            if (holder != null) {
                holder.onBind(getItem(position));
            }
        } catch (Exception e) {
            Log.e("HolderListAdapter", e.getMessage(), e);
        } finally {
            return view;
        }
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        return getView(position, view, parent);
    }

}
