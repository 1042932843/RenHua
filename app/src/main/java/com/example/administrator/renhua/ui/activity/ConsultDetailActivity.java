package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.HtmlStr;
import com.example.administrator.renhua.entity.SeekDetail;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

/**
 * Created by K on 2016/6/30.
 */
public class ConsultDetailActivity extends BaseActivity {

    @Bind(R.id.consult_title)
    TextView mTitle;
    @Bind(R.id.content)
    TextView mContent;
    @Bind(R.id.reply)
    TextView mReply;
    @Bind(R.id.question_type)
    TextView mQuestionType;

//    @Bind(R.id.time)
//    TextView mTime;

    String questionId;

    private ArrayList<SeekDetail> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        ButterKnife.bind(this);
        questionId = getIntent().getStringExtra("questionId");
        Log.d("reg", "questionId" + questionId);
        questionDetail(questionId);
        mReply.setHorizontalScrollBarEnabled(false);
        mReply.setVerticalScrollBarEnabled(false);
    }

    void initUserInfo(SeekDetail seekDetail) {
        mTitle.setText(seekDetail.getAskTitle());
        mContent.setText(seekDetail.getAskContent());
        Log.d("reg", "mContent:" + seekDetail.getContent());
        Log.d("reg", "mQuestionType:" + seekDetail.getQuestionType());
//        String content = StringUtil.decode64(seekDetail.getContent());
        String str = HtmlStr.delHTMLTag(seekDetail.getContent());

        if (seekDetail.getContent() == null){
            mReply.setText("暂无回复");
//            mReply.loadDataWithBaseURL("about:blank", "暂无回复", "text/html", "utf-8", null);
        }else {
            mReply.setText(str);
//            mReply.loadDataWithBaseURL("about:blank", seekDetail.getContent(), "text/html", "utf-8", null);
        }

//        mReply.getSettings().setMinimumFontSize(45);
        if (seekDetail.getTypeName() == null) {
            mQuestionType.setText("暂无分类");
        } else {
            mQuestionType.setText(seekDetail.getTypeName());
        }
//        mTime.setText(seekDetail.getTime());
    }

    void questionDetail(String id) {
        String url = Constant.DOMAIN + "questionController.do?findQuestionContent";
        RequestParams params = new RequestParams();
        params.put("objId", id);
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
                } else if (throwable!= null) {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.me().toast(apiMsg.getMessage());
                    }
                });
                if (apiMsg.getSuccess().equals("true")) {
                    String re = apiMsg.getResult();
                    Log.d("reg", "result:" + result);
                    try {
                        initUserInfo(JSON.parseObject(apiMsg.getObj(), SeekDetail.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    App.me().toast(apiMsg.getMsg());
                }
            }
        });
    }

//    void questionDetail(String id) {
//        String url = Constant.DOMAIN + "phoneQuestionController.do?queryQuestionById";
//        RequestParams params = new RequestParams();
//        params.put("questionId", id);
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(url, params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                if (throwable instanceof HttpException) {
//                    Log.d("reg", "HttpException");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            App.me().toast("网络不可用");
//                        }
//                    });
//                } else if (throwable instanceof ConnectTimeoutException) {
//                    Log.d("reg", "ConnectTimeoutException");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            App.me().toast("网络请求超时");
//                        }
//                    });
//                } else if (throwable instanceof JSONException) {
//                    App.me().toast("数据解析错误");
//                } else if (throwable instanceof NullPointerException) {
//                    App.me().toast("程序错误");
//                } else if (throwable!= null) {
//                    App.me().toast("未知错误");
//                } else {
//                    App.me().toast("未知错误");
//                }
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                String result = responseString.toString();
//                final ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
//                Log.d("reg", "result" + result);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        App.me().toast(apiMsg.getMessage());
//                    }
//                });
//                if (apiMsg.getState().equals("0")) {
//                    String re = apiMsg.getResult();
//                    Log.d("reg", "result:" + result);
//                    try {
//                        initUserInfo(JSON.parseObject(re, SeekDetail.class));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }
}
