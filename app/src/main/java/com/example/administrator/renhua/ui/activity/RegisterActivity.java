package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.entity.Town;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
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

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.user_name)
    EditText mName;
    @Bind(R.id.idcard)
    EditText mIdcard;
    @Bind(R.id.address)
    EditText mAddress;
    @Bind(R.id.phone)
    EditText mPhone;
    @Bind(R.id.town)
    TextView town;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.password2)
    EditText mPassword2;
    @Bind(R.id.randCode_image)
    ImageView img_identy;
    @Bind(R.id.randcode)
    EditText mRandCode;
    @Bind(R.id.sex)
    Button mBottom;
    @Bind(R.id.political)
    Button mPolitical;
    @Bind(R.id.idcard_address)
    EditText mIdcardAddress;
    @Bind(R.id.fix_phone)
    EditText mFixPhone;
    @Bind(R.id.email)
    EditText mEmail;

    ListPopupWindow sexPopupWindow, politicalPopupWindow;
    String sex = "", political = "";
    private List<String> strings1, strings2;
    private Town towns;
    public String seesion;
    private OkHttpClient okHttpClient;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        okHttpClient = new OkHttpClient();
        ChangeImage();
        strings1 = new ArrayList<String>();
        strings1.add("男");
        strings1.add("女");

        strings2 = new ArrayList<String>();
        strings2.add("普通群众");
        strings2.add("党员");


        sexPopupWindow = new ListPopupWindow(this);
        sexPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings1));
        sexPopupWindow.setAnchorView(mBottom);
        sexPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        sexPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        sexPopupWindow.setModal(true);
        sexPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mBottom.setText(strings1.get(position));
                mBottom.setTextColor(Color.WHITE);
                Log.d("reg", "性别："+strings1.get(position));
                sex = strings1.get(position);
                sexPopupWindow.dismiss();
            }
        });
        mBottom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sexPopupWindow.show();
            }
        });

        politicalPopupWindow = new ListPopupWindow(this);
        politicalPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings2));
        politicalPopupWindow.setAnchorView(mPolitical);
        politicalPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        politicalPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        politicalPopupWindow.setModal(true);
        politicalPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mPolitical.setText(strings2.get(position));
                mPolitical.setTextColor(Color.WHITE);
                political = strings2.get(position);
                Log.d("reg", "性别："+strings2.get(position));
                politicalPopupWindow.dismiss();
            }
        });
        mPolitical.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                politicalPopupWindow.show();
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
                    img_identy.setImageBitmap(bitmap);

                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    Toast.makeText(RegisterActivity.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @OnTextChanged(R.id.user_name)
    void onNameTextChanged() {
        mName.setError(null);
    }

    @OnTextChanged(R.id.idcard)
    void onIdcardTextChanged() {
        mIdcard.setError(null);
    }

    @OnTextChanged(R.id.password)
    void onPasswordTextChanged() {
        mPassword.setError(null);
    }

    @OnTextChanged(R.id.password2)
    void onPassword2TextChanged() {
        mPassword2.setError(null);
    }

    @OnTextChanged(R.id.phone)
    void onPhoneTextChanged() {
        mPhone.setError(null);
    }

    @OnTextChanged(R.id.town)
    void onTownTextChanged() {
        town.setError(null);
    }

    @OnTextChanged(R.id.randcode)
    void onRandCodeChanged() {
        mRandCode.setError(null);
    }

    @OnClick(R.id.town)
    void ontown() {
        Log.d("reg", "点击村镇");
        startActivity(new Intent(this, TownListActivity.class));
    }

    @OnClick(R.id.randCode_image)
    void onImageClick() {
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

    @OnClick(R.id.register)
    void onCompleteClick() {
        boolean doExecute = true;
        String name = mName.getText().toString();
        String idcard = mIdcard.getText().toString();
        String phone = mPhone.getText().toString();
        String idcardAddress;
        if (mIdcardAddress.getText().toString() != null){
            idcardAddress = mIdcardAddress.getText().toString();
        }else {
            idcardAddress = "";
        }
        String fixPhone = mFixPhone.getText().toString();
        String email = mEmail.getText().toString();
        if (fixPhone.equals(null)){
            Log.d("reg", "wiuefyhioweyfw");
            fixPhone = "";
        }
        if (email.equals(null)){
            Log.d("reg", "wiueewetwretwfyhioweyfw");
            email = "";
        }
        if (sex == null){
            sex = "";
        }
        if (political == null){
            political = "";
        }
        String address = mAddress.getText().toString();
        String townss = town.getText().toString();
        String password = mPassword.getText().toString();
        String password2 = mPassword2.getText().toString();
        String code = mRandCode.getText().toString();
        Log.d("reg", "sex:"+sex+",political:"+political);
        if (StringUtil.isEmpty(name)) {
            doExecute = false;
            mName.setError("请输入姓名");
            mName.requestFocus();
        } else if (StringUtil.isEmpty(idcard)) {
            doExecute = false;
            mIdcard.setError("请输入身份证号码");
            mIdcard.requestFocus();
        } else if (!StringUtil.matchesIdCard(idcard)) {
            doExecute = false;
            mIdcard.setError("身份证号码格式不正确");
        } else if (StringUtil.isEmpty(phone)) {
            doExecute = false;
            mPhone.setError("请输入电话号码");
            mPhone.requestFocus();
        } else if (!StringUtil.isMobileNO(phone)) {
            doExecute = false;
            mPhone.setError("电话号码格式不正确");
        }else if (!fixPhone.equals("") && !StringUtil.matchesFixPhone(fixPhone)){
            doExecute = false;
            Log.d("reg", "fixPhone1:"+fixPhone);
            mFixPhone.setError("固定电话格式错误");
            mFixPhone.requestFocus();
        }else if (!email.equals("") && !StringUtil.matchesEmail(email)){
            doExecute = false;
            Log.d("reg", "email1:"+email);
            mEmail.setError("邮箱格式错误");
            mEmail.requestFocus();
        }else if (StringUtil.isEmpty(password)) {
            doExecute = false;
            mPassword.setError("请设置登录密码");
            mPassword.requestFocus();
        } else if (!StringUtil.matchesPassword(password)) {
            doExecute = false;
            mPassword.setError("登录密码为6-20位");
        } else if (StringUtil.isEmpty(password2)) {
            doExecute = false;
            mPassword2.setError("请输入确认密码");
            mPassword2.requestFocus();
        } else if (!password2.equals(password)) {
            doExecute = false;
            mPassword2.setError("两次密码输入不一致");
        } else if (StringUtil.isEmpty(townss) || null == towns) {
            doExecute = false;
            town.setError("请选择所属村镇");
            town.requestFocus();
        } else if (StringUtil.isEmpty(code)) {
            doExecute = false;
            mRandCode.setError("请输入验证码");
            mRandCode.requestFocus();
        }

        if (doExecute) {
            App.me().hideInput(getWindow());
            Log.i("info_Login", "知道了session：" + seesion);
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("idcard", idcard)
                    .add("phone", phone)
                    .add("userName", name)
                    .add("zName", towns.getZ_id())
                    .add("cName", towns.getC_id())
                    .add("password", password)
                    .add("address", address)
                    .add("sex", sex)
                    .add("political", political)
                    .add("idCardAddress", idcardAddress)
                    .add("tel", fixPhone)
                    .add("eMail", email)
                    .add("randcode", code)
                    .add("langCode", "zh-cn")
                    .build();
            Request request = new Request.Builder()
                    .addHeader("cookie", seesion)
                    .url(Constant.DOMAIN + "registerController.do?saveOnlineUser")
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
                    try {
                        if (response.isSuccessful()) {
                            Headers headers = response.headers();
                            Log.i("info_respons.headers", headers + "");
                            String r = response.body().string();
                            Log.e("reg", "r:" + r);
                            final ApiMsg apiMsg = JSON.parseObject(r, ApiMsg.class);
                            if (apiMsg.getSuccess().equals("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        App.me().toast(apiMsg.getMsg());
                                    }
                                });
                                onRegSuccess(mIdcard.getText().toString());
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        App.me().toast(apiMsg.getMsg());
                                    }
                                });
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        App.me().toast("系统错误");
                    }
                }
            });
        }
    }

    void onRegSuccess(String idcard) {
//        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//        Log.d("reg", "idcard:" + idcard);
//        intent.putExtra("idcard", idcard);
        EventBus.getDefault().post(idcard, "RegTwoActivity.onRegSuccess");
//        startActivity(intent);
        finish();
    }

    @Subscriber(tag = "TownListActivity.onTownSuccess")
    void onTownSuccess(Town town2) {
        Log.d("reg", "12121" + town2.getZ_name());
        towns = town2;
        town.setText(town2.getZ_name());
    }

    @OnClick(R.id.back)
    void onBack() {
        onBackPressed();
    }

}
