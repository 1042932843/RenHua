package com.example.administrator.renhua.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.CollectorActivity;
import com.example.administrator.renhua.ui.adapter.FragmentPagerAdapter;
import com.example.administrator.renhua.ui.fragment.HomeFragment;
import com.example.administrator.renhua.ui.fragment.ProgressFragment;
import com.example.administrator.renhua.ui.fragment.UserFragment;
import com.example.administrator.renhua.ui.listener.PgyUpdateManagerListener;
import com.example.administrator.renhua.ui.view.NoScrollViewPager;
import com.pgyersdk.update.PgyUpdateManager;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class MainActivity extends BaseActivity {

    @Bind(R.id.pager)
    NoScrollViewPager mPager;

    @Bind(R.id.navHome)
    CheckBox mNavHome;

    @Bind(R.id.navProgress)
    CheckBox mNavProgress;

    @Bind(R.id.navUser)
    CheckBox mNavUser;

    private long backTimeMillis;
    //region 已启动过或重新启动时则不再显示启动页面
    // 例:小米打开相机后被activity结束,返回App时为重启
    private boolean started;
    private boolean restarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        started = savedInstanceState != null && savedInstanceState.getBoolean("started", false); // 是否启动过
        restarted = savedInstanceState != null && savedInstanceState.getBoolean("restarted", false); // 是否为重启
        if (!started) { // 如未启动过
            started = true; // 设置为已启动
            startActivity(new Intent(this, StartActivity.class)); // 显示启动页
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPager();
        PgyUpdateManager.register(this, new PgyUpdateManagerListener(this));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    void initPager() {
        Log.d("reg", "000000" + mPager);
        mPager = (NoScrollViewPager) findViewById(R.id.pager);
        mNavProgress = (CheckBox) findViewById(R.id.navProgress);
        mNavUser = (CheckBox) findViewById(R.id.navUser);
        if (mPager != null) {
            Log.d("reg", "365646");
            //viewpage的缓存机制，缓存两个页面，因为反复的销毁和重建也很耗费系统资源
            //viewpager当前页面两侧缓存/预加载的页面数目。当页面切换时，当前页面相邻两侧的numbers页面不会被销毁。
            mPager.setOffscreenPageLimit(2);
            mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                    HomeFragment.class, ProgressFragment.class, UserFragment.class));
        }
    }

    @OnPageChange(R.id.pager)
    void onPageChange(int pos) {
        mNavHome.setChecked(pos == 0);
        mNavProgress.setChecked(pos == 1);
        mNavUser.setChecked(pos == 2);
        EventBus.getDefault().post(pos, "MainActivity.onPageChange");
        Log.d("reg", "OnPageChange");
    }

    @OnClick(R.id.navHome)
    void onNavQueryClick() {
        mNavHome.setChecked(true);
        mPager.setCurrentItem(0, false);
        Log.d("reg", "navHome");
    }

    @OnClick(R.id.navProgress)
    void onNavCardClick() {
        mNavProgress.setChecked(true);
        mPager.setCurrentItem(1, false);
        Log.d("reg", "navCar");
    }

    @OnClick(R.id.navUser)
    void onNavUserClick() {
        mNavUser.setChecked(true);
        mPager.setCurrentItem(2, false);
        Log.d("reg", "navUser");
    }

    @Override
    public void onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (backTimeMillis != 0 && currentTimeMillis - backTimeMillis < 3000) {
            CollectorActivity.finishAll();
            super.onBackPressed();
        } else {
            App.me().toast("再按一次返回键退出");
        }
        backTimeMillis = currentTimeMillis;
    }

    @Override
    protected void onDestroy() {
        PgyUpdateManager.unregister();
        super.onDestroy();
    }
}
