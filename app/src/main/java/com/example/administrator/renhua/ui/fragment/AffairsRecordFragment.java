package com.example.administrator.renhua.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.AsyncHttpDialog;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.BusItem;
import com.example.administrator.renhua.entity.Enclosure;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.entity.SeekDetailTwo;
import com.example.administrator.renhua.entity.cllnfoItem;
import com.example.administrator.renhua.ui.activity.ConsultDetailActivity;
import com.example.administrator.renhua.ui.activity.LoginActivity;
import com.example.administrator.renhua.ui.activity.ProgressActivity;
import com.example.administrator.renhua.ui.adapter.BusAdapter;
import com.example.administrator.renhua.ui.adapter.DataAdapter;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;
import com.example.administrator.renhua.utils.EndlessRecyclerOnScrollListener;
import com.example.administrator.renhua.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/17 0017.
 * 办事记录
 */

public class AffairsRecordFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private BusAdapter adapter;
    private List<BusItem> datas;
    private boolean mIsRefreshing = false;
    int currentPage=0;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_ar, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView();
        loaddataGet();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=0;
            datas.clear();
            adapter.notifyDataSetChanged();
            mEndlessRecyclerOnScrollListener.refresh();
            loaddataGet();
        });
    }
    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new BusAdapter(datas,getContext());
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loaddataGet();

            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();
        adapter.setOnItemClickListener(new BusAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent it=new Intent(getActivity(),ProgressActivity.class);
                it.putExtra("SBLSH",datas.get(position).getSBLSH());
                startActivity(it);

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

    public void loaddataGet(){
        try {

            String url = Constant.DOMAIN  + "bsznController.do?getBusiList"+"&access_token="+App.me().login().token+"&start="+currentPage+"&limit=10";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIsRefreshing=false;
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    // 注：该回调是子线程，非主线程
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIsRefreshing=false;
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    String str = response.body().string();
                    if(str.contains("login")||str.contains("登录")){
                        ToastUtil.ShortToast("登录失效，请重新登录");
                        Intent it=new Intent(getActivity(),LoginActivity.class);
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
                                datas.add(annuncement);
                            }
                            getActivity().runOnUiThread(new Runnable() {
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

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void loadData(){
        String url = Constant.DOMAIN  + "bsznController.do?getBusiList";
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("strat",currentPage+"");
        multipartBodyBuilder.addFormDataPart("limit",20+"");
        multipartBodyBuilder.addFormDataPart("access_token",App.me().login().token);

        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();
                getActivity().runOnUiThread(new Runnable() {
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

                Log.d("reg", "result:" + str);
                try {
                    JSONObject obj = new JSONObject(str);
                    JSONObject data= obj.getJSONObject("attributes");
                    JSONObject data2= data.getJSONObject("data");

                    JSONArray jsonArray = data2.getJSONArray("meterias");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String o = jsonArray.getString(i);
                        Enclosure annuncement= JSON.parseObject(o, Enclosure.class);
                        datas.add(annuncement);
                    }
                    currentPage++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
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

    }*/
}

