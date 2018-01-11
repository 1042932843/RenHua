package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.administrator.renhua.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class ApplianceShopDetails extends BaseActivity {

    @Bind(R.id.enterprise)
    TextView mEnterprise;
    @Bind(R.id.responsible)
    TextView mResponsible;
    @Bind(R.id.tel)
    TextView mTel;
    @Bind(R.id.creditCode)
    TextView mCreditCode;
    @Bind(R.id.address)
    TextView mAddress;
    @Bind(R.id.scope)
    TextView mScope;

    String enterprise, responsible, tel, creditCode, address, scope;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_shop_details);
        ButterKnife.bind(this);
        enterprise = getIntent().getStringExtra("enterprise");
        responsible = getIntent().getStringExtra("responsible");
        tel = getIntent().getStringExtra("tel");
        creditCode = getIntent().getStringExtra("creditCode");
        address = getIntent().getStringExtra("address");
        scope = getIntent().getStringExtra("scope");

        mEnterprise.setText(enterprise);
        mResponsible.setText(responsible);
        mTel.setText(tel);
        mCreditCode.setText(creditCode);
        mAddress.setText(address);
        mScope.setText(scope);
    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }
}
