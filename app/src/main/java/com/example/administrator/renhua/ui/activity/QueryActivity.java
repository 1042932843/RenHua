package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.administrator.renhua.common.AsyncHttpDialog;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.QuestionType;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

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
 * Created by Administrator on 2016/11/18 0018.
 */

public class QueryActivity extends BaseActivity implements HolderBuilder {

    @Bind(R.id.list)
    ListView mList;

    private ArrayList<QuestionType> items;
    private HolderListAdapter<QuestionType> adapter;
    private GetTypes getTypes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        getTypes = new GetTypes(this);
//        getTypes.getQuestionTypes(null);
        getUserInfo();
        mList.setAdapter(adapter = new HolderListAdapter<QuestionType>(this, this));
    }

    private void getUserInfo() {
        String url = Constant.DOMAIN + "questionTypeController.do?getQuestionTypeList";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                Log.d("reg", "result" + result);
                final ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
                if (apiMsg.getSuccess().equals("true")) {
                    try {
                        items = new ArrayList<QuestionType>();
                        JSONArray resArray = new JSONArray(apiMsg.getObj());
                        if (resArray.length() <= 0) {
                            Log.d("reg", "000000");
                            App.me().toast("暂无数据");
                        }
                        for (int i = 0; i < resArray.length(); i++) {
                            String o = resArray.getString(i);
                            Log.d("reg", "o:" + o);
                            QuestionType questions = JSON.parseObject(o, QuestionType.class);
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
                } else {
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

    private class GetTypes extends AsyncHttpDialog {
        public GetTypes(Context context) {
            super(context);
        }

        private void getQuestionTypes(String typeId) {
            post("questionTypeController.do?getQuestionTypeList",
                    "typeId", typeId);
        }

        @Override
        public void onSuccess(final ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            Log.d("reg", "8908980");
            if (apiMsg.getSuccess().equals("true")) {
                try {
                    items = new ArrayList<QuestionType>();
                    JSONArray resArray = new JSONArray(apiMsg.getObj());
                    if (resArray.length() <= 0) {
                        Log.d("reg", "000000");
                        App.me().toast("暂无数据");
                    }
                    for (int i = 0; i < resArray.length(); i++) {
                        String o = resArray.getString(i);
                        Log.d("reg", "o:" + o);
                        QuestionType questions = JSON.parseObject(o, QuestionType.class);
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
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.me().toast(apiMsg.getMsg());
                    }
                });
            }
        }
    }


    @Subscriber(tag = "CounselActivity.submitSuccess")
    void refresh(String idcard){
//        getTypes.getQuestionTypes(null);
        getUserInfo();
    }

    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_question_type_item, null);
    }

    @Override
    public Holder createHolder(View view, int position) {
        return new QueryActivity.SeekHolder(view);
    }

    /**
     * 查询结果
     * <p/>
     * 事件分派
     * <br>
     */
    public class SeekHolder extends Holder<QuestionType> {

        @Bind(R.id.title)
        TextView mTitle;

        QuestionType model;

        public SeekHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(QuestionType item) {
            model = item;
            mTitle.setText(item.name);
        }

        @OnClick(R.id.seek_item)
        void onItemClick() {
            Intent intent = new Intent(QueryActivity.this, QuestionListActivity.class);
            intent.putExtra("questionId", model.getId());
            startActivity(intent);
        }

    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }

}

