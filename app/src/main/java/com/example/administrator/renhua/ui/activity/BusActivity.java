package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.administrator.renhua.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class BusActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        ButterKnife.bind(this);
    }
}
