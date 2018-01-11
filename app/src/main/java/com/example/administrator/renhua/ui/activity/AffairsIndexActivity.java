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

public class AffairsIndexActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.person_affairs)
    void onPersonClick(){
        startActivity(new Intent(this, AffairsQueryActivity.class));
    }

    @OnClick(R.id.company_affairs)
    void onCompanyClick(){
        startActivity(new Intent(this, AffairsQueryActivity.class));
    }

    @OnClick(R.id.back)
    void onBck(){
        onBackPressed();
    }
}
