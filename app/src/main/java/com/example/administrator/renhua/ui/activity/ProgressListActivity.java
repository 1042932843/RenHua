package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.BusItem;
import com.example.administrator.renhua.entity.Enclosure;
import com.example.administrator.renhua.entity.GovernmentForthTypes;
import com.example.administrator.renhua.entity.business;
import com.example.administrator.renhua.ui.adapter.BusAdapter;
import com.example.administrator.renhua.ui.adapter.BusjiedianAdapter;
import com.example.administrator.renhua.ui.adapter.DataAdapter;
import com.example.administrator.renhua.ui.listener.OnScrollLastItemListener;
import com.example.administrator.renhua.ui.listener.OnScrollListener;
import com.example.administrator.renhua.ui.view.PullToRefreshLayout;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.example.administrator.renhua.utils.EndlessRecyclerOnScrollListener;
import com.example.administrator.renhua.utils.ToastUtil;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class ProgressListActivity extends BaseActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.sblash)
    TextView sblash;
    @Bind(R.id.shixiang)
    TextView shixiang;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.shenbanren)
    TextView shenbanren;
    @Bind(R.id.bumen)
    TextView bumen;
    @Bind(R.id.stu)
    TextView stu;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    String sblsh;
    String Sxmc;
    String randCode;
    String seesion;

    private BusjiedianAdapter adapter;
    private List<business> datas;
    private boolean mIsRefreshing = false;
    int currentPage=1;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progresslist);
        ButterKnife.bind(this);
        sblsh=getIntent().getStringExtra("Sblsh");
        Sxmc=getIntent().getStringExtra("Sxmc");
        randCode=getIntent().getStringExtra("randCode");
        seesion=getIntent().getStringExtra("seesion");
        initRecyclerView();
        loadData();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            mEndlessRecyclerOnScrollListener.refresh();
            loadData();
        });
    }
    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new BusjiedianAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //loadData();

            }
        };
        //recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        //setRecycleNoScroll();
        adapter.setOnItemClickListener(new BusjiedianAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
            @Override
            public void onLongClick(int position) {
                //ToastUtil.ShortToast(position+"");
            }
        });
    }

    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }

    public void loadData2(){
        String url = Constant.DOMAIN  + "bsznController.do?getBusinessAllState";
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();

        multipartBodyBuilder.setType(MultipartBody.FORM);

        multipartBodyBuilder.addFormDataPart("sblsh",sblsh);
        multipartBodyBuilder.addFormDataPart("randCode",randCode);
        multipartBodyBuilder.addFormDataPart("access_token",App.me().login().token);

        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.addHeader("cookie", seesion);
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsRefreshing=false;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if(str.contains("验证码错误")){
                    ToastUtil.LongToast("验证码错误");
                    finish();
                    return;
                }
                if(str.contains("login")){
                    ToastUtil.ShortToast("登录失效，请重新登录");
                    Intent it=new Intent(ProgressListActivity.this,LoginActivity.class);
                    startActivity(it);

                }else{
                    try {
                        JSONObject obj = new JSONObject(str);
                        JSONObject obj2 = obj.getJSONObject("attributes");
                        JSONArray array=obj2.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            String o = array.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                            BusItem annuncement= JSON.parseObject(o, BusItem.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sblash.setText(annuncement.getSBLSH());
                                    shixiang.setText(annuncement.getSBXMMC());
                                    shenbanren.setText(annuncement.getSQRMC());
                                    bumen.setText(annuncement.getDEPT_NAME());
                                    stu.setText(annuncement.getSTATE());
                                }
                            });
                            datas.addAll(annuncement.getBusiness());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        currentPage++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mIsRefreshing=false;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                call.cancel();
            }
        });

    }
    public void loadData(){
        String url = Constant.DOMAIN  + "bsznController.do?getBusinessAllState"+"&SBLSH="+sblsh+"&randCode="+randCode+"&access_token="+App.me().login().token;
        OkHttpClient client = new OkHttpClient();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.addHeader("cookie", seesion);
        Request request = RequestBuilder.url(url).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIsRefreshing=false;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String str = response.body().string();
                if(str.contains("验证码错误")){
                    ToastUtil.LongToast("验证码错误");
                    finish();
                    return;
                }
                if(str.contains("查询失败")){
                    ToastUtil.LongToast("查询失败");
                }

                if(str.contains("login")){
                    ToastUtil.ShortToast("登录失效，请重新登录");
                    Intent it=new Intent(ProgressListActivity.this,LoginActivity.class);
                    startActivity(it);

                }else{
                    try {
                        JSONObject obj = new JSONObject(str);
                        JSONObject obj2 = obj.getJSONObject("attributes");
                        JSONArray array=obj2.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            String o = array.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                            BusItem annuncement= JSON.parseObject(o, BusItem.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sblash.setText(annuncement.getSBLSH());
                                    shixiang.setText(annuncement.getSBXMMC());
                                    shenbanren.setText(annuncement.getSQRMC());
                                    bumen.setText(annuncement.getDEPT_NAME());
                                    stu.setText(annuncement.getSTATE());
                                }
                            });
                            datas.addAll(annuncement.getBusiness());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        currentPage++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mIsRefreshing=false;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                call.cancel();
            }
        });
    }
}
