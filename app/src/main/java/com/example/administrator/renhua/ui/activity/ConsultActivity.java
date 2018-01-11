package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.entity.SeekQuestion;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class ConsultActivity extends BaseActivity implements HolderBuilder {

    @Bind(R.id.search_edit)
    EditText searchEdit;

    @Bind(R.id.list)
    ListView list;

    private ArrayList<SeekQuestion> items;
    private HolderListAdapter<SeekQuestion> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        ButterKnife.bind(this);
        uploadQuestion(null);
        list.setAdapter(adapter = new HolderListAdapter<SeekQuestion>(this, this));
    }

    @OnClick(R.id.consel)
    void onConsel() {
        LoginResponse login = App.me().login();
        if (login != null){
            startActivity(new Intent(this, CounselActivity.class));
        }else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @OnClick(R.id.search)
    void onSearch() {
        App.me().hideInput(getWindow());
        final String searchName = searchEdit.getText().toString();
        uploadQuestion(searchName);
    }

    private void uploadQuestion(String searchName) {
        String url = Constant.DOMAIN + "phoneQuestionController.do?fuzzyQueryByQuestion";
        RequestParams params = new RequestParams();
        params.put("searchName", searchName);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (throwable instanceof HttpException) {
                    Log.d("reg", "HttpException");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.me().toast("网络不可用");
                        }
                    });
                } else if (throwable instanceof ConnectTimeoutException) {
                    Log.d("reg", "ConnectTimeoutException");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.me().toast("网络请求超时");
                        }
                    });
                } else if (throwable instanceof JSONException) {
                    App.me().toast("数据解析错误");
                } else if (throwable instanceof NullPointerException) {
                    App.me().toast("程序错误");
                } else if (throwable != null) {
                    App.me().toast("未知错误");
                } else {
                    App.me().toast("未知错误");
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String result = responseString.toString();
                final ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
                Log.d("reg", "result" + result);
                if (apiMsg.getState().equals("0")) {
                    try {
                        items = new ArrayList<SeekQuestion>();
                        org.json.JSONObject re = new org.json.JSONObject(responseString);
                        JSONArray resArray = re.getJSONArray("result");
                        if (resArray.length() <= 0) {
                            Log.d("reg", "000000");
                            App.me().toast("暂无数据");
                        }
                        for (int i = 0; i < resArray.length(); i++) {
                            String o = resArray.getString(i);
                            Log.d("reg", "o:" + o);
                            SeekQuestion questions = JSON.parseObject(o, SeekQuestion.class);
                            items.add(questions);
                        }
                        runOnUiThread(new Runnable() {
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
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.me().toast(apiMsg.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Subscriber(tag = "CounselActivity.submitSuccess")
    void refresh(String idcard){
        uploadQuestion(null);
    }

    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_seek_item, null);
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
    public class SeekHolder extends Holder<SeekQuestion> {

        @Bind(R.id.time)
        TextView mTime;

        @Bind(R.id.title)
        TextView mTitle;

        @Bind(R.id.status)
        TextView mStatus;

        SeekQuestion model;

        public SeekHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(SeekQuestion item) {
            model = item;
            mTime.setText(item.getTime());
            mTitle.setText(item.name);
            if (model.getQuestionState() == null || model.getQuestionState().equals("待处理")) {
                mStatus.setText("[待处理]");
                mStatus.setTextColor(Color.rgb(179, 238, 58));
            } else if (model.getQuestionState().equals("已处理")) {
                mStatus.setText("[已回复]");
                mStatus.setTextColor(Color.rgb(247, 118, 28));
            }
        }

        @OnClick(R.id.seek_item)
        void onItemClick() {
            Intent intent = new Intent(ConsultActivity.this, ConsultDetailActivity.class);
            intent.putExtra("questionId", model.getQuestionId());
            startActivity(intent);
        }

    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }
}
