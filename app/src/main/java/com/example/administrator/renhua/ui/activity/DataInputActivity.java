package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.FromInfo;
import com.example.administrator.renhua.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class DataInputActivity extends BaseActivity {
    FromInfo frominfo;
    @Bind(R.id.ShenQingRen)
    EditText ShenQingRen;
    @Bind(R.id.LianXiRen)
    EditText LianXiRen;
    @Bind(R.id.ShouJi)
    EditText ShouJi;
    @Bind(R.id.ZhengJianLeiXing)
    EditText ZhengJianLeiXing;
    @Bind(R.id.ZhengJianHaoMa)
    EditText ZhengJianHaoMa;
    @Bind(R.id.DiZhi)
    EditText DiZhi;

    @OnClick(R.id.next)
    void next(){
        if(TextUtils.isEmpty(ShenQingRen.getText().toString())){
            ToastUtil.ShortToast("请填写申请人");
            return;
        }
        frominfo.setShenQingRen(ShenQingRen.getText().toString());
        if(TextUtils.isEmpty(LianXiRen.getText().toString())){
            ToastUtil.ShortToast("请填写联系人");
            return;
        }
        String lianxiren=LianXiRen.getText().toString();
        frominfo.setLianXiRen(lianxiren);
        if(TextUtils.isEmpty(ShouJi.getText().toString())){
            ToastUtil.ShortToast("请填写手机号");
            return;
        }
        frominfo.setShouJi(ShouJi.getText().toString());
        if(TextUtils.isEmpty(ZhengJianHaoMa.getText().toString())){
            ToastUtil.ShortToast("请填写身份证号码");
            return;
        }
        frominfo.setZhengJianHaoMa(ZhengJianHaoMa.getText().toString());
        if(TextUtils.isEmpty(DiZhi.getText().toString())){
            ToastUtil.ShortToast("请填写地址");
            return;
        }
        frominfo.setDiZhi(DiZhi.getText().toString());
       Intent it=new Intent(this,HandleActivity.class);
       it.putExtra("datainput",frominfo);
       startActivity(it);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datainput);
        ButterKnife.bind(this);
        frominfo=new FromInfo();
        frominfo.setXiangMuMingCheng(getIntent().getStringExtra("title"));
        frominfo.setShiJianBianMa(getIntent().getStringExtra("serviceCode"));
        frominfo.setItemCode(getIntent().getStringExtra("itemCode"));
        ZhengJianLeiXing.setEnabled(false);
        ZhengJianLeiXing.setText("身份证");
        frominfo.setZhengJianLeiXing("身份证");
    }

}
