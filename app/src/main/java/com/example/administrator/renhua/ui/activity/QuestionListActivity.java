package com.example.administrator.renhua.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.entity.SeekDetailTwo;
import com.example.administrator.renhua.ui.view.Holder;
import com.example.administrator.renhua.ui.view.HolderBuilder;
import com.example.administrator.renhua.ui.view.HolderListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class QuestionListActivity extends BaseActivity implements HolderBuilder {

    @Bind(R.id.list)
    ListView list;

    private ArrayList<SeekDetailTwo> items;
    private HolderListAdapter<SeekDetailTwo> adapter;
    private String questionId;
    private UploadQuestions uploadQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        ButterKnife.bind(this);
        LoginResponse login = App.me().login();
        questionId = getIntent().getStringExtra("questionId");
        Log.d("reg", "questionId" + questionId);
        uploadQuestions = new UploadQuestions(this);
        uploadQuestions.getQuestions(questionId, "10000");
        list.setAdapter(adapter = new HolderListAdapter<SeekDetailTwo>(this, this));
    }

    private class UploadQuestions extends AsyncHttpDialog {

        public UploadQuestions(Context context) {
            super(context);
        }

        private void getQuestions(String typeId, String str_pageRow){
            post(Constant.DOMAIN+"questionController.do?getQuestionListPage",
                    "typeId", typeId, "str_pageRow", str_pageRow);
        }

        @Override
        public void onSuccess(final ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getSuccess().equals("true")) {
                try {
                    items = new ArrayList<SeekDetailTwo>();
                    JSONObject list = new JSONObject(apiMsg.getAttributes());
                    JSONArray resArray = list.getJSONArray("list");
                    if (resArray.length() <= 0) {
                        Log.d("reg", "000000");
                        App.me().toast("暂无数据");
                    }
                    for (int i = 0; i < resArray.length(); i++) {
                        String o = resArray.getString(i);
                        Log.d("reg", "o:" + o);
                        SeekDetailTwo questions = JSON.parseObject(o, SeekDetailTwo.class);
                        items.add(questions);
                    }
                    runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
                        App.me().toast(apiMsg.getMsg());
                    }
                });
            }
        }
    }

    @Subscriber(tag = "CounselActivity.submitSuccess")
    void refresh(String idcard){
        uploadQuestions.getQuestions(questionId, "10000");
    }


    @Override
    public View inflateView(Context context, int position) {
        return View.inflate(context, R.layout.layout_question_list_item, null);
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
    public class SeekHolder extends Holder<SeekDetailTwo> {

        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.time)
        TextView mTime;
        @Bind(R.id.status)
        TextView mStatus;
        SeekDetailTwo model;

        public SeekHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(SeekDetailTwo item) {
            model = item;
            mTitle.setText(item.getAskTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            mTime.setText(sdf.format(item.getCreateDate()));
            if (model.getStatus().equals("待处理")||model.getStatus().equals("未处理")) {
                mStatus.setText("[待处理]");
                mStatus.setTextColor(Color.rgb(179, 238, 58));
            } else if (model.getStatus().equals("已处理")) {
                mStatus.setText("[已回复]");
                mStatus.setTextColor(Color.rgb(247, 118, 28));
            }

        }

        @OnClick(R.id.seek_item)
        void onItemClick() {
            Intent intent = new Intent(QuestionListActivity.this, ConsultDetailActivity.class);
            intent.putExtra("questionId", model.getId());
            startActivity(intent);
        }

    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }
}
