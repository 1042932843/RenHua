package com.example.administrator.renhua.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.example.administrator.renhua.App;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class AsyncHttpDialog extends AsyncHttpResponseHandler implements DialogInterface.OnCancelListener, ResponseHandlerInterface{

    protected ProgressDialog dialog;
    protected Handler dialogHandler;
    protected long dialogShowTime;
    protected RequestHandle requestHandle;
    private final static AsyncHttpClient ASYNC_HTTP_CLIENT = new AsyncHttpClient();

    public final RequestHandle getRequestHandle(){
        return requestHandle;
    }

    public final RequestHandle post(String url, Object... paramsKeyAndValue){

        if (!url.startsWith(Constant.DOMAIN) && !url.startsWith("http://")) {
            if (url.startsWith("/") || Constant.DOMAIN.endsWith("/")) {
                url = Constant.DOMAIN + url;
            } else {
                url = Constant.DOMAIN + "/" + url;
            }
        }

        RequestParams params = null;
        StringBuilder builder = null;
        if (paramsKeyAndValue != null && paramsKeyAndValue.length != 0) {
            if (paramsKeyAndValue.length % 2 == 0) {
                params = new RequestParams();
                builder = new StringBuilder();
                for (int i = 0; i < paramsKeyAndValue.length; i += 2) {
                    Object key = paramsKeyAndValue[i];
                    Object value = paramsKeyAndValue[i + 1];
                    if (key != null && value != null) {
                        params.put(key.toString(), value.toString());
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
        if (requestHandle != null) {
            requestHandle.cancel(true);
        }
        if (builder != null && builder.length() > 0) {
            LogUtil.d(getClass(), String.format("send：%s&%s", url, builder));

        } else {
            LogUtil.d(getClass(),  url);
        }
        return requestHandle = ASYNC_HTTP_CLIENT.post(url, params, this);
    }

    public AsyncHttpDialog() {
        dialog = null;
        dialogHandler = null;
    }

    protected AsyncHttpDialog(Context context){
        this(context, "加载中...");
    }

    protected AsyncHttpDialog(Context context, String message){
        if (context != null) {
            dialogHandler = new Handler();
            dialog = new ProgressDialog(context);
            dialog.setMessage(message);
//            dialog.setOnCancelListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showDialog();
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String result = new String(responseBody);
        onSuccess(result);
        Log.d("reg", "result.result" + result);
        onFinished();
    }

    public void onSuccess(String result) {
        LogUtil.d(getClass(), result);
        try {
            ApiMsg apiMsg = JSON.parseObject(result, ApiMsg.class);
            String state = apiMsg.getState();
            if (state != null && state.equals("1")) {
//                App.me().setUser(null);
            }
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
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }

    @Override
    public void onFinish() {
        super.onFinish();
        onFinished();
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onFinish();
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
