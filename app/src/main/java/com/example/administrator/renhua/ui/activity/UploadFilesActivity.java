package com.example.administrator.renhua.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.biz.FileUtil;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.UploadFiles;
import com.example.administrator.renhua.entity.model.BaseModel;
import com.example.administrator.renhua.ui.adapter.Holder;
import com.example.administrator.renhua.ui.adapter.UploadFilesAdapter;
import com.example.administrator.renhua.ui.listener.OnScrollLastItemListener;
import com.example.administrator.renhua.ui.view.PullToRefreshLayout;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.administrator.renhua.R.id.pull;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class UploadFilesActivity extends BaseActivity implements PtrHandler, OnScrollLastItemListener, AdapterView.OnItemClickListener, UploadFilesAdapter.MyCallBack {

    @Bind(R.id.list)
    ListView mList;
    @Bind(pull)
    PullToRefreshLayout mPull;
    private GetFilesApi getFilesApi;
    private String serviceCode, serviceInstanceId, orgCode, attachmentCode, itemAttachmentId, name, callTime, applyUserName;
    private UploadFilesAdapter adapter;
    private ArrayList<UploadFiles> items = new ArrayList<UploadFiles>();
    private Handler handler = new Handler();
    private static String TAG = "UploadFilesActivity";
    private String fileName;
    private int action, position;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        serviceCode = getIntent().getStringExtra("serviceCode");
        serviceInstanceId = getIntent().getStringExtra("serviceInstanceId");
        orgCode = getIntent().getStringExtra("orgCode");
        attachmentCode = getIntent().getStringExtra("attachmentCode");
        name = getIntent().getStringExtra("name");
        applyUserName= getIntent().getStringExtra("applyUserName");
        initViews();
        getFilesApi = new GetFilesApi(this);
        action = 1;
        getFilesApi.getFilesList();
        adapter = new UploadFilesAdapter(UploadFilesActivity.this, this);
        mList.addHeaderView(View.inflate(UploadFilesActivity.this, R.layout.layout_upload_files_top, null));
        mList.addFooterView(new BottomHolder().onBind(null, -1).getView());
        mList.setAdapter(adapter);

    }

    @Override
    public void click(View v) {
        position = (Integer) v.getTag();
//        App.me().toast("listview的内部的按钮被点击了！，位置是-->" + (Integer) v.getTag() + ",内容是-->"
//                + items.get((Integer) v.getTag()));
    }

    private class GetFilesApi extends AsyncHttpDialog {

        public GetFilesApi(Context context) {
            super(context);
        }

        private void getFilesList() {
            post(HttpRequest.HttpMethod.POST, Constant.WT_DOMAIN+"itemServer/getSubmitDocumentAttachment/" + serviceCode);
        }

        private void exitApply(String itemApplyId) {
            post(HttpRequest.HttpMethod.POST, "itemApplyController.do?deteleItemApply",
                    "itemApplyId", itemApplyId);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            switch (action) {
                case 1:
                    if (apiMsg.getMsg().equals("成功")) {
                        try {
                            JSONArray dataList = new JSONArray(apiMsg.getDataList());
                            UploadFiles uploadFiles;
                            for (int i = 0; i < dataList.length(); i++) {
                                String o = dataList.getString(i);
                                uploadFiles = JSON.parseObject(o, UploadFiles.class);
                                uploadFiles.setFileName("未选择任何文件");
                                itemAttachmentId = uploadFiles.getServiceItemId();
                                items.add(uploadFiles);
                            }
                            adapter.addAll(items);
                            adapter.notifyDataSetChanged();
                            callTime = apiMsg.getCallTime();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        App.me().toast(apiMsg.getMsg());
                    }
                    break;
                case 2:
                    App.me().toast(apiMsg.getMsg());
                    break;
            }

        }
    }

    class BottomHolder extends Holder<BaseModel> {
        public BottomHolder() {
            super(UploadFilesActivity.this, R.layout.layout_upload_files_bottom);
        }

        @OnClick(R.id.exit)
        void onExit() {
            action = 2;
            getFilesApi.exitApply(serviceInstanceId);
        }

        @OnClick(R.id.submit)
        void onSubmit(){
            Intent intent = new Intent(UploadFilesActivity.this, SubmitItemApplyActivity.class);
            intent.putExtra("itemApplyId", serviceInstanceId);
            intent.putExtra("name", name);
            intent.putExtra("callTime", callTime);
            intent.putExtra("serviceCode", serviceCode);
            intent.putExtra("applyUserName", applyUserName);
            startActivity(intent);
        }
    }

    private void initViews() {
        mPull.setPtrHandler(this);
        mPull.post(new Runnable() {
            @Override
            public void run() {
                mPull.autoRefresh();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.e("reg", "onActivityResult() error, resultCode: " + resultCode);
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == Constant.FILE_SELECT_CODE) {
            Uri uri = data.getData();
            String url;
            try {
                url = FileUtil.getPath(UploadFilesActivity.this, uri);
                Log.d("reg", "file_url:" + url);
                fileName = url.substring(url.lastIndexOf("/") + 1);
                Log.d("reg", "fileName:" + fileName);
                uploadMultiFile(url, serviceInstanceId, orgCode, serviceCode, attachmentCode, itemAttachmentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadMultiFile(String fileDir, String serviceInstanceId, String orgCode, String serviceCode,
                                 String attachmentCode, String itemAttachmentId) {
        final String url = Constant.DOMAIN+"/itemApplyController.do?ajaxUpload";

        File file = new File(fileDir);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, fileBody)
                .addFormDataPart("serviceInstanceId", serviceInstanceId)
                .addFormDataPart("orgCode", orgCode)
                .addFormDataPart("serviceCode", serviceCode)
                .addFormDataPart("attachmentCode", attachmentCode)
                .addFormDataPart("itemAttachmentId", itemAttachmentId)
                .addFormDataPart("attachUploadType", "0")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Log.d("reg", "upload_url:"+url+"&serviceInstanceId="+serviceInstanceId+"&orgCode="+orgCode+"&serviceCode="+serviceCode+
        "&attachmentCode="+attachmentCode+"&itemAttachmentId="+itemAttachmentId+"&attachUploadType=0");
        client = new OkHttpClient.Builder()
                //设置超时
                .connectTimeout(4000, TimeUnit.SECONDS)
                .writeTimeout(4000, TimeUnit.SECONDS)
                .readTimeout(4000, TimeUnit.SECONDS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                App.me().toast("上传失败");
                Log.e(TAG, "uploadMultiFile() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result =  response.body().string();
                boolean ok = response.isSuccessful();
                if (ok){
                    App.me().toast("上传成功");
                    Log.d(TAG, "uploadMultiFile() result=" +result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            items.get(position).setFileName("上传成功");
                            adapter.notifyDataSetChanged();
                        }
                    });

                }else {
                    Log.d(TAG, "uploadMultiFile() result=" +result);
                }

            }
        });
    }


    @Override
    public void onScrollLastItem(AbsListView view) {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        //结束后调用
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPull.refreshComplete();
            }
        }, 1000);
    }
}
