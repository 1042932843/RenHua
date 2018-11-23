package com.example.administrator.renhua.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.example.administrator.renhua.ui.fragment.ConditionfFragment;
import com.example.administrator.renhua.ui.fragment.MaterialFragment;

import org.simple.eventbus.EventBus;

/**
 * Created by lenovo on 2017/2/24.
 */
public class HandleAdaper extends FragmentPagerAdapter {



    private String[] mTitles = new String[]{"办理条件", "所需材料"};


    private String conditions;
    private String material;


    public HandleAdaper(FragmentManager fm, String conditions, String material) {
        super(fm);
        this.conditions = conditions;
        this.material = material;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            EventBus.getDefault().post(0, "material");
            ConditionfFragment newFragment = new ConditionfFragment();
            Bundle bundle = new Bundle();
            bundle.putString("conditions", conditions);
            newFragment.setArguments(bundle);
            return  newFragment;//办理条件
        } else if (position == 1) {
            EventBus.getDefault().post(1, "material");
//            return  new MaterialFragment(newsId);//所需材料
            MaterialFragment newFragment = new MaterialFragment();
            Bundle bundle = new Bundle();
            bundle.putString("material", material);
            newFragment.setArguments(bundle);
            return  newFragment;//办理条件
        }
        ConditionfFragment newFragment = new ConditionfFragment();
        Bundle bundle = new Bundle();
        bundle.putString("conditions", conditions);
        newFragment.setArguments(bundle);
        return  newFragment;
    }


    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
            return mTitles[position];
    }
}
