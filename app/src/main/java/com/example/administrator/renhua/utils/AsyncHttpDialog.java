package com.example.administrator.renhua.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.LogUtil;
import com.example.administrator.renhua.ui.activity.LoginActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;

import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class AsyncHttpDialog extends RequestCallBack<String> implements DialogInterface.OnCancelListener {

    protected ProgressDialog dialog;
    protected Handler dialogHandler;
    protected long dialogShowTime;
    protected HttpHandler httpHandler;
    private final static AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();
    private static final HttpUtils HTTP = new HttpUtils();

    public final HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public final HttpHandler post(HttpRequest.HttpMethod method, String url, Object... paramsKeyAndValue) {
        if (!url.startsWith(Constant.DOMAIN) && !url.startsWith("http://")) {
            if (url.startsWith("/")) {
                url = Constant.DOMAINn + url;
            } else {
                url = Constant.DOMAIN  + url;
            }
        }

        RequestParams params = null;
        StringBuilder builder = null;
        if (paramsKeyAndValue != null && paramsKeyAndValue.length != 0) {
            if (paramsKeyAndValue.length % 2 == 0) {
                params = new RequestParams();
                builder = new StringBuilder();
                params.addBodyParameter("orgCode", "");
                if(App.me().login()!=null&&!TextUtils.isEmpty(App.me().login().token)){
                    params.addBodyParameter("Access_token", App.me().login().token);
                }
                for (int i = 0; i < paramsKeyAndValue.length; i += 2) {
                    Object key = paramsKeyAndValue[i];
                    Object value = paramsKeyAndValue[i + 1];
                    if (key != null && value != null) {
                        params.addBodyParameter(key.toString(), value.toString());
                        if (builder.length() > 0) {
                            builder.append("&");
                        }
                        builder.append(key);
                        builder.append("=");
                        builder.append(value);
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (httpHandler != null) {
            httpHandler.cancel(true);
        }
        if (builder != null && builder.length() > 0) {
            if (url.contains("?")){
                LogUtil.d(getClass(), String.format("send：%s&%s", url, builder));
            }else {
                Log.d("reg", "result:" + String.format("send：%s?%s", url, builder));
            }
        } else {
            LogUtil.d(getClass(), "send：" + url);

        }
        return httpHandler = HTTP.send(method, url, params, this);
    }

    public AsyncHttpDialog() {
        dialog = null;
        dialogHandler = null;
    }

    protected AsyncHttpDialog(Context context) {
        this(context, "加载中...");
    }

    protected AsyncHttpDialog(Context context, String message) {
        if (context != null) {
            dialogHandler = new Handler();
            dialog = new ProgressDialog(context);
            dialog.setMessage(message);
            dialog.setOnCancelListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        showDialog();
    }

    @Override
    public void onSuccess(ResponseInfo<String> result) {
        if (result.result!=null){
            onSuccess(result.result);
            onFinished();
        }
    }

    @Override
    public final void onFailure(HttpException e, String s) {
        onFailure((Exception) e, s);
        onFinished();
    }

//    @Override
//    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//        String result = new String(responseBody);
//        onSuccess(result);
//        onFinished();
//    }

    public void onSuccess(String result) {
        Log.d("reg", "result:" + result);

            LogUtil.d(getClass(), result);
            try {
                ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
//            String state = apiMsg.getState();
//            if (state != null && state.equals("1")) {
//                App.me().setUser(null);
//            }
                onSuccess(apiMsg);

            } catch (Exception e) {
                onFailure(e, e.getMessage());
            }
        }


    public void onFailure(Exception e, String s) {
        LogUtil.e(getClass(), s, e);

        if (e instanceof HttpException) {
            App.me().toast("网络不可用");
        } else if (e instanceof SocketTimeoutException) {
            App.me().toast("网络请求超时");
        } else if (e instanceof JSONException) {
            App.me().toast("数据解析错误");
        } else if (e instanceof NullPointerException) {
            App.me().toast("程序错误");
        } else if (s != null) {
            App.me().toast("未知错误：" + s);
        } else {
            App.me().toast("未知错误");
        }
    }

    public void onSuccess(ApiMsg apiMsg) {

    }


    @Override
    public void onCancelled() {
        onFinished();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        if (httpHandler != null) {
            httpHandler.cancel();
        }
    }

    public void showDialog() {
        if (dialog != null) {
            try {
                dialogShowTime = System.currentTimeMillis();
                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            } catch (Exception e) {
                LogUtil.e(AsyncHttpDialog.class, e.getMessage(), e);
            }
        }
    }

    public void onFinished() {
        if (dialogHandler != null && dialog != null && dialog.isShowing()) {
            dialogHandler.removeCallbacks(null);
            long delayMillis = System.currentTimeMillis() - dialogShowTime;
            if (delayMillis < 1000) {
                delayMillis = 1000;
            } else {
                delayMillis = 0;
            }
            dialogHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideDialog();
                }
            }, delayMillis);
        }
    }

    public void hideDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                LogUtil.e(AsyncHttpDialog.class, e.getMessage(), e);
            }
        }
    }

}

