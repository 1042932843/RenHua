/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

// 自定义扩展网页视图控件 支持html5视频播放 支持外部链接跳转
public class CustomWebView extends WebView {

    private View customView;
    private WebChromeClient.CustomViewCallback customCallback;

    public CustomWebView(Context context) {
        super(context);
        setup();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    // 初始化设置,偏好
    private void setup() {
        WebSettings settings = getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT < 18) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        setWebViewClient(new CustomWebViewClient());
        setWebChromeClient(new CustomWebChromeClient());
    }

    public void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (customCallback != null) {
            customCallback.onCustomViewHidden();
            customCallback = null;
        } else {
            // 设置全屏
            Activity activity = (Activity) getContext();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // 视频置顶
            view.setBackgroundColor(Color.BLACK);
            window.addContentView(view, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));

            customView = view;
            customCallback = callback;
        }
    }

    private long last_time = 0L;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                long current_time = System.currentTimeMillis();
//                long d_time = current_time - last_time;
////                System.out.println(d_time);
//                if (d_time < 300) {
//                    last_time = current_time;
//                    return true;
//                } else {
//                    last_time = current_time;
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    public void hideCustomView() {
        if (customView != null) {
            // 取消全屏
            Activity activity = (Activity) getContext();
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Window window = activity.getWindow();
            WindowManager.LayoutParams attrs = window.getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(attrs);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            // 恢复布局
            ViewParent parent = customView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(customView);
            }
            customView = null;
        }

        if (customCallback != null) {
            customCallback.onCustomViewHidden();
            customCallback = null;
        }
    }

    public boolean isCustomViewShowing() {
        return customCallback != null;
    }

    // 允许点击外链跳转浏览器
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String temp = url.toLowerCase();
            if (!temp.startsWith("http://") && !temp.startsWith("https://")) {
                url = "http://" + url;
            }
//            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            view.loadUrl(url);
            return true;
        }
    }

    // 允许全屏播放视频
    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            showCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }
    }

}
