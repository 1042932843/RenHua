package com.example.administrator.renhua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.view.AppWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class ProgressFragment extends BaseFragment {

    @Bind(R.id.webView)
    AppWebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.bind(this, view);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl("http://www.gdbs.gov.cn/portal/bjInfoQueryPage.do");
        return view;
    }

//    @OnClick(R.id.search_progress)
//    void onProgress() {
//        App.me().toast("建设中");
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
