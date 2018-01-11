/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.view.AppWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 * todo办事指南 http://ggfw.xinxing.gov.cn/jsps/phone/bszn.jsp
 */
public class TrafficActivity extends BaseActivity {

    @Bind(R.id.webView)
    AppWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        ButterKnife.bind(this);
        mWebView.loadUrl("http://61.143.38.77:8899/baseUnitpay/webapp/weizhang/query_traffic.html?province=GD&city=GD_SHAOGUAN");
//        mWebView.loadUrl("file:///android_asset/about.html");
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebChromeClient(
                new WebChromeClient() {
                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                        App.me().toast(message);
                        result.confirm();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.back)
    void onBack(){
        if (mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            onBackPressed();
        }
    }
}
