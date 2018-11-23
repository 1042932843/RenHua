package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.entity.GovernmentPersonalTitles;
import com.example.administrator.renhua.ui.adapter.PersonalThirdAdapter;
import com.example.administrator.renhua.ui.adapter.ServiceAdapter;
import com.example.administrator.renhua.ui.listener.OnScrollLastItemListener;
import com.example.administrator.renhua.ui.listener.OnScrollListener;
import com.example.administrator.renhua.ui.view.PullToRefreshLayout;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class GovernmentAffairsActivity extends BaseActivity implements PtrHandler, OnScrollLastItemListener {

    @Bind(R.id.checkBox1)
    CheckBox mPerson;
    @Bind(R.id.checkBox2)
    CheckBox mCompany;
    @Bind(R.id.tabLayout1)
    TabLayout tableLayout1;
    @Bind(R.id.tabLayout2)
    TabLayout tableLayout2;
    @Bind(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @Bind(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @Bind(R.id.person_area)
    LinearLayout mPersonArea;
    @Bind(R.id.company_area)
    LinearLayout mCompanyArea;

    private PullToRefreshLayout pull;
    private ListView listView;
    private PullToRefreshLayout pull1;
    private ListView listView1;

    private GetPersonalNames getPersonalNames;
    private ServiceHttp serviceHttp;
    public ArrayList<HashMap<String, String>> personalTitlesList;
    public ArrayList<HashMap<String, String>> companyTitlesList;
    public ArrayList<ArrayList<HashMap<String, String>>> thirdPersonTitleList;
    public ArrayList<ArrayList<HashMap<String, String>>> thirdCompanyTitleList;
    private List<GovernmentPersonalTitles> items = new ArrayList<GovernmentPersonalTitles>();
    private int action;
    int index = 0, index2 = 0;
    private PersonalThirdAdapter adapter;
    private ServiceAdapter serviceadapter;
    int flag = PERSON;
    public static int PERSON = 0;
    public static int COMPANY = 1;
    private Handler handler = new Handler();
    private int totalRow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affairs);
        ButterKnife.bind(this);
        pull = (PullToRefreshLayout) findViewById(R.id.pull);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GovernmentPersonalTitles area = serviceadapter.getItem(position);
                Intent intent = new Intent(GovernmentAffairsActivity.this, GovernmentForthListActivity.class);
                intent.putExtra("title", area.getName());
                intent.putExtra("typeCode", "" + area.getOrgCode());
                intent.putExtra("flag", "depart");
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(new OnScrollListener(this));
        pull1 = (PullToRefreshLayout) findViewById(R.id.pull1);

        listView1 = (ListView) findViewById(R.id.list1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GovernmentPersonalTitles area = serviceadapter.getItem(position);
                Intent intent = new Intent(GovernmentAffairsActivity.this, GovernmentForthListActivity.class);
                intent.putExtra("typeCode", "" + area.getOrgCode());
                startActivity(intent);
            }
        });
        listView1.setOnScrollListener(new OnScrollListener(this));
        getPersonalNames = new GetPersonalNames(this);
        serviceHttp = new ServiceHttp(this);

        initTab1();
    }

    private void initTab1(){
        personalTitlesList = new ArrayList<>();
        HashMap<String, String> map;
        String[]  names={"主题分类","事件分类", "群体分类","扶贫服务","服务部门"};
        String[]  code={"ztfl","rssj", "tdrq", "fpfw","dep"};
        thirdPersonTitleList = new ArrayList<>(names.length );
        for (int i = 0; i < names.length;i++) {
            map = new HashMap<String, String>();
            map.put("name", names[i]);
            map.put("code", code[i]);
            personalTitlesList.add(map);
            thirdPersonTitleList.add(new ArrayList<HashMap<String, String>>());
        }
        thirdPersonTitleList.add(new ArrayList<HashMap<String, String>>());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initViews();
                action = 3;
                getPersonalNames.getTypes(personalTitlesList.get(0).get("code"), "grbs", "1000", "1");
            }
        });
    }

    protected void initViews() {
        pull.setPtrHandler(this);
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });
        pull1.setPtrHandler(this);
        pull1.post(new Runnable() {
            @Override
            public void run() {
                pull1.autoRefresh();
            }
        });
        //1.MODE_SCROLLABLE模式  可滑动  MODE_FIXED模式  充满
        //tableLayout1.setTabMode(TabLayout.MODE_SCROLLABLE);

        for (int i = 0; i < personalTitlesList.size(); i++) {
            tableLayout1.addTab(tableLayout1.newTab().setText(personalTitlesList.get(i).get("name")));
        }
        tableLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tab = tableLayout1.getTabAt(tab.getPosition());
                index = tab.getPosition();
                listView.setAdapter(serviceadapter = new ServiceAdapter(GovernmentAffairsActivity.this));
                action = 3;
                if (index == 4) {
                    pull.setVisibility(View.VISIBLE);
                    recyclerView1.setVisibility(View.GONE);
                    serviceHttp.getDepart("4",  "10", "1");//个人办事
                }else {
                    pull.setVisibility(View.GONE);
                    recyclerView1.setVisibility(View.VISIBLE);
                }
                if (thirdPersonTitleList.get(index).size() != 0 && index != 4) {
                    try {
                        initRecycler();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return;
                }
                if (tab.getPosition() !=4) {
                    getPersonalNames.getTypes(personalTitlesList.get(tab.getPosition()).get("code"), "grbs", "1000", "1");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initRecycler() {
        recyclerView1.removeAllViews();
        //3列   方向垂直
        StaggeredGridLayoutManager mgr = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(mgr);
        //设置适配器
        try {
            adapter = new PersonalThirdAdapter(GovernmentAffairsActivity.this, thirdPersonTitleList.get(index));
            adapter.notifyDataSetChanged();
            recyclerView1.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initRecycler2() {
        recyclerView2.removeAllViews();
        //3列   方向垂直
        StaggeredGridLayoutManager mgr = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(mgr);
        //设置适配器
        adapter = new PersonalThirdAdapter(GovernmentAffairsActivity.this, thirdCompanyTitleList.get(index2));
        recyclerView2.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void initViews2() {
        LogUtils.d("initViews2");
        for (int i = 0; i < companyTitlesList.size(); i++) {
            tableLayout2.addTab(tableLayout2.newTab().setText(companyTitlesList.get(i).get("name")));
        }
        tableLayout2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                listView1.setAdapter(serviceadapter = new ServiceAdapter(GovernmentAffairsActivity.this));
                index2 = tab.getPosition();
                action = 3;
                if (index2 == 4) {
                    pull1.setVisibility(View.VISIBLE);
                    recyclerView2.setVisibility(View.GONE);
                    serviceHttp.getDepart("4", "10", "1");//企业办事
                } else {
                    pull1.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                }
                if (tab.getPosition() != 4) {
                    getPersonalNames.getTypes(companyTitlesList.get(tab.getPosition()).get("code"), "qybs", "1000", "1");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @OnClick(R.id.checkBox1)
    void onNavQueryClick() {
        flag = PERSON;
        mPerson.setChecked(true);
        mCompany.setChecked(false);
        mPersonArea.setVisibility(View.VISIBLE);
        mCompanyArea.setVisibility(View.GONE);
    }

    @OnClick(R.id.checkBox2)
    void onNavCardClick() {
        flag = COMPANY;
        mPerson.setChecked(false);
        mCompany.setChecked(true);
        mPersonArea.setVisibility(View.GONE);
        mCompanyArea.setVisibility(View.VISIBLE);
        if (companyTitlesList == null) {
            initTab2();
        }
    }

    private void initTab2(){
        companyTitlesList = new ArrayList<>();
        HashMap<String, String> map;
        String[] titles = {"主题分类", "经营活动", "特定对象","扶贫服务", "服务部门"};
        String[]  code={"ztfl","jy", "tddx", "fpfw","dep"};
        thirdCompanyTitleList = new ArrayList<>(titles.length);
        for (int i = 0; i < titles.length; i++) {
            map = new HashMap<String, String>();
            map.put("name", titles[i]);
            map.put("code", code[i]);
            companyTitlesList.add(map);
            thirdCompanyTitleList.add(new ArrayList<HashMap<String, String>>());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initViews2();
                action = 3;
                getPersonalNames.getTypes(companyTitlesList.get(0).get("code"), "qybs", "1000", "1");
            }
        });
    }

    @OnClick(R.id.back)
    void onBck() {
        onBackPressed();
    }

    @OnClick(R.id.progress)
    void onProgress() {
        startActivity(new Intent(this, ProgressActivity.class));
    }

    @OnClick(R.id.affairs_order)
    void onOrder() {
        App.me().toast("建设中");
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (index == 3 || index2 == 3){
            serviceHttp.getDepart("4", "10", "1");//服务部门下拉刷新
        }else if (index == 4){
            serviceHttp.getDepart("5", "10", "1");//镇街服务
        }
        //结束后调用
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull.refreshComplete();
                pull1.refreshComplete();
            }
        }, 1000);
    }


    @Override
    public void onScrollLastItem(AbsListView view) {
        if (index == 3 || index2 == 3){
            serviceHttp.getnext("4", "10", "1");//上拉加载
        }else if (index == 4){
            serviceHttp.getnext("5", "10", "1");//上拉加载
        }
    }

    //获取服务部门、镇街服务数据
    private class ServiceHttp extends AsyncHttpDialog {
        private int page;
        private String type;
        private boolean hasMore;

        public ServiceHttp(Context context) {
            super(context);
        }

        private void getDepart(String typeCode, String str_pageRow, String str_currentPage) {
            type=typeCode;
            page = 1;
            hasMore = false;
            if(typeCode.equals("4")){
                post(HttpRequest.HttpMethod.POST,"bsznController.do?getOrgListEx",
                        "type", "organ");
            }
            if(typeCode.equals("5")){
                post(HttpRequest.HttpMethod.POST,"townController.do?getCunZhen",
                        "type", "organ");
            }

        }

        private void getnext(String typeCode, String str_pageRow, String str_currentPage) {
            if (hasMore) {
                type=typeCode;
                if(typeCode.equals("4")){
                    post(HttpRequest.HttpMethod.POST,"bsznController.do?getOrgListEx",
                            "type", "organ");
                }
                if(typeCode.equals("5")){
                    post(HttpRequest.HttpMethod.POST,"bsznController.do?getCunZhen",
                            "type", "organ");
                }
            }
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (serviceadapter == null) {
                serviceadapter = new ServiceAdapter(GovernmentAffairsActivity.this);
            } else {
                serviceadapter.clear();
            }
            if (page == 1) {
                if (items == null) {
                    items = new ArrayList<GovernmentPersonalTitles>();
                } else {
                    items.clear();
                }
            }

            if (apiMsg.getSuccess().equals("true")) {
                try {
                    JSONObject list = new JSONObject(apiMsg.getAttributes());
                    JSONArray ja=new JSONArray();
                    if(type.equals("4")){
                        ja = list.getJSONArray("data");
                        totalRow = list.getInt("total");
                        Log.d("总共条数", "total:"+totalRow);
                        if (ja.length() <= 0) {
                            Log.d("reg", "000000");
                            App.me().toast("暂无数据");
                        }
                    }
                    if(type.equals("5")){
                        ja = list.getJSONArray("cunNameList");
                        if (ja.length() <= 0) {
                            Log.d("reg", "000000");
                            App.me().toast("暂无数据");
                        }
                    }

                    for (int i = 0; i < ja.length(); i++) {
                        String o = ja.getString(i);
                        Log.d("reg", "o:" + o);
                        items.add(JSON.parseObject(o, GovernmentPersonalTitles.class));
                    }
                    int ww = ((totalRow-1)/10)+1;
                    if (page<ww){
                        page += 1;
                        hasMore=true;
                    }else {
                        hasMore=false;
                    }
                    Log.d("reg", "page:" + page);
                    Log.d("reg", "ww:" + ww);
                    Log.d("reg", "hasMore:" + hasMore);
                    serviceadapter.addAll(items);
                    serviceadapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

//获取事件、主题、群体等数据
    public class GetPersonalNames extends AsyncHttpDialog {

        public GetPersonalNames(Context context) {
            super(context);
        }

        private void getTypes(String navCode, String menuCode, String pageSize, String currentPage) {
            post(HttpRequest.HttpMethod.POST,"itemsController.do?findItemTypeList",
                    "navCode", navCode, "menuCode", menuCode, "str_pageRow", pageSize, "str_currentPage", currentPage);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getSuccess().equals("true")) {
                switch (action) {
                    //获取三级菜单
                    case 3:
                        try {
                            JSONObject obj = new JSONObject(apiMsg.getAttributes());
                            JSONArray jsonArray = obj.getJSONArray("list");
                            HashMap<String, String> map;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                GovernmentPersonalTitles title = JSON.parseObject(o, GovernmentPersonalTitles.class);
                                map = new HashMap<String, String>();
                                map.put("name", title.getName());
                                map.put("typeCode", title.getCatalog_id());
                                if (flag == PERSON) {
                                    thirdPersonTitleList.get(index).add(map);
                                } else {
                                    thirdCompanyTitleList.get(index2).add(map);
                                }
                            }
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (flag == PERSON) {
                                            initRecycler();
                                        } else {
                                            initRecycler2();
                                        }

                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } else {
                App.me().toast(apiMsg.getMsg());
            }
        }
    }


}
