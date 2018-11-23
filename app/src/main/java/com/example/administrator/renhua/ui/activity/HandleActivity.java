package com.example.administrator.renhua.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.JsonReader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.Enclosure;
import com.example.administrator.renhua.entity.FromInfo;
import com.example.administrator.renhua.entity.FromInfo2;
import com.example.administrator.renhua.entity.cllnfoItem;
import com.example.administrator.renhua.ui.adapter.DataAdapter;
import com.example.administrator.renhua.ui.adapter.HandleAdaper;
import com.example.administrator.renhua.ui.view.NoScrollViewPager;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.example.administrator.renhua.utils.EndlessRecyclerOnScrollListener;
import com.example.administrator.renhua.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/2/24.
 */
public class HandleActivity extends BaseActivity {
    String serviceCode="";
    String itemCode="";
    String title="";
    MediaType type=MEDIA_TYPE_PNG;
    @Bind(R.id.title)
    TextView titleTv;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    List<File> files;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    FromInfo frominfo;
    FromInfo2 fromInfo2;
    cllnfoItem currentItem;
    @OnClick(R.id.next)
    public void Next(){
        Apply();
    }
    private DataAdapter adapter;
    private List<Enclosure> datas;
    List<cllnfoItem> items;
    JSONArray array;
    private boolean mIsRefreshing = false;
    int currentPage=1;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);
        ButterKnife.bind(this);
        items=new ArrayList<>();
        array=new JSONArray();
        frominfo=(FromInfo) getIntent().getSerializableExtra("datainput");
        serviceCode = frominfo.getShiJianBianMa();
        itemCode= frominfo.getItemCode();
        Log.d("reg", "serviceCode:"+serviceCode);
        title = frominfo.getXiangMuMingCheng();
        titleTv.setText(title);
        initRecyclerView();
        loadData();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            mEndlessRecyclerOnScrollListener.refresh();
            loadData();
        });
    }

    public void initRecyclerView(){
        datas=new ArrayList<>();
        files=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DataAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                //loadData();

            }
        };
        //recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        //setRecycleNoScroll();
        adapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

                if("doc".contains(datas.get(position).getEmpty_name())){
                    type=MEDIA_TYPE_DOC;
                }
                if("xls".contains(datas.get(position).getEmpty_name())){
                    type=MEDIA_TYPE_EXC;
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
                ToastUtil.ShortToast("选择文件");
                currentItem=items.get(position);
                //uploadData();
            }
            @Override
            public void onLongClick(int position) {
                //ToastUtil.ShortToast(position+"");
            }
        });
    }

    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();

                String path="";
                try {
                    path=getPath(HandleActivity.this,uri);
                    Toast.makeText(this, "文件路径："+path, Toast.LENGTH_SHORT).show();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                String url=Constant.DOMAIN+"bsznController.do?uploadFile";
                Map<String ,String > params=new HashMap<>();
                params.put("uid",App.me().login().token);
                files.clear();
                File file = new File(path);
                files.add(file);
                uploadData(url,params,"file",files,MediaType.parse("multipart/form-data"),currentItem);
            }
        }
    }

    public void loadData(){
    String url = Constant.DOMAIN  + "bsznController.do?getMTDocList";
    OkHttpClient okHttpClient = new OkHttpClient();
    MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            multipartBodyBuilder.setType(MultipartBody.FORM);
            multipartBodyBuilder.addFormDataPart("itemId",serviceCode);
            multipartBodyBuilder.addFormDataPart("Access_token",App.me().login().token);

    RequestBody requestBody = multipartBodyBuilder.build();
    Request.Builder RequestBuilder = new Request.Builder();
            RequestBuilder.url(url);// 添加URL地址
            RequestBuilder.post(requestBody);
    Request request = RequestBuilder.build();
            okHttpClient.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("err", "result:" + e);
            call.cancel();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIsRefreshing=false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String str = response.body().string();
            //{"attributes":{"total":1,"data":{"NAME":"转基因水生生物加工审核","ITEM_ID":"6C900CAFF1344942967D5B6DA06F3561","meterias":[{"filling_requirement":"每项内容填写清晰、准确并加盖申请人公章","copy_check_num":0,"origin_check_num":0,"origin":1,"copy_accept_num":0,"is_situation":"0","sample_name":"农业转基因生物加工许可证申请表范本.doc","creator":"2868","description":"4","origin_accept_num":1,"empty":"854850","empty_name":"农业转基因生物加工许可证申请表.doc","last_modification_time":1530131191000,"creation_time":1530131191000,"materials_name":"《农业转基因生物加工许可证申请表》","sort_order":"00001","last_modificator":"2868","materials_code":"602568","materials_type":"3","sample":"854849","copy":0},{"filling_requirement":"真实性、有效性","copy_check_num":0,"origin_check_num":0,"origin":0,"copy_accept_num":0,"is_situation":"0","creator":"2868","issuing_organ":"渔业主���部门","description":"1","origin_accept_num":1,"last_modification_time":1530131191000,"creation_time":1530131191000,"materials_name":"加工原料的《农业转基因生物安全证书》","sort_order":"00001","last_modificator":"2868","materials_code":"602569","materials_type":"2","copy":1},{"filling_requirement":"样本的真实性、有效性等","copy_check_num":0,"origin_check_num":0,"origin":1,"copy_accept_num":0,"is_situation":"0","creator":"2868","description":"4","origin_accept_num":1,"last_modification_time":1530131191000,"creation_time":1530131191000,"materials_name":"农业转基因生物产品标识样本","sort_order":"00001","last_modificator":"2868","materials_code":"602570","materials_type":"2","copy":0},{"filling_requirement":"每项内容填写清晰、准确，说明和承诺书需责任人签名，加盖责任单位公章","copy_check_num":0,"origin_check_num":0,"origin":1,"copy_accept_num":0,"is_situation":"0","creator":"2868","description":"4","origin_accept_num":1,"last_modification_time":1530131191000,"creation_time":1530131191000,"materials_name":"农业转基因生物安全管理小组人员名单和专业知识、学历证明","sort_order":"00001","last_modificator":"2868","materials_code":"602572","materials_type":"2","copy":0},{"filling_requirement":"无","copy_check_num":0,"origin_check_num":0,"origin":1,"copy_accept_num":0,"is_situation":"0","creator":"2868","description":"4","origin_accept_num":1,"last_modification_time":1530131191000,"creation_time":1530131191000,"materials_name":"农业转基因生物安全法规和加工安全知识培训记录","sort_order":"00001","last_modificator":"2868","materials_code":"602571","materials_type":"2","copy":0},{"filling_requirement":"无","copy_check_num":0,"origin_check_num":0,"origin":1,"copy_accept_num":0,"is_situation":"0","creator":"2868","description":"4","origin_accept_num":1,"last_modification_time":1530131191000,"creation_time":1530131191000,"materials_name":"农业转基因生物加工安全管理制度文本","sort_order":"00001","last_modificator":"2868","materials_code":"602573","materials_type":"2","copy":0}],"individuals":[],"CODE":"00697000801009440004440224"},"state":1},"msg":"查询成功","obj":null,"success":true,"jsonStr":"{\"attributes\":{\"total\":1,\"data\":{\"NAME\":\"转基因水生生物加工审核\",\"ITEM_ID\":\"6C900CAFF1344942967D5B6DA06F3561\",\"meterias\":[{\"filling_requirement\":\"每项内容填写清晰、准确并加盖申请人公章\",\"copy_check_num\":0,\"origin_check_num\":0,\"origin\":1,\"copy_accept_num\":0,\"is_situation\":\"0\",\"sample_name\":\"农业转基因生物加工许可证申请表范本.doc\",\"creator\":\"2868\",\"description\":\"4\",\"origin_accept_num\":1,\"empty\":\"854850\",\"empty_name\":\"农业转基因生物加工许可证申请表.doc\",\"last_modification_time\":1530131191000,\"creation_time\":1530131191000,\"materials_name\":\"《农业转基因生物加工许可证申请表》\",\"sort_order\":\"00001\",\"last_modificator\":\"2868\",\"materials_code\":\"602568\",\"materials_type\":\"3\",\"sample\":\"854849\",\"copy\":0},{\"filling_requirement\":\"真实性、有效性\",\"copy_check_num\":0,\"origin_check_num\":0,\"origin\":0,\"copy_accept_num\":0,\"is_situation\":\"0\",\"creator\":\"2868\",\"issuing_organ\":\"渔业主���部门\",\"description\":\"1\",\"origin_accept_num\":1,\"last_modification_time\":1530131191000,\"creation_time\":1530131191000,\"materials_name\":\"加工原料的《农业转基因生物安全证书》\",\"sort_order\":\"00001\",\"last_modificator\":\"2868\",\"materials_code\":\"602569\",\"materials_type\":\"2\",\"copy\":1},{\"filling_requirement\":\"样本的真实性、有效性等\",\"copy_check_num\":0,\"origin_check_num\":0,\"origin\":1,\"copy_accept_num\":0,\"is_situation\":\"0\",\"creator\":\"2868\",\"description\":\"4\",\"origin_accept_num\":1,\"last_modification_time\":1530131191000,\"creation_time\":1530131191000,\"materials_name\":\"农业转基因生物产品标识样本\",\"sort_order\":\"00001\",\"last_modificator\":\"2868\",\"materials_code\":\"602570\",\"materials_type\":\"2\",\"copy\":0},{\"filling_requirement\":\"每项内容填写清晰、准确，说明和承诺书需责任人签名，加盖责任单位公章\",\"copy_check_num\":0,\"origin_check_num\":0,\"origin\":1,\"copy_accept_num\":0,\"is_situation\":\"0\",\"creator\":\"2868\",\"description\":\"4\",\"origin_accept_num\":1,\"last_modification_time\":1530131191000,\"creation_time\":1530131191000,\"materials_name\":\"农业转基因生物安全管理小组人员名单和专业知识、学历证明\",\"sort_order\":\"00001\",\"last_modificator\":\"2868\",\"materials_code\":\"602572\",\"materials_type\":\"2\",\"copy\":0},{\"filling_requirement\":\"无\",\"copy_check_num\":0,\"origin_check_num\":0,\"origin\":1,\"copy_accept_num\":0,\"is_situation\":\"0\",\"creator\":\"2868\",\"description\":\"4\",\"origin_accept_num\":1,\"last_modification_time\":1530131191000,\"creation_time\":1530131191000,\"materials_name\":\"农业转基因生物安全法规和加工安全知识培训记录\",\"sort_order\":\"00001\",\"last_modificator\":\"2868\",\"materials_code\":\"602571\",\"materials_type\":\"2\",\"copy\":0},{\"filling_requirement\":\"无\",\"copy_check_num\":0,\"origin_check_num\":0,\"origin\":1,\"copy_accept_num\":0,\"is_situation\":\"0\",\"creator\":\"2868\",\"description\":\"4\",\"origin_accept_num\":1,\"last_modification_time\":1530131191000,\"creation_time\":1530131191000,\"materials_name\":\"农业转基因生物加工安全管理制度文本\",\"sort_order\":\"00001\",\"last_modificator\":\"2868\",\"materials_code\":\"602573\",\"materials_type\":\"2\",\"copy\":0}],\"individuals\":[],\"CODE\":\"00697000801009440004440224\"},\"state\":1},\"msg\":\"查询成功\",\"success\":true}"}
            Log.d("reg", "result:" + str);
            try {
                JSONObject obj = new JSONObject(str);
                JSONObject data= obj.getJSONObject("attributes");
                JSONObject data2= data.getJSONObject("data");

                JSONArray jsonArray = data2.getJSONArray("meterias");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                    Enclosure annuncement= JSON.parseObject(o, Enclosure.class);
                    cllnfoItem item=new cllnfoItem();
                    item.setCode(annuncement.getMaterials_code());
                    items.add(item);
                    datas.add(annuncement);
                }
                currentPage++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    mIsRefreshing=false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            call.cancel();
        }
    });

}


    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_DOC = MediaType.parse("application/msword");
    private static final MediaType MEDIA_TYPE_EXC = MediaType.parse("application/vnd.ms-excel");
    public void uploadData(String reqUrl, Map<String, String> params, String pic_key, List<File> files,MediaType type,cllnfoItem item){
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                if(null!=params.get(key)){
                    multipartBodyBuilder.addFormDataPart(key, params.get(key));
                }else{
                    multipartBodyBuilder.addFormDataPart(key, "");
                }

            }
        }

        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(type, file));
            }
        }
        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("reg", "result:" + str);
                ApiMsg apiMsg = JSON.parseObject(str,ApiMsg.class);
                item.setDocid("");
                item.setName("test");
                ToastUtil.ShortToast(apiMsg.getMsg());
                call.cancel();
            }
        });
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public void Apply(){
        String url = Constant.DOMAIN  + "bsznController.do?applySubmit";
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("sxbm",itemCode);
        String token=App.me().login().token;
        multipartBodyBuilder.addFormDataPart("access_token",App.me().login().token);
        //1、使用JSONObject
        Gson gs = new Gson();
        fromInfo2=new FromInfo2();
        fromInfo2.setDiZhi(frominfo.getDiZhi());
        fromInfo2.setLianXiRen(frominfo.getLianXiRen());
        fromInfo2.setShenQingRen(frominfo.getShenQingRen());
        fromInfo2.setShouJi(frominfo.getShouJi());
        fromInfo2.setXiangMuMingCheng(frominfo.getXiangMuMingCheng());
        fromInfo2.setZhengJianLeiXing("1");
        fromInfo2.setZhengJianHaoMa(frominfo.getZhengJianHaoMa());

        String objectStr = gs.toJson(fromInfo2);//把对象转为JSON格式的字符串
        multipartBodyBuilder.addFormDataPart("formInfo",objectStr);
        String jsonArray=array.toString();
        multipartBodyBuilder.addFormDataPart("clInfo",jsonArray);
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                Log.d("reg", "result:" + str);
                try {
                    JSONObject obj = new JSONObject(str);
                    JSONObject data= obj.getJSONObject("attributes");
                    String msg=obj.getString("msg");
                    if(msg.contains("失效")){
                        ToastUtil.ShortToast(msg);
                        Intent it=new Intent(HandleActivity.this,LoginActivity.class);
                        startActivity(it);
                    }else{
                        ToastUtil.ShortToast(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                call.cancel();
            }
        });

    }

}
