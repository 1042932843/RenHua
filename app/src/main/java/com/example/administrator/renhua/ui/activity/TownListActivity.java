/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.AsyncHttpDialog;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.Town;
import com.example.administrator.renhua.entity.TownListResponse;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 村镇列表
 * <p/>
 * 事件分派
 * <br>
 * NumberListActivity.onPageListSuccess {@link #onPageListSuccess}
 */
public class TownListActivity extends BaseActivity implements HolderBuilder {

    private String mytown;
    private String zid;
    private String cid;

    Handler handler = new Handler();

    @Bind(R.id.town_head)
    TextView town_head;

    @Bind(R.id.list)
    ListView mList;

    HolderListAdapter<TownListResponse> adapter;

    int action; // 1:查询 2:删除

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_list);
        ButterKnife.bind(this);
        mList.addHeaderView(new Space(this));
        mList.addFooterView(new Space(this));
        mList.setAdapter(adapter = new HolderListAdapter<>(this, this));
        //声明
        town_head.setText("请选择镇");
        pageList(1, null);
        Log.d("reg","0000000000000");

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TownListResponse item = adapter.getItem(i - 1);

                if (action == 1) {
                    zid = item.z_id;
                    pageList(2, item.z_id);
//                    zid = item.z_id;
                    Log.d("reg", "zid"+zid);
                    mytown = item.z_name;
                } else {
                    //回调
                        cid = item.z_id;
                        onTownSuccess(new Town(zid, cid, mytown + item.z_name));
                }
            }
        });
    }

    void onTownSuccess(Town town) {
        EventBus.getDefault().post(town, "TownListActivity.onTownSuccess");
        Log.d("reg", "shabo"+town.getZ_name().toString());
        finish();
    }


    void pageList(int action, String id) {
        this.action = action;
//        SoapParams params;
//        if (action == 1) {
//            params = new SoapParams("getZhen");
//        } else {
//            params = new SoapParams("getCun");
//            params.add("z_id", id);
//        }
//        async.execute(params, callback);
        String url;
        if (action ==1){
            url = Constant.DOMAIN+"departController.do?getzhen";
            getZhen(url);
//            zhen.getZhen(url);
        }else {
            url = Constant.DOMAIN+"departController.do?getCunLevel";
            getCun(url, id);
        }

    }

    private void getCun(String url, final String zhen){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("zhen", zid);
        Log.d("reg", "zhen:"+zid);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<TownListResponse> towns = new ArrayList<TownListResponse>();
                            TownListResponse town;
                            JSONObject result = new JSONObject(responseString);
                            Log.d("reg", "responseString"+responseString);
                            JSONArray resArray = result.getJSONArray("obj");
                            Log.d("RegOne", "resArray:" + resArray);
                            if (resArray.length() == 0) {
                                town = new TownListResponse();
                                town.z_name = "";
                                town.z_id = "";
                                towns.add(town);
                                TownListResponse item = new TownListResponse();
                                cid = "";
                                onTownSuccess(new Town(zid, cid, mytown + ""));
                                finish();
                            } else {
                                for (int i = 0; i < resArray.length(); i++) {
                                    String o = resArray.getString(i);
                                    town = JSON.parseObject(o, TownListResponse.class);
                                    towns.add(town);
                                    Log.d("RegOne", "towns:" + towns);
                                    Log.d("RegOne", "o:" + town);
                                    Log.d("RegOne", "o:" + town.getZ_id());
                                    Log.d("RegOne", "o:" + town.getZ_name());
                                }
                                mList.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_out));
                                onPageListSuccess(towns);
                                town_head.setText("请选择村");
                                mList.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in));
                            }

                        } catch (Exception e) {
                            Log.e("reg", "err", e);
                        }
                    }
                }, 400);
            }
        });
    }

    private void getZhen(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                final ApiMsg apiMsg = JSON.parseObject(responseString, ApiMsg.class);
                App.me().toast(apiMsg.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                List<TownListResponse> towns = new ArrayList<TownListResponse>();
                Log.d("reg", "getzhen"+responseString);
                try {
                    JSONObject result = new JSONObject(responseString);
                    JSONArray resArray = result.getJSONArray("obj");
                    for (int i = 0; i < resArray.length(); i++) {
                        String o = resArray.getString(i);
                        TownListResponse town = JSON.parseObject(o, TownListResponse.class);
                        towns.add(town);
                        Log.d("RegOne", "o:" + town);
                        Log.d("RegOne", "o:" + town.getZ_id());
                        Log.d("RegOne", "o:" + town.getZ_name());
                    }
                    onPageListSuccess(towns);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    @OnClick(R.id.logo)
//    void onLogoClick() {
//        pageList(1, null);
//    }

    void onPageListSuccess(List<TownListResponse> numbers) {
        adapter.setNotifyOnChange(false);
        adapter.clear();
        for (TownListResponse number : numbers) {
            adapter.add(number);
        }
        adapter.notifyDataSetChanged();
//        mLogo.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
        EventBus.getDefault().post(numbers, "NumberListActivity.onPageListSuccess");
    }


    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_town_item, null);
    }

    @Override
    public Holder createHolder(View view, int position) {
        return new NumberHolder(view);
    }

    public class NumberHolder extends Holder<TownListResponse> {

        @Bind(R.id.town_item)
        TextView town_item;


        TownListResponse number;

        public NumberHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(TownListResponse item) {
            number = item;
            town_item.setText(item.z_name);
        }


    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }

}
