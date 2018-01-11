package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.ui.view.AppWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailWebViewActivity extends BaseActivity {


    @Bind(R.id.navbar_title)
    TextView mTitle;
    @Bind(R.id.webView)
    AppWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web_view);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("detailurl");
        mWebView.loadUrl(Constant.DOMAIN2+url);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("reg", "onPageFinished");
                if (view.getTitle().equals("法律援助")){
                    mTitle.setText("法律咨询");
                }else {
                    mTitle.setText(view.getTitle());
                }
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTitle.setText(title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                App.me().toast(message);
                result.confirm();
                return true;
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);    //此webview可以是一般新创建的
                resultMsg.sendToTarget();
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
