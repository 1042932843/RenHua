package com.example.administrator.renhua.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.renhua.ui.activity.ConsultDetailActivity;
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
 * Created by Administrator on 2016/11/17 0017.
 * 问题记录
 */

public class QuestionRecordFragment extends BaseFragment implements HolderBuilder {

    @Bind(R.id.list)
    ListView mList;
    @Bind(R.id.no_data)
    TextView mNoData;

    private ArrayList<SeekDetailTwo> items;
    private HolderListAdapter<SeekDetailTwo> adapter;
    private UploadQuestions uploadQuestions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_common, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LoginResponse login = App.me().login();
        if (login == null){
            mNoData.setText("请先登录哦^-^");
            mNoData.setVisibility(View.VISIBLE);
        }else {
           // uploadQuestions = new UploadQuestions(getActivity());
            //uploadQuestions.getQuestions(login.id);
            //mList.setAdapter(adapter = new HolderListAdapter<SeekDetailTwo>(getActivity(), this));
        }
    }

    private class UploadQuestions extends AsyncHttpDialog {

        public UploadQuestions(Context context) {
            super(context);
        }

        private void getQuestions(String userId){
            post(Constant.DOMAIN+"questionController.do?getQuestionRecordByPage",
                    "userid", userId);
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
//                        App.me().toast("暂无数据");
                        mNoData.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < resArray.length(); i++) {
                        String o = resArray.getString(i);
                        Log.d("reg", "o:" + o);
                        SeekDetailTwo questions = JSON.parseObject(o, SeekDetailTwo.class);
                        items.add(questions);
                    }
                    getActivity().runOnUiThread(new Runnable() {
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
                getActivity().runOnUiThread(new Runnable() {
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
        //LoginResponse login = App.me().login();
        //uploadQuestions.getQuestions(login.id);
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

        SeekDetailTwo model;
        @Bind(R.id.status)
        TextView mStatus;
        public SeekHolder(@NonNull View view) {
            super(view);
        }

        @Override
        public void onBind(SeekDetailTwo item) {
            model = item;
            mTitle.setText(item.getAskTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            mTime.setText(sdf.format(item.getCreateDate()));
            if (model.getAskContent()== null || model.getStatus().equals("待处理")) {
                mStatus.setText("[待处理]");
                mStatus.setTextColor(Color.rgb(179, 238, 58));
            } else if (model.getStatus().equals("已处理")) {
                mStatus.setText("[已回复]");
                mStatus.setTextColor(Color.rgb(247, 118, 28));
            }
        }

        @OnClick(R.id.seek_item)
        void onItemClick() {
            Intent intent = new Intent(getActivity(), ConsultDetailActivity.class);
            intent.putExtra("questionId", model.getId());
            startActivity(intent);
        }

    }
}

