package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RadioButton;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.adapter.FragmentPagerAdapter;
import com.example.administrator.renhua.ui.adapter.ZtEvaluateAdapter;
import com.example.administrator.renhua.ui.fragment.BsEvaluateFragment;
import com.example.administrator.renhua.ui.fragment.ZtEvaluateFragment;
import com.example.administrator.renhua.ui.view.NoScrollViewPager;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by Administrator on 2017/2/3 0003.
 */

public class EvaluateActivity extends BaseActivity {

    @Bind(R.id.pager)
    NoScrollViewPager mPager;
    @Bind(R.id.zt_evaluate)
    RadioButton mZt;
    @Bind(R.id.bs_evaluate)
    RadioButton mBs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initPager();

    }

    void initPager() {
        if (mPager != null) {
            //viewpage的缓存机制，缓存两个页面，因为反复的销毁和重建也很耗费系统资源
            //viewpager当前页面两侧缓存/预加载的页面数目。当页面切换时，当前页面相邻两侧的numbers页面不会被销毁。
            mPager.setOffscreenPageLimit(1);

            mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                    ZtEvaluateFragment.class, BsEvaluateFragment.class));
        }
    }

    @OnPageChange(R.id.pager)
    void onPagerChanged(int pos) {
        mZt.setChecked(pos == 0);
        mBs.setChecked(pos == 1);
        ZtEvaluateAdapter.cleanEvaItemMap();
    }

    @OnClick(R.id.zt_evaluate)
    void onQuestionType() {
        mZt.setChecked(true);
        mPager.setCurrentItem(0, false);
    }

    @Override
    protected void onRestart() {
        ZtEvaluateAdapter.cleanEvaItemMap();
        super.onRestart();
    }

    @OnClick(R.id.bs_evaluate)
    void onCounsel() {
        mBs.setChecked(true);
        mPager.setCurrentItem(1, false);
    }

    @OnClick(R.id.back)
    void onBack() {
        onBackPressed();
    }
}
