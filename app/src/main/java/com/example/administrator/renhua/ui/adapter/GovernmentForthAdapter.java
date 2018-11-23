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
import com.example.administrator.renhua.entity.GovernmentForthTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 列表适配器
 */
public class GovernmentForthAdapter extends BaseAdapter {

    private final Context context;
    private List<GovernmentForthTypes> list;
    private List<GovernmentForthTypes> olist;

    public GovernmentForthAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<GovernmentForthTypes>();
        this.olist = list;
    }

    public void SearchCity(String city) {
        this.list = Search(city);
        notifyDataSetChanged();
    }
    private List<GovernmentForthTypes> Search(String city) {
        if (city != null && city.length() > 0) {
            ArrayList<GovernmentForthTypes> area = new ArrayList<GovernmentForthTypes>();
            for (GovernmentForthTypes a : this.olist) {
//                if (a.getItemsName().indexOf(city) != -1) {
//                    area.add(a);
//                }
            }
            return area;
        } else {
            return this.olist;
        }

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GovernmentForthTypes getItem(int position) {
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

        GovernmentForthTypes item = list.get(position);
        holder.mTitle.setText(item.getName());
        return view;
}



    public void clear() {
        list.clear();
    }

    public boolean addAll(Collection<? extends GovernmentForthTypes> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(GovernmentForthTypes object) {
        return list.add(object);
    }

}
