package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.AsyncHttpDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class LifeServiceActivity extends BaseActivity {

    String title;
    LoadZhen loadZhen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_service);
        ButterKnife.bind(this);
        loadZhen = new LoadZhen(this);
        loadZhen.getZhen();
    }

    @OnClick(R.id.phone)
    void onQuery(){
        startActivity(new Intent(this, PhoneActivity.class));
    }

    @OnClick(R.id.hospital)
    void onHospital(){
//        App.me().toast("建设中");
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "hospital");
        startActivity(intent);
    }

    @OnClick(R.id.bus)
    void onBus(){
//        App.me().toast("建设中");
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "bus");
        startActivity(intent);
    }

    @OnClick(R.id.electric_charge)
    void onElectric(){
//        App.me().toast("建设中");
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "electric");
        startActivity(intent);
    }

    @OnClick(R.id.express)
    void onExpress(){
        startActivity(new Intent(this, ExpressQueryActivity.class));
    }

    @OnClick(R.id.health)
    void onHealth(){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "wei_sheng_wei_ji");
        startActivity(intent);
    }

    @OnClick(R.id.traffic)
    void onConsult(){
        startActivity(new Intent(this, TrafficActivity.class));
    }

    @OnClick(R.id.fa_lv_yuan_zhu)
    void FaLvYuanZhu(){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("mark", "fa_lv_yuan_zhu");
        startActivity(intent);
    }

    @OnClick(R.id.appliance_repair)
    void onAppliance(){
        Intent intent = new Intent(this, ApplianceRepairActivity.class);
        Log.d("reg", "title"+title);
        if (title == null){
            title = "['暂无信息']";
        }
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @OnClick(R.id.zhi_wu_shi_bie)
    public void PlantFind() {
        Intent intent = new Intent(this, PlantInformActivity.class);
        intent.putExtra("mark", "plant_inform");
        startActivity(intent);
    }


    private class LoadZhen extends AsyncHttpDialog {

        public LoadZhen(Context context) {
            super(context);
        }

        private void getZhen() {
            post("dyTbJdwxController.do?getExistzhen");
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            try {
                JSONObject attributes = new JSONObject(apiMsg.getAttributes());
                Log.d("reg", "list:" + attributes);
                title = attributes.getJSONArray("list").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
//    @OnClick(R.id.farming_insurance)
//    void onFarming(){
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("mark", "insurance");
//        startActivity(intent);
//    }

//    @OnClick(R.id.complaint)
//    void onComplaint(){
//        startActivity(new Intent(this, EvaluateActivity.class));
//    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }
}
