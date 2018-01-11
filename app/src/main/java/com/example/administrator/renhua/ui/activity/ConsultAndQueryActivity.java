package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.administrator.renhua.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class ConsultAndQueryActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_and_query);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.query)
    void onQuery(){
        startActivity(new Intent(this, QueryActivity.class));
    }

    @OnClick(R.id.consult)
    void onConsult(){
        startActivity(new Intent(this, ConsultActivity.class));
    }

//    @OnClick(R.id.complaint)
//    void onComplaint(){
//        startActivity(new Intent(this, EvaluateActivity.class));
//    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }
}
