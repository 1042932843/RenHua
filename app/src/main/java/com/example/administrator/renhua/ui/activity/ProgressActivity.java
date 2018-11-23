package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class ProgressActivity extends BaseActivity {

    @Bind(R.id.name)
    EditText Sxmc;
    @Bind(R.id.number)
    EditText number;

    @Bind(R.id.randcode)
    EditText mRandCode;

    @Bind(R.id.randCode_image)
    ImageView mRandImg;
    String SBLSH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);
        SBLSH=getIntent().getStringExtra("SBLSH");
        if(!TextUtils.isEmpty(SBLSH)){
            number.setText(SBLSH);
        }
        ChangeImage();
    }

    @OnClick(R.id.randCode_image)
    void onRndomImg() {
        ChangeImage();
    }

    @OnClick(R.id.search_progress)
    void search(){
        /*if(TextUtils.isEmpty(Sxmc.getText().toString())){
            Toast.makeText(this,"请输入事项关键字！",Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(TextUtils.isEmpty(number.getText().toString())){
            Toast.makeText(this,"请输入流水号！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(mRandCode.getText().toString())){
            Toast.makeText(this,"请输入验证码！",Toast.LENGTH_SHORT).show();
            return;
        }
        Search(Sxmc.getText().toString(),number.getText().toString(),mRandCode.getText().toString());
    }

    private void Search(String s, String s1,String randCode) {
        Intent it=new Intent(this,ProgressListActivity.class);
        it.putExtra("Sxmc",s);
        it.putExtra("Sblsh",s1);
        it.putExtra("randCode",randCode);
        it.putExtra("seesion",seesion);
        startActivity(it);
    }
    public String seesion;
    private void ChangeImage() {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.DOMAIN + "webpage/views/commons/checkCode.jsp?random=" + System.currentTimeMillis())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_callFailure", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //session
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);

                seesion = session.substring(0, session.indexOf(";"));
                Log.i("info_s", "session is  :" + seesion);
                byte[] byte_image = response.body().bytes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(byte_image, 0, byte_image.length);
                        //通过imageview，设置图片
                        mRandImg.setImageBitmap(bitmap);
                    }
                });

                //通过handler更新UI



            }
        });
    }



}
