package com.example.administrator.renhua.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.renhua.R;


/**
 * Created by K on 2016/3/21.
 */
public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_start, null);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
        setContentView(view);
    }
}
