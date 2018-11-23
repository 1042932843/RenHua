package com.example.administrator.renhua.ui.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.Enclosure;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.view.CustomWebView;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.example.administrator.renhua.utils.LoadDialog;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lenovo on 2017/2/24.
 */
public class ServiceGuideActivity extends BaseActivity implements View.OnClickListener {
    AnnexDownloadApi annexDownloadApi;
    ServiceApi serviceApi;
    private CustomWebView webView;
    private TextView Title;
    private String titl;
    private String serviceCode;
    private Button Apply;
    @OnClick(R.id.chaxun)
    void  chaxun(){
        Intent it=new Intent(this,ProgressActivity.class);
        startActivity(it);
    }
    private TextView drawerimg;
    private DrawerLayout mDrawerLayout = null;
    private boolean isShowOrNot = false;
    private ArrayList<Enclosure> items;
    private EnclosureAdaptent adapter;
    private ListView listView;
    private static Button tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceguide);
        ButterKnife.bind(this);
        items = new ArrayList<Enclosure>();
       // serviceApi=new ServiceApi(this);
        annexDownloadApi=new AnnexDownloadApi(this);

        assignViews();
        initViews();
        titl = getIntent().getStringExtra("title");
        serviceCode = getIntent().getStringExtra("serviceCode");
        annexDownloadApi.Download();
       // serviceApi.getSewrvice();
        Title.setText(titl + "指南");
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.loadUrl(Constant.DOMAIN + "webpage/views/app/guide_template.html?itemId=" + serviceCode);
//            webView.loadUrl("file:///android_asset/guide_template.html");
    }


    private class ServiceApi extends AsyncHttpDialog {

        public ServiceApi(Context context) {
            super(context);
        }

        private void getSewrvice() {
            post(HttpRequest.HttpMethod.POST, "bsznController.do?getAllItemInfo","Itemid",serviceCode);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            String msg=apiMsg.getMsg();

        }
    }


    private void assignViews() {
        listView = (ListView) findViewById(R.id.list);
        drawerimg = (TextView) findViewById(R.id.drawerimg);
        webView = (CustomWebView) findViewById(R.id.webView);
        Title = (TextView) findViewById(R.id.Title);
        Apply = (Button) findViewById(R.id.Apply);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        listView.setAdapter(adapter = new EnclosureAdaptent(ServiceGuideActivity.this));
        //这里设置clickable(true)  必须动态设置  静态设置没有效果
        //解决问题   侧滑菜单出来的时候 点击菜单上的区域会有点击穿透问题

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });


        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
             */
            @Override
            public void onDrawerStateChanged(int arg0) {
                Log.i("drawer", "drawer的状态：" + arg0);
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * arg1 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View arg0, float arg1) {
                Log.i("drawer", arg1 + "");
            }

            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View arg0) {
                Log.i("drawer", "抽屉被完全打开了！");
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                isShowOrNot = true;
                //annexDownloadApi.Download();
            }

            /**
             * 当一个抽屉完全关闭的时候调用此方法
             */
            @Override
            public void onDrawerClosed(View arg0) {
                Log.i("drawer", "抽屉被完全关闭了！");
                isShowOrNot = false;
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

    }

    private void onIC(int i, View v) {
        tvName = (Button) v;
        Log.d("reg", "iii:" + i);
        Enclosure area = adapter.getItem(i);

       if(!TextUtils.isEmpty(area.getEmpty())){
           area.setFileUrl("http://sg.disk.sg.gov.cn/WebDiskServerDemo/doc?doc_id="+area.getEmpty());
           area.setFileName(area.getEmpty_name());
       }else{
           area.setFileUrl("http://sg.disk.sg.gov.cn/WebDiskServerDemo/doc?doc_id="+area.getSample());
           area.setFileName(area.getSample_name());
       }

        if(TextUtils.isEmpty(area.getFileUrl())||TextUtils.isEmpty(area.getFileName())){
            Toast.makeText(this,"文件错误无法下载！",Toast.LENGTH_SHORT).show();
            return;
        }
        downFile(area.getFileUrl(), area.getFileName());
    }

    /**
     * 下载文件
     *
     * @param url
     */
    public void downFile(String url, String name) {
        try {
            downLoadFile(url, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                    "FileDownloader/", name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initViews() {
        View[] views = {drawerimg, Apply};
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Apply:
                Apply();
                break;
            case R.id.drawerimg:
                if(items.size()<=0){
                    Toast.makeText(this,"没有可以下载的文件",Toast.LENGTH_SHORT).show();
                    return;
                }
                RightDrawer();
                break;
        }
    }

    private void RightDrawer() {
        // 按钮按下，将抽屉打开
        if (isShowOrNot) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
            isShowOrNot = false;

        } else {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
            isShowOrNot = true;
            //annexDownloadApi.Download();

        }
    }

    private void Apply() {
        LoginResponse login = App.me().login();
        if (login != null) {
            Intent intent = new Intent(this, DataInputActivity.class);
            intent.putExtra("serviceCode", serviceCode);
            intent.putExtra("itemCode",getIntent().getStringExtra("itemCode"));
            intent.putExtra("title", titl);
            startActivity(intent);
        } else {
            startActivity(new Intent(ServiceGuideActivity.this, LoginActivity.class));
        }

    }

    private class AnnexDownloadApi extends AsyncHttpDialog {

        public AnnexDownloadApi(Context context) {
            super(context);
        }

        private void Download() {
            post(HttpRequest.HttpMethod.POST, "bsznController.do?getMTDocList",
                    "itemId", serviceCode);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (adapter == null) {
                adapter = new EnclosureAdaptent(ServiceGuideActivity.this);
            } else {
                adapter.clear();
            }
            if (items == null) {
                items = new ArrayList<Enclosure>();
            } else {
                items.clear();
            }

            try {
                JSONObject data = new JSONObject(apiMsg.getAttributes());
                JSONObject list =data.getJSONObject("data");
                JSONArray jsonArray = list.getJSONArray("meterias");
                for (int ii = 0; ii < jsonArray.length(); ii++) {
                    String yy = jsonArray.getString(ii);
                    Enclosure en = JSON.parseObject(yy, Enclosure.class);
                    if(!TextUtils.isEmpty(en.getEmpty())||!TextUtils.isEmpty(en.getSample())){
                        items.add(en);
                    }
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class EnclosureAdaptent extends BaseAdapter {
        private HashSet<Integer> hs;
        private Context mContext;

        public EnclosureAdaptent(Context context) {
            this.mContext = context;
            this.hs = new HashSet<Integer>();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Enclosure getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class holder {
            TextView text;
            Button button;

        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final holder holder;
            if (view == null) {
                view = View.inflate(mContext, R.layout.activity_enclosure, null);
                holder = new holder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.button = (Button) view.findViewById(R.id.downLoadBtn);
                view.setTag(holder);
            } else {
                holder = (holder) view.getTag();
            }
            final Enclosure item = items.get(position);
            holder.text.setText(item.getMaterials_name());
            if (item.getMaterials_name().indexOf("（") != -1) {
                Log.d("reg", "name1111:" + item.getFileName());
                holder.text.setBackgroundResource(R.drawable.list_item_background);
                holder.button.setVisibility(View.GONE);
                holder.text.setTextColor(getResources().getColor(R.color.transparent));
            } else {
                holder.text.setBackgroundResource(R.drawable.list_frame_background);
                holder.button.setVisibility(View.VISIBLE);
            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mItemOnClickListener.itemOnClickListener(v);
                    onIC(position, v);
                }
            });

            return view;
        }

        public HashSet<Integer> getHs() {
            return hs;
        }

        public void setHs(HashSet<Integer> hs) {
            this.hs = hs;
        }

        public void clear() {
            items.clear();
        }

        public boolean addAll(Collection<? extends Enclosure> collection) {
            boolean pa = items.addAll(collection);
            return pa;
        }

        public boolean add(Enclosure object) {
            return items.add(object);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawers();
                Log.d("reg", "11111");
            }else{
                Log.d("reg", "222222");
                super.onBackPressed();
            }
        }

    }

    @OnClick(R.id.back)
    void onBack() {
        Log.d("reg", "ServiceGuideActivity---->onback");
        onBackPressed();
    }

    /**
     * 下载文件方法
     *
     * @param fileUrl     文件url
     * @param destFileDir 存储目标目录
     */
    public void downLoadFile(String fileUrl, final String destFileDir, String name) throws IOException {

        makeRootDirectory(destFileDir);

        int index = fileUrl.lastIndexOf("/");

        final File file = new File(destFileDir + name);


        Log.d("reg", "file:" + file);
        //文件判断
        if (!file.exists()) {
            file.createNewFile();
            //下载
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(fileUrl).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("reg", "下载失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = -1;
                    FileOutputStream fos = null;
                    try {
                        long total = response.body().contentLength();
                        long current = 0;
                        is = response.body().byteStream();
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            current += len;
                            fos.write(buf, 0, len);
                            onDown(1, (int) (current / (float) total * 100));
//                            Log.d("reg", "current ： " + current);
//                            Log.d("reg", "total ： " + (int) (current / (float) total * 100));
                        }
                        fos.flush();
                        onDown(2, 0);
                        Log.d("reg", "下载完成");
                        tvName.setText("下载完毕，打开");
                        tvName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openFile(file);
                            }
                        });
                    } catch (IOException e) {
//                        Log.e(TAG, e.toString());
                        onDown(-1, 0);
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            Log.e("err", e.toString());
                        }
                    }
                }
            });
        } else {
            tvName.setText("下载完毕，打开");
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFile(file);
                }
            });
        }

    }

    public static void makeRootDirectory(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    public void onDown(int state, long num) {
        Message message = new Message();
        message.arg1 = state;
        message.arg2 = (int) num;
        handler.sendMessage(message);


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
                tvName.setText("下载中");
                LoadDialog.show(ServiceGuideActivity.this);
            } else if (msg.arg1 == 2) {
                LoadDialog.dismiss(ServiceGuideActivity.this);
                tvName.setText("下载完成");
            } else if (msg.arg1 == -1) {
                tvName.setText("下载失败");
            }

        }
    };

    /**
     * <code>openFile</code>
     *
     * @param file
     * @description: TODO(打开附件)
     * @since 2012-5-19    liaoyp
     */
    public void openFile(File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            Uri data;
            // 判断版本大于等于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                data = FileProvider.getUriForFile(this, "com.example.administrator.renhua.fileprovider", file);
                // 给目标应用一个临时授权
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                data = Uri.fromFile(file);

            }
                intent.setDataAndType(/*uri*/data, type);
                //跳转
                startActivity(intent);
                //      Intent.createChooser(intent, "请选择对应的软件打开该附件！");
            } catch(ActivityNotFoundException e){
                // TODO: handle exception
                App.me().toast("sorry附件不能打开，请下载相关软件！");
            }
    }

    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
       /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {


            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    // 可以自己随意添加
    private static String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.Android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };


}
