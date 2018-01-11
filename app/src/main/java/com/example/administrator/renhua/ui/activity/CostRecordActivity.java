package com.example.administrator.renhua.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.CostRecord;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 缴费记录
 */
public class CostRecordActivity extends BaseActivity implements HolderBuilder {

    @Bind(R.id.list)
    ListView mList;
    @Bind(R.id.no_data)
    TextView mNoData;

    private ArrayList<CostRecord> items;
    private HolderListAdapter<CostRecord> adapter;
//    private UploadQuestions uploadQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        ButterKnife.bind(this);
        LoginResponse login = App.me().login();
        if (login == null) {
            mNoData.setText("请先登录哦^-^");
            mNoData.setVisibility(View.VISIBLE);
        } else {
//            uploadQuestions = new UploadQuestions(this);
//            uploadQuestions.getPhoneCost(login.id);
            getListPay(login.id);
            mList.setAdapter(adapter = new HolderListAdapter<CostRecord>(this, this));
        }
    }

    private void getListPay(String userid){
        String url = Constant.DOMAIN+"jhMobilePayController.do?getOrdersMobileByPage";
        RequestParams params = new RequestParams();
        params.put("userid", userid);
        Log.d("reg", "url:"+url+" params:"+params);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                Log.d("reg", "result"+result);
                final ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
                if (apiMsg.getSuccess().equals("true")) {
                    try {
                        items = new ArrayList<CostRecord>();
                        JSONObject list = new JSONObject(apiMsg.getAttributes());
                        JSONArray resArray = list.getJSONArray("list");
                        if (resArray.length() <= 0) {
                            Log.d("reg", "000000");
                            mNoData.setVisibility(View.VISIBLE);
//                        App.me().toast("暂无数据");
                        }
                        for (int i = 0; i < resArray.length(); i++) {
                            String o = resArray.getString(i);
                            Log.d("reg", "o:" + o);
                            CostRecord questions = JSON.parseObject(o, CostRecord.class);
                            items.add(questions);
                        }
                        runOnUiThread(new Runnable() {
                            @SuppressLint("NewApi")
                            @Override
                            public void run() {
                                adapter.setNotifyOnChange(false);
                                adapter.clear();
                                adapter.addAll(items);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.me().toast(apiMsg.getMsg());
                        }
                    });
                }
            }
        });
    }

    @Subscriber(tag = "CounselActivity.submitSuccess")
    void refresh(String idcard) {
        LoginResponse login = App.me().login();
        getListPay(login.id);
    }

    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_cost_record_item, null);
    }

    @Override
    public Holder createHolder(View view, int position) {
        return new SeekHolder(view);
    }

    /**
     * 查询结果
     * <p/>
     * 事件分派
     * <br>
     */
    public class SeekHolder extends Holder<CostRecord> {

        @Bind(R.id.phone_number)
        TextView mPhone;
        @Bind(R.id.amt)
        TextView mAmt;
        @Bind(R.id.type)
        TextView mType;
        @Bind(R.id.time)
        TextView mTime;

        CostRecord model;

        public SeekHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(CostRecord item) {
            model = item;
            mPhone.setText(item.getPhoneNumber());
            Log.d("reg", "getTransAmt:"+item.getTransAmt()+"getTransDate:"+item.getTransDate());
            float amt = Float.parseFloat(item.getTransAmt())/100;
            Log.d("reg", "amt:"+amt);
            mAmt.setText(String.format("￥%.2f", amt));
            mType.setText("话费");
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            ParsePosition pos = new ParsePosition(0);
//            Date strtodate = formatter.parse(item.getTransDate(), pos);
//            String data = formatter.format(strtodate);
            mTime.setText(item.getTransDate());
        }

//        @OnClick(R.id.seek_item)
//        void onItemClick() {
//            Intent intent = new Intent(getActivity(), ConsultDetailActivity.class);
//            intent.putExtra("questionId", model.getId());
//            startActivity(intent);
//        }

    }

    @OnClick(R.id.back)
    void onBck(){
        onBackPressed();
    }
}

