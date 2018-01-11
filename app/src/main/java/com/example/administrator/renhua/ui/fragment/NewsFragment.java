package com.example.administrator.renhua.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.AsyncHttpDialog;
import com.example.administrator.renhua.entity.ApplianceRepair;
import com.example.administrator.renhua.ui.activity.ApplianceShopDetails;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsFragment extends Fragment implements HolderBuilder {

    private static final String ARG_POSITION = "position";
    private HolderListAdapter<ApplianceRepair> adapter;

    public String departName;
    @Bind(R.id.list)
    ListView mList;

    List<ApplianceRepair> applianceRepairs;
    GetMaintenance getMaintenance;

    public static NewsFragment newInstance(int position, String departName) {
        NewsFragment f = new NewsFragment();
        Log.d("reg", "departName:"+departName);
        Bundle b = new Bundle();
        b.putString("departName", departName);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        departName = getArguments().getString("departName");
        Log.d("reg", "onCreate");
        Log.d("reg", "departName:" + departName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home_appliance_shops, container, false);
        ButterKnife.bind(this, view);
        departName = getArguments().getString("departName");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getMaintenance = new GetMaintenance(getActivity());
        Log.d("reg", "onViewCreated");
        Log.d("reg", "departName2:" + departName);
        getMaintenance.loadData(departName, "1", "1000");
        mList.setAdapter(adapter = new HolderListAdapter<ApplianceRepair>(getActivity(), this));
    }

    private class GetMaintenance extends AsyncHttpDialog {

        public GetMaintenance(Context context) {
            super(context);
        }

        private void loadData(String departName, String str_currentPage, String str_pageRow) {
            post("dyTbJdwxController.do?getMaintenance",
                    "departName", departName, "str_currentPage", str_currentPage, "str_pageRow", str_pageRow);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            try {
                applianceRepairs = new ArrayList<ApplianceRepair>();
                JSONObject attr = new JSONObject(apiMsg.getAttributes());
                JSONArray jsonArray = attr.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
                    Log.d("reg", "o:" + o);
                    ApplianceRepair title1 = JSON.parseObject(o, ApplianceRepair.class);
                    applianceRepairs.add(title1);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setNotifyOnChange(false);
                        adapter.clear();
                        adapter.addAll(applianceRepairs);
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_appliance_shops_item, null);
    }

    @Override
    public Holder createHolder(View view, int position) {
        return new GetItems(view);
    }

    public class GetItems extends Holder<ApplianceRepair> {

        @Bind(R.id.title)
        TextView mTitle;

        ApplianceRepair model;

        public GetItems(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(ApplianceRepair item) {
            model = item;
            mTitle.setText(model.getEnterprise());
        }

        @OnClick(R.id.seek_item)
        void onSeekItem(){
            Intent intent = new Intent(getActivity(), ApplianceShopDetails.class);
            intent.putExtra("enterprise", model.getEnterprise());
            intent.putExtra("responsible", model.getResponsible());
            intent.putExtra("tel", model.getTel());
            intent.putExtra("creditCode", model.getCreditCode());
            intent.putExtra("address", model.getAddress());
            intent.putExtra("scope", model.getScope());
            startActivity(intent);
        }
    }
}