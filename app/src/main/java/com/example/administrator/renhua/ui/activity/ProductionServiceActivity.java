package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.administrator.renhua.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class ProductionServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_service);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sen_fang)
    void SenFang() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "sen_fang");
        startActivity(intent);
    }

    @OnClick(R.id.ting_shui_ting_dian)
    void TingShui() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "ting_shui");
        startActivity(intent);
    }

    @OnClick(R.id.bing_chong_hai)
    void BingChong() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "bing_chong_hai");
        startActivity(intent);
    }

    @OnClick(R.id.rou_cai)
    void RouCai() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "rou_cai");
        startActivity(intent);
    }

    @OnClick(R.id.xun_qing)
    void XunQing() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "xun_qing");
        startActivity(intent);
    }

    @OnClick(R.id.di_zhi_zai_hai)
    void DiZhi() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "di_zhi_zai_hai");
        startActivity(intent);
    }

    @OnClick(R.id.nong_ji_zhi_shi)
    void NongJi() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "nong_ji_zhi_shi");
        startActivity(intent);
    }

    @OnClick(R.id.policy_farmer)
    void PolicyFarmer() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "policy_farmer");
        startActivity(intent);
    }

    @OnClick(R.id.farmer_insurance)
    void FarmerInsurance() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "farmer_insurance");
        startActivity(intent);
    }

    @OnClick(R.id.ren_cai_zhao_pin)
    void ZhaoPin() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "ren_cai_zhao_pin");
        startActivity(intent);
    }

    @OnClick(R.id.back)
    void onback() {
        onBackPressed();
    }


}
