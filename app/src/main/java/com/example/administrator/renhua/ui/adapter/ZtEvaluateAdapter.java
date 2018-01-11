package com.example.administrator.renhua.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.Evaluate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class ZtEvaluateAdapter extends BaseAdapter {

    private List<Evaluate> list;
    private Context context;
    public static HashMap<String, Integer> evaItemMap;
    private LayoutInflater layoutInflater;
    public List<String> tag = new ArrayList<String>();

    public ZtEvaluateAdapter(List<Evaluate> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.evaItemMap = new HashMap<String, Integer>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Evaluate item = list.get(position);
        if (convertView != null && convertView.getId() == R.id.list) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_evaluate_item, null);
            viewHolder.rg = (RadioGroup) convertView.findViewById(R.id.rg);
            viewHolder.radioButton = (RadioButton) convertView.findViewById(R.id.perfect);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.tv.setTag(item.getId());
            if (tag.size() < list.size()){
                tag.add((String) viewHolder.tv.getTag());
            }
            Log.d("reg", "tag:"+ tag);
        }
        viewHolder.tv.setText(item.getItemTitle());
        convertView.setTag(viewHolder);
        viewHolder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.perfect:
                        evaItemMap.put(tag.get(position), 1);
                        Log.d("reg", "evaItemMap:"+evaItemMap.get(tag.get(position)));
                        break;
                    case R.id.good:
                        evaItemMap.put(tag.get(position), 2);
                        Log.d("reg", "evaItemMap:"+evaItemMap.get(tag.get(position)));
                        break;
                    case R.id.bad:
                        evaItemMap.put(tag.get(position), 3);
                        Log.d("reg", "evaItemMap:"+evaItemMap.get(tag.get(position)));
                        break;

                }

            }
        });
        return convertView;
    }
    public static void cleanEvaItemMap() {
        evaItemMap.clear();
    }

    public HashMap<String, Integer> getEvaItemMap() {
        return this.evaItemMap;
    }

    public class ViewHolder {
        public RadioGroup rg;
        public RadioButton radioButton;
        public TextView tv;
    }
}
