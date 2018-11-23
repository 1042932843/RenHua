package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.entity.GovernmentForthTypes;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.adapter.GovernmentForthAdapter;
import com.example.administrator.renhua.ui.listener.OnScrollLastItemListener;
import com.example.administrator.renhua.ui.listener.OnScrollListener;
import com.example.administrator.renhua.ui.view.PullToRefreshLayout;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2017/1/17 0017.
 */

public class GovernmentForthListActivity extends BaseActivity implements PtrHandler, OnScrollLastItemListener, AdapterView.OnItemClickListener {

    @Bind(R.id.list)
    ListView mList;
    @Bind(R.id.header_title)
    TextView mTitle;
    private PullToRefreshLayout pull;
    private List<GovernmentForthTypes> items = new ArrayList<GovernmentForthTypes>();
    private GovernmentForthAdapter adapter;
    private UploadForthTypes uploadForthTypes;
    private String flag;
    private Handler handler = new Handler();
//    private EditText search;
    private String typeCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_third_list);
        ButterKnife.bind(this);
        pull = (PullToRefreshLayout) findViewById(R.id.pull);
//        search = (EditText) findViewById(R.id.search);
        //搜索关键字
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String a = search.getText().toString();
//                adapter.SearchCity(a);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        initViews();

        mList.setOnItemClickListener(this);
        mList.setOnScrollListener(new OnScrollListener(this));


//        flag = getIntent().getStringExtra("flag");
        mTitle.setText(getIntent().getStringExtra("title"));
        typeCode = getIntent().getStringExtra("typeCode");
        flag = getIntent().getStringExtra("flag");
        Log.d("reg", "typeCode:"+typeCode);
        LoginResponse login = App.me().login();
        uploadForthTypes = new UploadForthTypes(this);


        mList.setAdapter(adapter = new GovernmentForthAdapter(GovernmentForthListActivity.this));
    }

    protected void initViews() {
        pull.setPtrHandler(this);
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GovernmentForthTypes area = adapter.getItem(i);
//        Intent intent = new Intent(GovernmentForthListActivity.this, HandleActivity.class);
//        intent.putExtra("newsId", area.getItemsId());
//        intent.putExtra("title", area.getItemsName());

        Intent intent = new Intent(GovernmentForthListActivity.this, ServiceGuideActivity.class);
        intent.putExtra("serviceCode", area.getITEM_ID());
        intent.putExtra("itemCode", area.getCODE());
        intent.putExtra("title", area.getName());
        startActivity(intent);
    }

    @Override
    public void onScrollLastItem(AbsListView view) {
        if (flag.equals("depart")) {
            uploadForthTypes.getnext1(typeCode, "1", "1000");
        } else {
            uploadForthTypes.getnext(typeCode,  "1", "1000");
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if ("depart".equals(flag)) {
            uploadForthTypes.getDepart(typeCode, "1", "1000");
        } else {
            uploadForthTypes.getTypes(typeCode, "1", "1000");
        }

        //结束后调用
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull.refreshComplete();
            }
        }, 1000);
    }

    private class UploadForthTypes extends AsyncHttpDialog {
        private int page;
        private int page1;
        private boolean hasMore;
        private boolean hasMore1;


        public UploadForthTypes(Context context) {
            super(context);
        }

        private void getTypes(String typeCode, String str_currentPage, String str_pageRow) {
            page = 0;
            hasMore = false;
            post(HttpRequest.HttpMethod.POST,"bsznController.do?getItemList",
                    "catalog_id", typeCode, "start", page + "", "limit", "10");
        }

        private void getDepart(String depCode, String str_currentPage, String str_pageRow) {
            page1 = 0;
            hasMore1 = false;
            post(HttpRequest.HttpMethod.POST,"itemsController.do?findItemListByDep",
                    "depCode", depCode,"start", page1 + "", "limit", "10");
        }

        private void getnext(String typeCode, String str_currentPage, String str_pageRow) {

            if (hasMore) {
                post(HttpRequest.HttpMethod.POST,"itemsController.do?findItemListByItemsType",
                        "catalog_id", typeCode, "start", page + "", "limit", "10");

            }
        }

        private void getnext1(String typeCode, String str_currentPage, String str_pageRow) {
            if (hasMore1) {
                post(HttpRequest.HttpMethod.POST,"itemsController.do?findItemListByDep",
                        "catalog_id", typeCode, "start", page1 + "", "limit", "10");
            }

        }

        @Override
        public void onSuccess(final ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (adapter == null) {
                adapter = new GovernmentForthAdapter(GovernmentForthListActivity.this);
            } else {
                adapter.clear();
            }

            if (page == 0) {
                if (items == null) {
                    items = new ArrayList<GovernmentForthTypes>();
                } else {
                    items.clear();
                }
            }else  if (page1 == 0) {
                if (items == null) {
                    items = new ArrayList<GovernmentForthTypes>();
                } else {
                    items.clear();
                }
            }

           if (apiMsg.getSuccess().equals("true")) {
            try {
                JSONObject list = new JSONObject(apiMsg.getAttributes());
                JSONArray ja = list.getJSONArray("data");
                if (ja.length() <= 0) {
                    Log.d("reg", "000000");
                    App.me().toast("暂无数据");
                }
                for (int i = 0; i < ja.length(); i++) {
                    String o = ja.getString(i);
                    Log.d("reg", "o:" + o);
                    items.add(JSON.parseObject(o, GovernmentForthTypes.class));
                }
                    if (flag.equals("depart")) {
                        page1 += 1;
                        hasMore1 = ja.length() >= 10;
                    }else {
                        page += 1;
                        hasMore = ja.length() >= 10;
                    }
                Log.d("reg", "page1:" + page1);
                Log.d("reg", "page:" + page);
                Log.d("reg", "resArray.length():" + ja.length());
                Log.d("reg", "hasMore1:" + hasMore);
                adapter.addAll(items);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        App.me().toast(apiMsg.getMsg());
//                    }
//                });
           }

    }

    /*@Subscriber(tag = "CounselActivity.submitSuccess")
    void refresh(String idcard) {
        uploadForthTypes.getTypes(subTypeId, areaName, "1", "1000");
    }

    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_government_third_list_item, null);
    }

    @Override
    public Holder createHolder(View view, int position) {
        return new SeekHolder(view);
    }

    */

    /**
     * 查询结果
     * <p/>
     * 事件分派
     * <br>
     *//*
    public class SeekHolder extends Holder<GovernmentForthTypes> {

        @Bind(R.id.title)
        TextView mTitle;

        GovernmentForthTypes model;

        public SeekHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(GovernmentForthTypes item) {
            model = item;
            mTitle.setText(item.getItemsName());
        }

        @OnClick(R.id.seek_item)
        void onItemClick() {
            Intent intent = new Intent(GovernmentForthListActivity.this, InformationDetailActivity.class);
            intent.putExtra("newsId", model.getItemsId());
            startActivity(intent);
        }*/

//   }
    @OnClick(R.id.back)
    void onBack() {
        onBackPressed();
    }
}
