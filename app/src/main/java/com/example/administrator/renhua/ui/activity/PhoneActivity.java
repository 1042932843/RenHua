package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.MyOkHttp;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.common.ToolsUtil;
import com.example.administrator.renhua.entity.GetBillInfoResponse;
import com.example.administrator.renhua.entity.LoginResponse;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by K on 2016/3/22.
 */
public class PhoneActivity extends BaseActivity {

    @Bind(R.id.phone)
    EditText mPhone;
    @Bind(R.id.rg1)
    RadioGroup mRg;
    @Bind(R.id.money_thirty)
    RadioButton mThirty;
    @Bind(R.id.money_fifty)
    RadioButton mFifty;
    @Bind(R.id.money_hundred)
    RadioButton mHundred;
    @Bind(R.id.go_to_pay)
    Button mPay;
    @Bind(R.id.randcode)
    EditText mRandCode;
    @Bind(R.id.randomImg)
    ImageView mRandomImg;

    LoginResponse login;
    int action; // 1:获取面值 2:获取TN号
    GetBillInfoResponse selected; // 选中面值
    List<GetBillInfoResponse> items; // 全部面值
    private static final int SUCCESS = 1;
    private static final int FALL = 2;
    public String seesion;

    private OkHttpClient okHttpClient;
    PhoneOkHttp phoneOkHttp;
    private String OrdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_cost);
        ButterKnife.bind(this);
        login = App.me().login();
        phoneOkHttp = new PhoneOkHttp(this);
        okHttpClient = new OkHttpClient();
        ChangeImage();
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                App.me().hideInput(getWindow());
                if (checkedId == mThirty.getId()) {
                    action = 1;
                } else if (checkedId == mFifty.getId()) {
                    action = 2;
                } else if (checkedId == mHundred.getId()) {
                    action = 3;
                }
            }
        });
    }

    @OnTextChanged(R.id.randcode)
    void onR() {
        mRandCode.setError(null);
    }

    @OnTextChanged(R.id.phone)
    void onPhone() {
        mPhone.setError(null);
    }

    @OnClick(R.id.go_to_pay)
    void onPayClick() {
        App.me().hideInput(getWindow());
        boolean doExecute = true;
        String phone = mPhone.getText().toString();
        String code = mRandCode.getText().toString();

        if (StringUtil.isEmpty(phone)) {
            doExecute = false;
            mPhone.setError("请输入手机号码");
        } else if (!StringUtil.matchesPhone(phone)) {
            doExecute = false;
            mPhone.setError("手机格式不正确");
        } else if (phone.matches("^1[7]\\d{9}$")) {
            doExecute = false;
            App.me().toast("抱歉,暂不支持17开头的号码进行充值");
        } else if (StringUtil.isEmpty(code)) {
            doExecute = false;
            mRandCode.setError("请输入验证码");
            mRandCode.requestFocus();
        }

        if (doExecute) {
            mPay.setEnabled(true);
            String idcard, userid;
            if (login == null) {
                idcard = "";
                userid = "";
            } else {
                idcard = login.idcard;
                userid = login.id;
            }

            if (!ToolsUtil.isNetworkAvailable(this)) {
                App.me().toast("无网络连接");
                return;
            } else {
                if (action == 1) {
                    phoneOkHttp.goToPay("3000", phone, userid, idcard, userid, code);
                } else if (action == 2) {
                    phoneOkHttp.goToPay("5000", phone, userid, idcard, userid, code);
                } else if (action == 3) {
                    phoneOkHttp.goToPay("10000", phone, userid, idcard, userid, code);
                } else {
                    App.me().toast("请选择充值面额");
                }
            }
        }
    }

    private class PhoneOkHttp extends MyOkHttp {

        public PhoneOkHttp(Context context) {
            super(context);
        }

        private void goToPay(String amt, String phone, String code, String idcard, String userid, String randCode) {
            call(seesion, "phoneMobileController.do?getTn",
                    "amt", amt, "phone", phone, "code", code,
                    "idcard", idcard, "userid", userid,
                    "randCode", randCode, "langCode", "zh-cn");
        }

        @Override
        public void onSuccess(final ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getState().equals("0")) {
                try {
                    JSONObject result = new JSONObject(apiMsg.getResult());
                    final String tn = result.getString("tn");
                    OrdId = result.getString("OrdId");
                    Log.d("reg", "tn:" + tn);
                    Log.d("reg", "OrdId:" + OrdId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPay.setEnabled(false);
                            UPPayAssistEx.startPayByJAR(PhoneActivity.this, PayActivity.class, null, null, tn, "00");
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
    }

    @OnClick(R.id.randomImg)
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
                    mRandomImg.setImageBitmap(bitmap);

                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    Toast.makeText(PhoneActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        boolean isSuccess = false;
        mPay.setEnabled(true);
        String str = data.getStringExtra("pay_result");
        if (str.equalsIgnoreCase("success")) {
            feedbackStatus();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        if (str.equalsIgnoreCase("success")) {
            isSuccess = true;
            builder.setMessage("支付成功！");
        } else if (str.equalsIgnoreCase("fail")) {
            builder.setMessage("支付失败！");
        } else if (str.equalsIgnoreCase("cancel")) {
            builder.setMessage("用户取消了支付");
        }
        builder.setInverseBackgroundForced(true);
        final boolean finalIsSuccess = isSuccess;
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (finalIsSuccess) {
                    finish();
                }
            }
        });
        builder.show();
    }

    @OnClick(R.id.back)
    void onback() {
        onBackPressed();
    }

    private void feedbackStatus(){
        FormBody body = new FormBody.Builder()
                .add("OrdId", OrdId)
                .add("PayStat", "1001")
                .add("langCode", "zh-cn")
                .build();
        Request request = new Request.Builder()
                .addHeader("cookie", seesion)
                .url(Constant.DOMAIN + "phoneMobileController.do?getOrdersAndJump")
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

                }
            }
        });
    }

}

