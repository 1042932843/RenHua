package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.administrator.renhua.common.AsyncHttpDialog;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.entity.LoginResponse;

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
 * Created by Administrator on 2017/1/17 0017.
 */

public class UpdatePwdActivity extends BaseActivity {

    @Bind(R.id.original_pwd)
    EditText mOriginalPwd;
    @Bind(R.id.new_pwd)
    EditText mNewPwd;
    @Bind(R.id.confirm_pwd)
    EditText mConfirmPwd;
    @Bind(R.id.randcode)
    EditText mRandCode;
    @Bind(R.id.randomImg)
    ImageView mRandomImg;

    private UpdatePwd updatePwd;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;
    public String seesion;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        updatePwd = new UpdatePwd(this);
        okHttpClient = new OkHttpClient();
        ChangeImage();
    }

    @OnTextChanged(R.id.original_pwd)
    void onOriginalPwdChanged() {
        mOriginalPwd.setError(null);
    }

    @OnTextChanged(R.id.new_pwd)
    void onNewPwdChanged() {
        mNewPwd.setError(null);
    }

    @OnTextChanged(R.id.confirm_pwd)
    void onConfirmPwdChanged() {
        mConfirmPwd.setError(null);
    }

    @OnTextChanged(R.id.randcode)
    void onR(){
        mRandCode.setError(null);
    }

    @OnClick(R.id.submit)
    void onSubmit(){
        boolean doExecute = true;
        String oldPwd = mOriginalPwd.getText().toString();
        String newPwd = mNewPwd.getText().toString();
        String confirmPwd = mConfirmPwd.getText().toString();
        String code = mRandCode.getText().toString();

        if (StringUtil.isEmpty(oldPwd)) {
            doExecute = false;
            mOriginalPwd.setError("请输入原密码");
            mOriginalPwd.requestFocus();
        }else if (StringUtil.isEmpty(newPwd)) {
            doExecute = false;
            mNewPwd.setError("请设置新密码");
            mNewPwd.requestFocus();
        }else if (newPwd.length()<6 || newPwd.length()>20){
            doExecute = false;
            mNewPwd.setError("密码长度为6-20位");
            mNewPwd.requestFocus();
        } else if (!StringUtil.matchesPassword(newPwd)) {
            doExecute = false;
            mNewPwd.setError("密码格式不正确");
            mNewPwd.requestFocus();
        }else if (StringUtil.isEmpty(confirmPwd)) {
            doExecute = false;
            mConfirmPwd.setError("请输入确认密码");
            mConfirmPwd.requestFocus();
        } else if (!confirmPwd.equals(newPwd)) {
            doExecute = false;
            mConfirmPwd.setError("两次密码输入不一致");
            mConfirmPwd.requestFocus();
        }else if (StringUtil.isEmpty(code)){
            doExecute = false;
            mRandCode.setError("请输入验证码");
            mRandCode.requestFocus();
        }

        if (doExecute){
            final LoginResponse login = App.me().login();
//            updatePwd.submit(login.idcard, oldPwd, newPwd, code);
            Log.i("info_Login", "知道了session：" + seesion);
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("idcard", login.idcard)
                    .add("userid", login.id)
                    .add("oldpassword", oldPwd)
                    .add("password", newPwd)
                    .add("randCode", code)
                    .add("langCode", "zh-cn")
                    .build();
            Request request = new Request.Builder()
                    .addHeader("cookie", seesion)
                    .url(Constant.DOMAIN + "phoneUserOnlineController.do?updatePassword")
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
                        if (apiMsg.getState().equals("0")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    App.me().toast(apiMsg.getMessage());
                                }
                            });
                            App.me().logout();
                            EventBus.getDefault().post("login", "UserFragment.onLogoutSuccess");
                            Intent intent = new Intent(UpdatePwdActivity.this, LoginActivity.class);
                            intent.putExtra("idcard", login.idcard);
                            startActivity(intent);
                            finish();
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    App.me().toast(apiMsg.getMessage());
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

    private class UpdatePwd extends AsyncHttpDialog{
        public UpdatePwd(Context context) {
            super(context);
        }

        private void submit(String idcard, String oldPassWord, String newPassWord, String randCode){
            LoginResponse login = App.me().login();
            post(Constant.DOMAIN+"phoneUserOnlineController.do?updatePassword",
                    "userid", login.id, "oldpassword", oldPassWord, "password", newPassWord, "randCode", randCode);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getState().equals("0")){
                App.me().toast(apiMsg.getMessage());
                App.me().logout();
                EventBus.getDefault().post("login", "UserFragment.onLogoutSuccess");
                startActivity(new Intent(UpdatePwdActivity.this, LoginActivity.class));
                finish();
            }else {
                App.me().toast(apiMsg.getMessage());
            }
        }
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
                    Toast.makeText(UpdatePwdActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @OnClick(R.id.back)
    void onback(){
        onBackPressed();
    }
}
