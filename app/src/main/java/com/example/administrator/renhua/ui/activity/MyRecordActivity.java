package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.adapter.FragmentPagerAdapter;
import com.example.administrator.renhua.ui.fragment.AffairsRecordFragment;
import com.example.administrator.renhua.ui.fragment.CostRecordFragment;
import com.example.administrator.renhua.ui.fragment.QuestionRecordFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class MyRecordActivity extends BaseActivity {

    @Bind(R.id.rg3)
    RadioGroup rg3;
    @Bind(R.id.cost_record)
    RadioButton mCostRecord;
    @Bind(R.id.question_record)
    RadioButton mQuestionRecord;
    @Bind(R.id.affairs_record)
    RadioButton mAffairsRecord;
    @Bind(R.id.pager)
    ViewPager mPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_record);
        ButterKnife.bind(this);
        initFragments();
    }

    public void initFragments() {
        if (mPager != null) {
            Log.d("reg", "365646");
            //viewpage的缓存机制，缓存两个页面，因为反复的销毁和重建也很耗费系统资源
            //viewpager当前页面两侧缓存/预加载的页面数目。当页面切换时，当前页面相邻两侧的numbers页面不会被销毁。
            mPager.setOffscreenPageLimit(2);
            mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                    CostRecordFragment.class, QuestionRecordFragment.class, AffairsRecordFragment.class));
        }
    }

    @OnPageChange(R.id.pager)
    void onPagerChanged(int pos) {
        mCostRecord.setChecked(pos == 0);
        mQuestionRecord.setChecked(pos == 1);
        mAffairsRecord.setChecked(pos == 2);
    }

    @OnClick(R.id.cost_record)
    void onCost() {
        mCostRecord.setChecked(true);
        mPager.setCurrentItem(0, false);
    }

    @OnClick(R.id.question_record)
    void onQuestion() {
        mQuestionRecord.setChecked(true);
        mPager.setCurrentItem(1, true);
    }

    @OnClick(R.id.affairs_record)
    void onAffairs() {
        mAffairsRecord.setChecked(true);
        mPager.setCurrentItem(2, false);
    }

    @OnClick(R.id.back)
    void onBck(){
        onBackPressed();
    }
}

