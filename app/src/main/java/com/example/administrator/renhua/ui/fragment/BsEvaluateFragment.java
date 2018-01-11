package com.example.administrator.renhua.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.AsyncHttpDialog;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.common.StringUtil;
import com.example.administrator.renhua.entity.Evaluate;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.adapter.BsEvaluateAdapter;
import com.example.administrator.renhua.ui.adapter.ZtEvaluateAdapter;
import com.example.administrator.renhua.ui.view.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class BsEvaluateFragment extends BaseFragment {

    @Bind(R.id.list)
    ListViewForScrollView lvEvaluate;
    @Bind(R.id.suggest)
    EditText mSuggest;
    @Bind(R.id.randcode)
    EditText mRandCode;
    @Bind(R.id.randCode_image)
    ImageView mRandomImg;
    @Bind(R.id.number)
    EditText mNumber;

    private BsEvaluateAdapter adapter;
    private List<Evaluate> list;
    private HashMap<String, Integer> ItemMaps;
    private String evaluateData;

    public String seesion;
    private OkHttpClient okHttpClient;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;
    private GetIndicators getIndicators;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bs_evaluate, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        okHttpClient = new OkHttpClient();
        getIndicators = new GetIndicators(getActivity());
        getIndicators.load("办事评价");
        ChangeImage();
    }

    private class GetIndicators extends AsyncHttpDialog {

        public GetIndicators(Context context) {
            super(context);
        }

        private void load( String type) {
            post(Constant.DOMAIN + "commentRecordController.do?getCommentItem",
                    "type", type);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            String data = apiMsg.getObj();
            evaluateData = data;
            Log.d("reg", "evaluateData:" + evaluateData);
            list = new ArrayList<Evaluate>();
            Evaluate evaluate;
            try {
                JSONArray res = new JSONArray(evaluateData);
                for (int i = 0; i < res.length(); i++) {
                    String o = res.getString(i);
                    Log.d("reg", "o:" + o);
                    evaluate = JSON.parseObject(o, Evaluate.class);
                    String type = evaluate.getType();
                    if ("办事评价".equals(type)) {
                        list.add(evaluate);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter = new BsEvaluateAdapter(list, getActivity());
            lvEvaluate.setAdapter(adapter);
            ItemMaps = adapter.getEvaItemMap();
        }
    }

    @OnClick(R.id.submit)
    void onSubmit() {
        ItemMaps = adapter.getEvaItemMap();
        boolean doExecute = true;
        String yzm = mRandCode.getText().toString();
        String number = mNumber.getText().toString();
        Iterator iter = ItemMaps.entrySet().iterator();
        LoginResponse login = App.me().login();
        String result = "";
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            result += "&" + key + "=" + val;
            i++;
        }
        Log.d("reg", "result:" + result);
        if (i < list.size()){
            doExecute = false;
            App.me().toast("请填完所有选项");
        }else if (StringUtil.isEmpty(number)) {
            doExecute = false;
            App.me().toast("请填写受理编号");
        }else if (StringUtil.isEmpty(yzm)) {
            doExecute = false;
            App.me().toast("请填写验证码");
        }

        if (doExecute){
            Log.i("info_Login", "知道了session：" + seesion);
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("bsYzm", mRandCode.getText().toString())
                    .add("bsContent2", mSuggest.getText().toString())
                    .add("busyDealnumber", number)
                    .add("userid", login.id)
                    .add("type", "办事评价")
                    .add("langCode", "zh-cn")
                    .build();
            Log.d("reg", "body:"+body.toString());
            Request request = new Request.Builder()
                    .addHeader("cookie", seesion)
                    .url(Constant.DOMAIN + "commentRecordController.do?phoneSaveCommentRecord" + result)
                    .post(body)
                    .build();
            Log.d("reg", "request:"+request.toString());
            Log.d("reg", body.encodedValue(2));
            Call call2 = okHttpClient.newCall(request);
            call2.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("info_call2fail", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Headers headers = response.headers();
                        Log.i("info_response.headers", headers + "");
                        String r = response.body().string();
                        Log.e("reg", "r:" + r);
                        final ApiMsg apiMsg = JSON.parseObject(r, ApiMsg.class);
                        if (apiMsg.getSuccess().equals("true")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    App.me().toast(apiMsg.getMsg());
                                    getActivity().finish();
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    App.me().toast(apiMsg.getMsg());
                                }
                            });
                        }
                    }
                }
            });
        }

    }

    @OnClick(R.id.randCode_image)
    void onRndomImg() {
        ChangeImage();
    }

    private void ChangeImage() {
        Request request = new Request.Builder()
                .url(Constant.url_randCodeImage + "random=" + System.currentTimeMillis())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_callFailure", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image = response.body().bytes();

                //通过handler更新UI
                Message message = handler.obtainMessage();
                message.obj = byte_image;
                message.what = SUCCESS;
                Log.i("info_handler", "handler");
                handler.sendMessage(message);

                //session
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);

                seesion = session.substring(0, session.indexOf(";"));
                Log.i("info_s", "session is  :" + seesion);

            }
        });
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //加载网络成功进行UI的更新,处理得到的图片资源
                case SUCCESS:
                    //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    mRandomImg.setImageBitmap(bitmap);

                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    Toast.makeText(getActivity(), "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
