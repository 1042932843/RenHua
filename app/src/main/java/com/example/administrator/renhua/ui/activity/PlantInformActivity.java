package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.view.AppWebView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlantInformActivity extends BaseActivity {

    @Bind(R.id.webView)
    AppWebView mWebView;
    @Bind(R.id.navbar_title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_inform);
        ButterKnife.bind(this);
        String mark = getIntent().getStringExtra("mark");
        String url = "http://www.rhggfw.com/lyweixin/view/home.html";
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl(url);


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
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.back)
    void onBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            onBackPressed();
        }
    }

    @OnClick(R.id.fab)
    public void fabClick() {
        Intent intent = new Intent(PlantInformActivity.this, QRCodeActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(PlantInformActivity.this,WebViewActivity.class);
//                    intent.putExtra("mark","plant");
//                    intent.putExtra("url",result);
//                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(PlantInformActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
