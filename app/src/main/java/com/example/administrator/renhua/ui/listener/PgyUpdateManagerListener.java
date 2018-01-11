/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.administrator.renhua.BuildConfig;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.StringUtil;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.UpdateManagerListener;

public class PgyUpdateManagerListener extends UpdateManagerListener {

    final Activity activity;

    public PgyUpdateManagerListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onNoUpdateAvailable() {
    }

    @Override
    public void onUpdateAvailable(String s) {
        final AppBean bean = getAppBeanFromString(s);
        if ((!StringUtil.matchesNumber(bean.getVersionCode()) ||
                Integer.valueOf(bean.getVersionCode()) > BuildConfig.VERSION_CODE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setIcon(R.mipmap.home_logo);
            builder.setTitle("发现新版本：" + bean.getVersionName());
            builder.setMessage(bean.getReleaseNote());
            builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startDownloadTask(activity, bean.getDownloadURL());
                }
            });
            builder.setNegativeButton("取消", null);
            builder.setCancelable(false);
            builder.show();
        }
    }
}
