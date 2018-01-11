package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.ExpressDetail;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpressDetailActivity extends BaseActivity implements HolderBuilder {

    @Bind(R.id.list)
    ListView mList;
    private String result;
    private ArrayList<ExpressDetail> items;
    private HolderListAdapter<ExpressDetail> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_detail);
        ButterKnife.bind(this);
        result = getIntent().getStringExtra("detail");
        initViews();
        Log.d("reg", "result:" + result);

    }

    private void initViews() {
        if (result != null) {
            try {
                items = new ArrayList<ExpressDetail>();
                JSONArray jsonArray = new JSONArray(result);
                if (jsonArray.length() <= 0) {
                    Log.d("reg", "000000");
                    App.me().toast("暂无数据");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
                    Log.d("reg", "o:" + o);
                    ExpressDetail detail = JSON.parseObject(o, ExpressDetail.class);
                    if (i == jsonArray.length() - 1) {
                        detail.setIsLasted(-1);
                    }
                    items.add(detail);
                }
                mList.setAdapter(adapter = new HolderListAdapter<ExpressDetail>(this, this));
                adapter.setNotifyOnChange(false);
                adapter.clear();
                Collections.reverse(items);
                adapter.addAll(items);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            App.me().toast("此单无物流信息");
        }

    }

    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_express_detail_item, null);
    }

    @Override
    public Holder createHolder(View view, int position) {

        return new ExpressDetailActivity.Details(view);
    }

    public class Details extends Holder<ExpressDetail> {

        @Bind(R.id.progress)
        TextView progress;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.status)
        ImageView status;

        ExpressDetail model;

        public Details(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(ExpressDetail item) {
            model = item;
            if (item.getIsLasted() == -1) {
                status.setBackgroundResource(R.mipmap.ic_circle_point);
                progress.setTextColor(getResources().getColor(R.color.windowBackground));
                time.setTextColor(getResources().getColor(R.color.windowBackground));
            } else {
                status.setBackgroundResource(R.mipmap.ic_gray_circle_point);
                progress.setTextColor(getResources().getColor(R.color.color9));
                time.setTextColor(getResources().getColor(R.color.color9));
            }
            progress.setText(item.getRight().replaceAll("\"", ""));
            time.setText(item.getLeft());
        }
    }

    @OnClick(R.id.back)
    void onback() {
        onBackPressed();
    }
}
