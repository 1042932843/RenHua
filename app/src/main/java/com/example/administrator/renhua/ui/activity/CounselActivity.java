package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.HttpTool;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.entity.LoginResponse;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 提交问题
 */
public class CounselActivity extends BaseActivity {

    @Bind(R.id.counsel_title)
    EditText mTitle;
    @Bind(R.id.counsel_content)
    EditText mContent;
    @Bind(R.id.randcode)
    EditText mRandCode;
    @Bind(R.id.randomImg)
    ImageView mRandomImg;

    LoginResponse login;
    public String seesion;
    private OkHttpClient okHttpClient;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsel);
        ButterKnife.bind(this);
        login = App.me().login();
        okHttpClient = new OkHttpClient();
        ChangeImage();
        if (login != null) {
//            if (BuildConfig.DEBUG) {
//                mContent.setText("测试");
//            }
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @OnTextChanged(R.id.counsel_title)
    void onT(){
        mTitle.setError(null);
    }

    @OnTextChanged(R.id.counsel_content)
    void onC(){
        mContent.setError(null);
    }

    @OnTextChanged(R.id.randcode)
    void onR(){
        mRandCode.setError(null);
    }

    @OnClick(R.id.submit)
    void onSaveClick() {
        App.me().hideInput(getWindow());
        final String title = mTitle.getText().toString();
        final String content = mContent.getText().toString();
        String code = mRandCode.getText().toString();
        if (StringUtil.isEmpty(title)) {
            mTitle.setError("请输入问题主题");
            mTitle.requestFocus();
        }else if (StringUtil.isEmpty(content)){
            mContent.setError("请输入问题内容");
            mContent.requestFocus();
        }else if (StringUtil.isEmpty(code)){
            mRandCode.setError("请输入验证码");
            mRandCode.requestFocus();
        }else {

            Log.i("info_Login", "知道了session：" + seesion);
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("qTitle", title)
                    .add("qContent", content)
                    .add("userid", login.id)
                    .add("randCode", code)
                    .add("langCode", "zh-cn")
                    .build();
            Request request = new Request.Builder()
                    .addHeader("cookie", seesion)
                    .url(Constant.DOMAIN + "questionController.do?saveQuestion")
                    .post(body)
                    .build();
            Log.d("reg", body.encodedValue(2));
            Call call2 = okHttpClient.newCall(request);
            call2.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("info_call2fail", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Headers headers = response.headers();
                        Log.i("info_response.headers", headers + "");
                        String r = response.body().string();
                        Log.e("reg", "r:" + r);
                        final ApiMsg apiMsg = JSON.parseObject(r, ApiMsg.class);
                        if (apiMsg.getSuccess().equals("true")) {
                            runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        App.me().toast(apiMsg.getMsg());
                                        Log.d("reg", "45465453");
                                        EventBus.getDefault().post(login.idcard, "CounselActivity.submitSuccess");
                                        finish();
                                    }
                                });
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
            });

        }
    }

    @OnClick(R.id.randomImg)
    void onRndomImg(){
        ChangeImage();
    }

    private void ChangeImage() {
        Request request = new Request.Builder()
                .url(Constant.url_randCodeImage + "random=" + System.currentTimeMillis())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_callFailure", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image = response.body().bytes();

                //通过handler更新UI
                Message message = handler.obtainMessage();
                message.obj = byte_image;
                message.what = SUCCESS;
                Log.i("info_handler", "handler");
                handler.sendMessage(message);

                //session
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);

                seesion = session.substring(0, session.indexOf(";"));
                Log.i("info_s", "session is  :" + seesion);

            }
        });
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //加载网络成功进行UI的更新,处理得到的图片资源
                case SUCCESS:
                    //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    mRandomImg.setImageBitmap(bitmap);

                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    Toast.makeText(CounselActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @OnClick(R.id.back)
    void onBack() {
        onBackPressed();
    }
}
