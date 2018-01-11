package com.example.administrator.renhua.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.activity.ConsultAndQueryActivity;
import com.example.administrator.renhua.ui.activity.EvaluateActivity;
import com.example.administrator.renhua.ui.activity.LifeServiceActivity;
import com.example.administrator.renhua.ui.activity.LoginActivity;
import com.example.administrator.renhua.ui.activity.ProductionServiceActivity;
import com.example.administrator.renhua.ui.activity.WebViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class HomeFragment extends BaseFragment{

    @Bind(R.id.slider)
    SliderLayout mSlider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSlider.addSlider(new DefaultSliderView(getActivity())
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .image("http://www.rhggfw.com:9700/rhggfw/webpage/views/app/img/app-4.jpg"));
        mSlider.addSlider(new DefaultSliderView(getActivity())
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .image("http://www.rhggfw.com:9700/rhggfw/webpage/views/app/img/app-5.jpg"));
        mSlider.addSlider(new DefaultSliderView(getActivity())
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                .image("http://www.rhggfw.com:9700/rhggfw/webpage/views/app/img/app-6.jpg"));
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        PagerIndicator indicator = mSlider.getPagerIndicator();
        indicator.setDefaultSelectedIndicatorSize(8f, 8f, PagerIndicator.Unit.DP);
        indicator.setDefaultUnselectedIndicatorSize(8f, 8f, PagerIndicator.Unit.DP);
        indicator.setDefaultIndicatorColor(Color.parseColor("#ffffffff"), Color.parseColor("#80ffffff"));
    }

    @OnClick(R.id.help)
    void onHelp(){
//        App.me().toast("建设中");
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("mark", "governmentService");
        startActivity(intent);
    }

    @OnClick(R.id.counsel)
    void onConsult(){
        startActivity(new Intent(getActivity(), ConsultAndQueryActivity.class));
    }

    @OnClick(R.id.target_poverty)
    void onTarget_poverty(){
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("mark", "poverty");
        startActivity(intent);
    }

    @OnClick(R.id.party_function)
    void onParty_function(){
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("mark", "party");
        startActivity(intent);
    }

    @OnClick(R.id.auto_function)
    void onAuto_function(){
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("mark", "auto");
        startActivity(intent);
    }

    @OnClick(R.id.economy_function)
    void onEconomy_function(){
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("mark", "economy");
        startActivity(intent);

    }

    @OnClick(R.id.evaluate)
    void onEvaluate(){
        LoginResponse login = App.me().login();
        if (login != null) {
            startActivity(new Intent(getActivity(), EvaluateActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @OnClick(R.id.life_service)
    void onLife_service(){
        startActivity(new Intent(getActivity(), LifeServiceActivity.class));
    }

    @OnClick(R.id.production_service)
    void onTraffic(){
        Intent intent = new Intent(getActivity(), ProductionServiceActivity.class);
        intent.putExtra("mark", "production");
        startActivity(intent);
    }

}
