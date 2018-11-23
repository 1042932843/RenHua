package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.example.administrator.renhua.R.id.serviceObjectType;
import static com.example.administrator.renhua.R.id.sex;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class FillInBasicInfo extends BaseActivity {


    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.applyUserName)
    EditText mApplyUserName;
    @Bind(R.id.certNo)
    EditText mCertNo;
    @Bind(R.id.mobile)
    EditText mMobile;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.postcode)
    EditText mPostcode;
    @Bind(R.id.address)
    EditText mAddress;
    @Bind(R.id.corpName)
    EditText mCorpName;
    @Bind(R.id.busiLicense)
    EditText mBusiLicense;
    @Bind(R.id.ltaxNo)
    EditText mItaxNo;
    @Bind(R.id.gtaxNo)
    EditText mGtaxNo;
    @Bind(serviceObjectType)
    TextView mObjectType;
    @Bind(sex)
    TextView mSex;
    @Bind(R.id.certType)
    TextView mCertType;

    private String serviceCode, name, orgName, orgCode, authorityLevel, serviceItemType, certType, attachmentCode;
    //    private int legalDayType, legaldays, promiseDayIype, promisedays;
    private String legalDayType, legaldays, promiseDayIype, promisedays;
    private InfoApi infoApi;
    private LoginResponse login;
    private String mobile = "", email = "", postcode = "", address = "", corpName = "", busilicense = "", itaxno = "", gtaxno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_basic_info);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        serviceCode = getIntent().getStringExtra("serviceCode");
        name = getIntent().getStringExtra("title");
        orgName = getIntent().getStringExtra("orgName");
        orgCode = getIntent().getStringExtra("orgCode");
        authorityLevel = getIntent().getStringExtra("authorityLevel");
        promisedays = getIntent().getStringExtra("promisedays");
        legaldays = getIntent().getStringExtra("legaldays");
        promiseDayIype = getIntent().getStringExtra("promiseDayIype");
        legalDayType = getIntent().getStringExtra("legalDayType");
        serviceItemType = getIntent().getStringExtra("serviceItemType");
        attachmentCode = getIntent().getStringExtra("attachmentCode");
        mName.setText(name);
        login = App.me().login();
        infoApi = new InfoApi(this);
        mObjectType.setText("个人");
        mObjectType.setTag("1");
        mCertType.setText("身份证");
        mCertType.setTag("10");
        mSex.setText("男");
        mSex.setTag(1);
    }

    @OnClick(R.id.submit_info)
    public void onBtnClicked() {
        String applyUserName = mApplyUserName.getText().toString();
        String certNo = mCertNo.getText().toString();
        mobile = mMobile.getText().toString();
        email = mEmail.getText().toString();
        postcode = mPostcode.getText().toString();
        address = mAddress.getText().toString();
        corpName = mCorpName.getText().toString();
        busilicense = mBusiLicense.getText().toString();
        itaxno = mItaxNo.getText().toString();
        gtaxno = mGtaxNo.getText().toString();

        boolean doExecute = true;
        if (StringUtil.isEmpty(applyUserName)) {
            doExecute = false;
            App.me().toast("请输入申请单位或申请人姓名");
            mApplyUserName.requestFocus();
        } else if (StringUtil.isEmpty(certNo)) {
            doExecute = false;
            App.me().toast("请输入申请的证件号码");
            mCertNo.requestFocus();
        } else if (!StringUtil.matchesIdCard(certNo)) {
            doExecute = false;
            App.me().toast("证件号码格式错误");
            mCertNo.requestFocus();
        } else if (!StringUtil.isEmpty(mobile)) {
            if (!StringUtil.matchesPhone(mobile)) {
                if (!StringUtil.matchesFixPhone(mobile)) {
                    doExecute = false;
                    App.me().toast("电话号码格式错误");
                    mMobile.requestFocus();
                }
            }
        } else if (!StringUtil.isEmpty(email) && !StringUtil.matchesEmail(email)) {
            doExecute = false;
            App.me().toast("邮箱号码格式错误");
            mEmail.requestFocus();
        }
        if (doExecute) {

            infoApi.submitInfo(applyUserName, serviceCode, name, certNo, mobile, email, postcode, address, corpName, busilicense, itaxno, gtaxno);
        }
    }

    @OnClick({serviceObjectType, R.id.certType, sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case serviceObjectType:
                Log.d("reg", "click->serviceObjectType");
                final String[] items = {"个人", "企业", "其他"};
                final String[] values = {"1", "2", "9"};
                sheetKit().items(items).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < values.length) {
                            mObjectType.setTag(values[which]);
                            mObjectType.setText(items[which]);
                        }
                    }
                }).show();

                break;
            case R.id.certType:
                Log.d("reg", "click->certType");
                final String[] values1 = {"10"};
                final String[] items1 = {"身份证"};
                sheetKit().items(items1).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < values1.length) {
                            mCertType.setTag(values1[which]);
                            mCertType.setText(items1[which]);
                        }
                    }
                }).show();
                break;
            case sex:
                final int[] values2 = {1, 0};
                final String[] items2 = {"男", "女"};
                sheetKit().items(items2).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which < values2.length) {
                            mSex.setTag(values2[which]);
                            mSex.setText(items2[which]);
                        }
                    }
                }).show();
                break;
        }
    }

    private class InfoApi extends AsyncHttpDialog {
        public InfoApi(Context context) {
            super(context);
        }

        private void submitInfo(String applyUserName, String serviceCode, String name, String certNo, String mobile, String email, String postcode,
                                String address, String corpName, String busiLicense, String ltaxNo, String gtaxNo) {
            post(HttpRequest.HttpMethod.POST, "itemApplyController.do?saveItemApply", "serviceInstanceId", "",
                    "userId", login.id, "name", name, "serviceCode", serviceCode, "orgName", orgName,
                    "orgCode", orgCode, "authorityLevel", authorityLevel, "promisedays", promisedays,
                    "legaldays", legaldays, "promiseDayIype", promiseDayIype, "legalDayType", legalDayType,
                    "serviceItemType", serviceItemType, "submitType", "1", "needEms", "0", "sbType", "J",
                    "applyUserName", applyUserName, "serviceObjectType", mObjectType.getTag(),
                    "certType", mCertType.getTag(), "certNo", certNo, "sex", mSex.getTag(), "mobile", mobile, "email", email,
                    "postcode", postcode, "address", address, "corpName", corpName, "busiLicense", busiLicense, "ltaxNo", ltaxNo,
                    "gtaxNo", gtaxNo);

//            post(HttpRequest.HttpMethod.POST, "itemApplyController.do?saveItemApply",
//                    "serviceInstanceId", "", "userId", login.id, "sbType", "J", "sex", mSex.getTag(), "authorityLevel", authorityLevel, "promisedays", promisedays,
//                    "legalDayType", legalDayType, "serviceCode", serviceCode, "name", name, "orgName", orgName,
//                    "certNo", certNo, "serviceItemType", serviceItemType, "needEms", "0", "applyUserName", applyUserName,
//                    "submitType", "1", "corpName", corpName,
//                    "email", email, "ltaxNo", ltaxNo, "certType", mCertType.getTag(), "mobile", mobile, "busiLicense", busiLicense, "orgCode", orgCode,
//                    "promiseDayIype", promiseDayIype, "gtaxNo", gtaxNo, "legaldays", legaldays, "postcode", postcode,
//                    "address", address, "serviceObjectType", mObjectType.getTag());
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getSuccess().equals("true")) {
                String serviceInstanceId = "";
                try {
                    JSONObject object = new JSONObject(apiMsg.getObj());
                    serviceInstanceId = object.getString("serviceInstanceId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(FillInBasicInfo.this, UploadFilesActivity.class);
                intent.putExtra("serviceCode", serviceCode);
                intent.putExtra("serviceInstanceId", serviceInstanceId);
                intent.putExtra("orgCode", orgCode);
                intent.putExtra("attachmentCode", attachmentCode);
                intent.putExtra("name", name);
                intent.putExtra("applyUserName", mApplyUserName.getText().toString());
                startActivity(intent);
            } else {
                App.me().toast(apiMsg.getMsg());
            }
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }
}
