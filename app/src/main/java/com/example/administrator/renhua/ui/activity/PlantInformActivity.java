package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.view.AppWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zxing.decoding.Intents;
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
        //webview默认关闭文件下载，要添加这个Listiner才行
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
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
        Intent intent = new Intent(PlantInformActivity.this, MipcaActivityCapture.class);
        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.SCAN_FORMATS, "QR_CODE");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){
            Bundle bundle =  data.getExtras();
            String result = bundle.getString("result");
            Toast.makeText(PlantInformActivity.this,result,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlantInformActivity.this,WebViewActivity.class);
            intent.putExtra("mark","plant");
            intent.putExtra("url",result);
            startActivity(intent);
        }

    }
}
