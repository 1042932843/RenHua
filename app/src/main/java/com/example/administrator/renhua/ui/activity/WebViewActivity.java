package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
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

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webView)
    AppWebView mWebView;
    @Bind(R.id.navbar_title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        String mark = getIntent().getStringExtra("mark");
        String url = "";
        //这段要加上，汽车购票才能显示完全
        mWebView.getSettings().setDomStorageEnabled(true);
        if ("poverty".equals(mark)){

            url = "targeted_poverty_alleviation.html";//http://192.168.217.1:8080/testH5/test.html
        }else if ("party".equals(mark)){
            url = "party_organization.html";
        }else if ("auto".equals(mark)){
            url = "Autonomy_organization.html";
        }else if ("economy".equals(mark)){
            url = "Economics_organization.html";
        }else if ("life".equals(mark)){
            url = "";
        }else if ("sen_fang".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028816858cc88a00158ccc2c155009b&title=2";
        }else if ("ting_shui".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028816858cc88a00158ccc36a54009e&title=6";
        }else if ("bing_chong_hai".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028816858cc88a00158ccc8a6e500b8&title=3";
        }else if ("rou_cai".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028816858cc88a00158ccc967f200c1&title=7";
        }else if ("xun_qing".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028816858cc88a00158ccc1fb3e0095&title=0";
        }else if ("di_zhi_zai_hai".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028816858cc88a00158ccc259b70098&title=4";
        }else if ("ren_cai_zhao_pin".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028978a5a93e5c5015aa2379e3d0136&title=8";
        }else if ("fa_lv_yuan_zhu".equals(mark)){
            url = "production_services_item.html?newsTypeId=4028978a5a93e5c5015aa237d2350139&title=9";
        }else if ("nong_ji_zhi_shi".equals(mark)){
            url = "agricultural_knowledge_promotion.html";
        }else if ("policy_farmer".equals(mark)){
            url = "benefit_farming_policy.html";
        }else if ("farmer_insurance".equals(mark)){
            url = "policy_based_agricultural_insurance.html";
        }else if ("wei_sheng_wei_ji".equals(mark)){
            url = "healthy.html";
        }else if("governmentService".equals(mark)){
            url = "index?service=p";
        }

//        mWebView.getSettings().setSupportMultipleWindows(true);

        if("governmentService".equals(mark)){
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.loadUrl("http://www.gdbs.gov.cn/sites/dsft/gdswsbsdtsgft/qxIndex.html?qxxzqhdm=440224");
        } else if ("hospital".equals(mark)) {
            mWebView.loadUrl("http://www.yihu.com/");
        } else if ("bus".equals(mark)) {
            mWebView.loadUrl("https://m.changtu.com/city/shaoguanshi/");
        } else if ("electric".equals(mark)) {
            mWebView.loadUrl("https://www.gd.csg.cn/");
        }else if("plant".equals(mark)){
            Log.d("Planturl",url);
            url = getIntent().getStringExtra("url");
            mWebView.loadUrl(url);
        }else{
            mWebView.loadUrl(Constant.DOMAIN2+url);
        }

//        mWebView.loadUrl(url);


        mWebView.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void openNewWindow(String url) {
                Intent intent = new Intent(WebViewActivity.this,DetailWebViewActivity.class);
                intent.putExtra("detailurl",url);
                startActivity(intent);
            }
        },"android");


        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

                    mWebView.loadUrl("javascript:AndroidOpen()");
//                try{
//                }catch (Exception e){
//                    e.printStackTrace();
//                }


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
