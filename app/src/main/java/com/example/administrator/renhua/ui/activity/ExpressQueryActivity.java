package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.MyOkHttp;
import com.example.administrator.renhua.common.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class ExpressQueryActivity extends BaseActivity {

    @Bind(R.id.express)
    Button mExpress;
    @Bind(R.id.express_num)
    EditText mExpNum;

    ListPopupWindow expressPopupWindow;
    String express = "", name = "";
    private List<String> strings1;
    private Express expressApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_query);
        ButterKnife.bind(this);
        expressApi = new Express(this);
        strings1 = new ArrayList<String>();
        strings1.add("顺丰快递");
        strings1.add("百世快递");
        strings1.add("中通快递");
        strings1.add("申通快递");
        strings1.add("圆通快递");
        strings1.add("韵达快递");
        strings1.add("邮政平邮");
        strings1.add("EMS");
        strings1.add("天天快递");
        strings1.add("京东快递");
        strings1.add("金峰快递");
        strings1.add("国通快递");
        strings1.add("优速快递");
        strings1.add("德邦快递");
        strings1.add("快捷快递");
        strings1.add("亚马逊");
        strings1.add("宅急送");

        expressPopupWindow = new ListPopupWindow(this);
        expressPopupWindow.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, strings1));
        expressPopupWindow.setAnchorView(mExpress);
        expressPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
        expressPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        expressPopupWindow.setModal(true);
        expressPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mExpress.setText(strings1.get(position));
                mExpress.setTextColor(Color.parseColor("#666666"));
                Log.d("reg", "快递：" + strings1.get(position));
                name = strings1.get(position);
                if (name.equals("顺丰快递")){
                    express = "SF";
                }else if (name.equals("百世快递")){
                    express = "HTKY";
                }else if (name.equals("中通快递")){
                    express = "ZTO";
                } else if (name.equals("申通快递")){
                    express = "STO";
                } else if (name.equals("圆通快递")){
                    express = "YTO";
                }else if (name.equals("韵达快递")){
                    express = "YD";
                }else if (name.equals("邮政平邮")){
                    express = "YZPY";
                }else if (name.equals("EMS")){
                    express = "EMS";
                }else if (name.equals("天天快递")){
                    express = "HHTT";
                }else if (name.equals("京东快递")){
                    express = "JD";
                }else if (name.equals("金峰快递")){
                    express = "QFKD";
                }else if (name.equals("国通快递")){
                    express = "GTO";
                }else if (name.equals("优速快递")){
                    express = "UC";
                }else if (name.equals("德邦快递")){
                    express = "DBL";
                }else if (name.equals("快捷快递")){
                    express = "FAST";
                }else if (name.equals("亚马逊")){
                    express = "AMAZON";
                }else if (name.equals("宅急送")){
                    express = "ZJS";
                }
                expressPopupWindow.dismiss();
            }
        });
        mExpress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                expressPopupWindow.show();
            }
        });
    }

    @OnTextChanged(R.id.express_num)
    void onNumChange() {
        mExpNum.setError(null);
    }

    @OnTextChanged(R.id.express)
    void onExpChange() {
        mExpress.setError(null);
    }

    @OnClick(R.id.query)
    void onQuery() {
        App.me().hideInput(getWindow());
        String num = mExpNum.getText().toString();
        if (StringUtil.isEmpty(express)){
            App.me().toast("请选择快递类型");
            mExpress.setError("请选择快递类型");
        }else if (StringUtil.isEmpty(num)) {
            mExpNum.setError("请输入正确的运单编号");
            mExpNum.requestFocus();
        } else {
            expressApi.query(express, num);
        }
    }

    private class Express extends MyOkHttp {

        public Express(Context context) {
            super(context);
        }

        private void query(String express, String expressNum) {
            call("", "courierCourier.do?getOrderTraces",
                    "express", express, "expressNum", expressNum);
        }

        @Override
        public void onSuccess(final ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getMsg().equals("查询成功")){
                Intent intent = new Intent(ExpressQueryActivity.this, ExpressDetailActivity.class);
                intent.putExtra("detail", apiMsg.getObj());
                startActivity(intent);
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.me().toast("此单号无物流信息，请检查您的单号是否正确");
                    }
                });

            }

        }
    }

    @OnClick(R.id.back)
    void onback() {
        onBackPressed();
    }
}
