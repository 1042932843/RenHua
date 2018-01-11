package com.example.administrator.renhua.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.renhua.App;
import com.example.administrator.renhua.BuildConfig;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.LoginResponse;
import com.example.administrator.renhua.ui.activity.LoginActivity;
import com.example.administrator.renhua.ui.activity.MyRecordActivity;
import com.example.administrator.renhua.ui.activity.UpdatePwdActivity;
import com.example.administrator.renhua.ui.activity.UserInfoActicity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class UserFragment extends BaseFragment {

    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.logout)
    Button mLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        version.setText("当前版本号："+ BuildConfig.VERSION_NAME);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginResponse login = App.me().login();
        if (login == null) {
            mLogout.setText("登录");
        } else {
            mLogout.setText("退出");
        }
    }

    @OnClick(R.id.logout)
    void onLogout() {
        LoginResponse login = App.me().login();
        if (login == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            App.me().logout();
            mLogout.setText("登录");
            EventBus.getDefault().post("login", "UserFragment.onLogoutSuccess");
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Subscriber(tag = "LoginActivity.onLoginSuccess")
    void onLoginSuccess(LoginResponse login){
        mLogout.setText("退出");
    }

    @OnClick(R.id.user_info)
    void onUserInfo() {
        LoginResponse login = App.me().login();
        if (login != null) {
            startActivity(new Intent(getActivity(), UserInfoActicity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @OnClick(R.id.update_pwd)
    void onCostRecord() {
        LoginResponse login = App.me().login();
        if (login != null) {
            startActivity(new Intent(getActivity(), UpdatePwdActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.my_record)
    public void onClick() {
        LoginResponse login = App.me().login();
        if (login != null) {
            startActivity(new Intent(getActivity(), MyRecordActivity.class));
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

}
