/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.kit;

import android.view.Gravity;
import android.widget.Toast;

import com.example.administrator.renhua.App;


public class ToastKit {

    static Toast toast;

    public static void toast(String message) {
        if (StringKit.isEmpty(message)) return;
        LogKit.i("ToastKit.toast", message);
        if (toast == null) {
            synchronized (ToastKit.class) {
                if (toast == null) {
                    toast = Toast.makeText(App.me(), message, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                }
            }
        }
        toast.setText(message);
        toast.show();
    }

}
