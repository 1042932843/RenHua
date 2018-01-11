package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.common.ToolsUtil;
import com.example.administrator.renhua.entity.LoginResponse;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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
 * Created by Administrator on 2016/11/18 0018.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.idcard)
    EditText mIdcard;
    @Bind(R.id.pwd)
    EditText mPwd;
    @Bind(R.id.randcode)
    EditText mRandCode;
    @Bind(R.id.randCode_image)
    ImageView mRandImg;
    String userNameValue, passwordValue;
    public String seesion;
    private OkHttpClient okHttpClient;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        okHttpClient = new OkHttpClient();
        ChangeImage();
    }

    @OnClick(R.id.register)
    void onRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void initView() {
        Intent intent = getIntent();
        SharedPreferences config = App.me().config();
        if (intent.hasExtra("idcard")) {
            mIdcard.setText(intent.getStringExtra("idcard"));
        }
    }

    @OnTextChanged(R.id.idcard)
    void onIdcardTextChange() {
        mIdcard.setError(null);
    }

    @OnTextChanged(R.id.pwd)
    void onPwdTextChang() {
        mPwd.setError(null);
    }

    @OnTextChanged(R.id.randcode)
    void onRandCodeChang() {
        mPwd.setError(null);
    }

    @OnClick(R.id.login)
    void onLoginClick() {
        boolean doExecute = true;
        userNameValue = mIdcard.getText().toString();
        passwordValue = mPwd.getText().toString();
        String randcode = mRandCode.getText().toString();

        if (StringUtil.isEmpty(userNameValue)) {
            doExecute = false;
            mIdcard.setError("请输入身份证号码");
            mIdcard.requestFocus();
        }else if (!StringUtil.matchesIdCard(userNameValue)){
            doExecute = false;
            mIdcard.setError("身份证格式错误");
            mIdcard.requestFocus();
        }else if (StringUtil.isEmpty(passwordValue)) {
            doExecute = false;
            mPwd.setError("请输入密码");
            mPwd.requestFocus();
        } else if (!StringUtil.matchesPassword(passwordValue)) {
            doExecute = false;
            mPwd.setError("登录密码为6-20位");
        }else if (StringUtil.isEmpty(randcode)){
            doExecute = false;
            mRandCode.setError("请输入验证码");
            mRandCode.requestFocus();
        }

//        if (mRemPwd.isChecked()) {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("USER_NAME", userNameValue);
//            editor.putString("PASSWORD", passwordValue);
//            editor.putString("ISCHECKED", "isChecked");
//            editor.commit();
//        } else {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("ISCHECKED", "noChecked");
//            editor.clear().commit();
//        }

        if (doExecute) {
            App.me().hideInput(getWindow());
            if (!ToolsUtil.isNetworkAvailable(this)) {
                App.me().toast("无网络连接");
                return;
            } else {
                Log.i("info_Login", "知道了session：" + seesion);
                FormBody body = new FormBody.Builder()
                        .add("identityCard", userNameValue)
                        .add("passwd", passwordValue)
                        .add("randcode", mRandCode.getText().toString())
                        .add("langCode", "zh-cn")
                        .build();
                Request request = new Request.Builder()
                        .addHeader("cookie", seesion)
                        .url(Constant.DOMAIN + "registerController.do?login")
                        .post(body)
                        .build();
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
                            Log.i("info_respons.headers", headers + "");
                            String r = response.body().string();
                            Log.e("reg", "r:" + r);
                            final ApiMsg apiMsg = JSON.parseObject(r, ApiMsg.class);
                            if (apiMsg.getSuccess().equals("true")) {
                                LoginResponse login = JSON.parseObject(apiMsg.getObj(), LoginResponse.class);
                                onLoginSuccess(login);
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
    }

    @OnClick(R.id.randCode_image)
    void onRndomImg() {
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
                    mRandImg.setImageBitmap(bitmap);

                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    Toast.makeText(LoginActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    void onLoginSuccess(LoginResponse login) {
        App.me().login(login);
        login = App.me().login();
        Log.d("reg", "LoginActivity.onLoginSuccess");
        EventBus.getDefault().post(login, "LoginActivity.onLoginSuccess");
        finish();
    }

    @Subscriber(tag = "RegTwoActivity.onRegSuccess")
    void onRegSuccess(String idcard) {
        Log.d("reg", "idcard" + idcard);
        mIdcard.setText(idcard);
        mPwd.setText("");
    }

    @OnClick(R.id.back)
    void onBack() {
        onBackPressed();
    }
}
