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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.GovernmentPersonalTitles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 列表适配器
 */
public class ServiceAdapter extends BaseAdapter {

    private final Context context;
    private List<GovernmentPersonalTitles> list;
    private List<GovernmentPersonalTitles> olist;

    public ServiceAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<GovernmentPersonalTitles>();
        this.olist = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GovernmentPersonalTitles getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class holder{
        TextView mTitle;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.layout_government_third_list_item, null);
            holder=new holder();

            holder.mTitle = (TextView) view.findViewById(R.id.title);
            view.setTag(holder);
        }else {
            holder=(holder)view.getTag();
        }

        GovernmentPersonalTitles item = list.get(position);
        holder.mTitle.setText(item.getName());
        return view;
}



    public void clear() {
        list.clear();
    }

    public boolean addAll(Collection<? extends GovernmentPersonalTitles> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(GovernmentPersonalTitles object) {
        return list.add(object);
    }

}
