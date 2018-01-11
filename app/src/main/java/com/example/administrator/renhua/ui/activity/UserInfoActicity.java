package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.entity.UserInfoResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cz.msebera.android.httpclient.Header;

/**
 * Created by K on 2016/4/7.
 */
public class UserInfoActicity extends BaseActivity {

    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.idcard)
    TextView mIdcard;
    @Bind(R.id.cunzen)
    TextView mCunzen;
    @Bind(R.id.phone)
    TextView mPhone;
    @Bind(R.id.sex)
    Button mSex;
    @Bind(R.id.address)
    EditText mAddress;
    @Bind(R.id.idcard_address)
    EditText mIdcardAddress;
    @Bind(R.id.fix_phone)
    EditText mFixPhone;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.political)
    Button mPolitical;
    @Bind(R.id.real_name)
    EditText mRealName;

    ListPopupWindow politicalPopupWindow, sexPopupWindow;
    private List<String> strings, strings1;
    String sex = "", political = "";
    LoginResponse login;
    int action; // 1:获取 2:修改

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        login = App.me().login();
        if (login != null) {
            getUserInfo(login.id);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mRealName.clearFocus();
//        mIdcardAddress.clearFocus();
//        mFixPhone.clearFocus();
//        mEmail.clearFocus();
//        mAddress.clearFocus();
//        App.me().hideInput(getWindow());
        strings1 = new ArrayList<String>();
        strings1.add("男");
        strings1.add("女");

        sexPopupWindow = new ListPopupWindow(this);
        sexPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings1));
        sexPopupWindow.setAnchorView(mSex);
        sexPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        sexPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        sexPopupWindow.setModal(true);
        sexPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSex.setText(strings1.get(position));
                mSex.setTextColor(Color.parseColor("#666666"));
                Log.d("reg", "性别："+strings1.get(position));
                sex = strings1.get(position);
                sexPopupWindow.dismiss();
            }
        });
        mSex.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sexPopupWindow.show();
            }
        });

        politicalPopupWindow = new ListPopupWindow(this);
        strings = new ArrayList<String>();
        strings.add("普通群众");
        strings.add("党员");
        politicalPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings));
        politicalPopupWindow.setAnchorView(mPolitical);
        politicalPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        politicalPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        politicalPopupWindow.setModal(true);
        politicalPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPolitical.setText(strings.get(position));
                mPolitical.setTextColor(Color.parseColor("#666666"));
                political = strings.get(position);
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

    @OnTextChanged(R.id.address)
    void onAddressTextChanged() {
        mAddress.setError(null);
    }
    @OnTextChanged(R.id.real_name)
    void onRealName() {
        mRealName.setError(null);
    }

    @OnClick(R.id.save)
    void onSaveClick() {
        boolean doExecute = true;
        String name = mName.getText().toString();
        String address = mAddress.getText().toString();
        String phone = mPhone.getText().toString();
        String tel = mFixPhone.getText().toString();
        String idCardAddress = mIdcardAddress.getText().toString();
        String eMail = mEmail.getText().toString();
        String sex = mSex.getText().toString();
        String political = mPolitical.getText().toString();
        String realName = mRealName.getText().toString();
        if (StringUtil.isEmpty(realName)){
            doExecute = false;
            mRealName.setError("请输入姓名");
            mRealName.requestFocus();
        }
        if (!tel.equals("") && !StringUtil.matchesFixPhone(tel)){
            Log.d("reg", "tel:"+tel);
            doExecute = false;
            mFixPhone.setError("固定电话格式错误");
            mFixPhone.requestFocus();
        }else if (!eMail.equals("") && !StringUtil.matchesEmail(eMail)){
            doExecute = false;
            mEmail.setError("邮箱格式错误");
            mEmail.requestFocus();
        }else if (!address.equals("")&&StringUtil.isEmpty(address)) {
            doExecute = false;
            mAddress.setError("请输入联系地址");
            mAddress.requestFocus();
        }

        if (doExecute) {
            action = 2;
            App.me().hideInput(getWindow());
            updateInfo(login.username, login.idcard, login.phone, realName, address, sex, tel, idCardAddress, eMail, political);
        }
    }

    private void getUserInfo(String id) {
        String url = Constant.DOMAIN + "registerController.do?getUserInfo";
        RequestParams params = new RequestParams();
        params.put("userid", id);
        Log.d("reg", "id:" + id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.toString();
                Log.d("reg", "result" + result);
                final ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
                if (apiMsg.getSuccess().equals("true")) {
                    initUserInfo(JSON.parseObject(apiMsg.getObj(), UserInfoResponse.class));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.me().toast(apiMsg.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("reg", "getUserInfo.responseString:" + responseString);
            }
        });
    }

    private void updateInfo(String userName, String idcard, String phone, String realName, String address, String sex, String tel, String idCardAddress, String eMail, String political) {
        final String url = Constant.DOMAIN + "phoneUserOnlineController.do?update";
        final RequestParams params = new RequestParams();
        params.put("idcard", idcard);
        params.put("userName", userName);
        params.put("phone", phone);
        params.put("realName", realName);
        params.put("address", address);
        params.put("sex", sex);
        params.put("tel", tel);
        params.put("idCardAddress", idCardAddress);
        params.put("eMail", eMail);
        params.put("political", political);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String result = response.toString();
                    Log.d("reg", "url:" + url);
                    Log.d("reg", "params:" + params.toString());
                    Log.d("reg", "result:" + result);
                    final ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.me().toast(apiMsg.getMessage());
                        }
                    });
                    if (apiMsg.getState().equals("0")) {
                        EventBus.getDefault().post(mName.getText().toString(), "UserInfoActivity.ChangeUserName");
                        Log.d("RegOne", "updateInfo.UserInfoActivity:" + "ChangeUserName");
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("reg", "updateInfo.responseString:" + responseString);
            }
        });
    }

    void initUserInfo(UserInfoResponse userInfo) {
        mName.setText(userInfo.user_name);
        mIdcard.setText(userInfo.idcard);
        mPhone.setText(userInfo.phone);
        mRealName.setText(userInfo.real_name);
        mIdcardAddress.setText(userInfo.idCardAddress);
        mFixPhone.setText(userInfo.tel);
        mEmail.setText(userInfo.eMail);
        mPolitical.setText(userInfo.political);
        mSex.setText(userInfo.sex);
        mAddress.setText(userInfo.address);
        String str = userInfo.zname + userInfo.cname;
        if (str != null && str.indexOf("null") == -1) {
            mCunzen.setText(str);
        } else {
            mCunzen.setText(" ");
        }
    }

    @OnClick(R.id.back)
    void onBack() {
        onBackPressed();
    }
//    @Override
//    public void onSuccess(@Nullable JSONObject data, @NonNull SoapResponse response) throws Exception {
//        super.onSuccess(data, response);
//        if (response.state == 0) {
//            switch (action) {
//                case 1:
//                    initUserInfo(JSON.parseObject(data.getJSONObject("result").toString(), UserInfoResponse.class));
//                    break;
//                case 2:
//                    EventBus.getDefault().post(mName.getText().toString(), "UserInfoActivity.ChangeUserName");
//                    Log.d("RegOne", "UserInfoActivity:" + "ChangeUserName");
//                    finish();
//                    break;
//            }
//        }
//    }

}
