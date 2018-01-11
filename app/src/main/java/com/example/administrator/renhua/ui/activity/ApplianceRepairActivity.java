package com.example.administrator.renhua.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.fragment.NewsFragment;
import com.example.administrator.renhua.ui.view.CategoryTabStrip;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class ApplianceRepairActivity extends BaseActivity{

    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_repair);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), title);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<String> catalogs = new ArrayList<String>();

        public MyPagerAdapter(FragmentManager fm, String title) {
            super(fm);
            try {
                JSONArray jsonArray = new JSONArray(title);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
                    catalogs.add(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catalogs.get(position);
        }

        @Override
        public int getCount() {
            return catalogs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return NewsFragment.newInstance(position, catalogs.get(position));
        }

    }

    @OnClick(R.id.back)
    void onBack(){
        onBackPressed();
    }


}
