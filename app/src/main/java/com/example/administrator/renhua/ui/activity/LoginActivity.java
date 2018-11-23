package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.view.AppWebView;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.webView)
    AppWebView mWebView;
    @Bind(R.id.navbar_title)
    TextView mTitle;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearCache(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        String url = "http://wsbs.sgrh.gov.cn/renhua/app/uc/appLogin?appType=app&goto=unitUserController.do?callback";
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl(url);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("max", "将加载:" + url);
                super.onPageStarted(view, url, favicon);
                if(url.contains("userToken=")){
                    LoginResponse login=new LoginResponse();
                    login.token= TTest(url);
                    if(!TextUtils.isEmpty(login.token)){
                        App.me().login(login);
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

            }



            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("max", "加载完成:" + url);
                if(url.contains("userToken=")){
                    LoginResponse login=new LoginResponse();
                    login.token= TTest(url);
                    if(!TextUtils.isEmpty(login.token)){
                        App.me().login(login);
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }

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

    public String TTest(String str){
        String userToken = "";

        String[] dataList = str.split("&")[1].split("=");

        for(int i=0;i<dataList.length;i++){
            if(dataList[i].equals("userToken")){
                userToken = dataList[i+1];
                break;
            }
        }

        System.out.println(userToken);
        return  userToken;
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
