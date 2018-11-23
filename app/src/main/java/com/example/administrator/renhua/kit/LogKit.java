/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.kit;

import android.util.Log;

import com.example.administrator.renhua.App;
import com.pgyersdk.crash.PgyCrashManager;

public class LogKit implements ConfigKit {

    public static void i(String tag, Object src) {
        if (DEBUG) {
            Log.i(tag, String.valueOf(JsonKit.toJson(src)));
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, String.valueOf(msg));
        }
    }

    public static void i(String tag, String format, Object... args) {
        if (DEBUG) {
            Log.i(tag, String.format(format, args));
        }
    }

    public static void e(String tag, Exception e) {
        if (DEBUG) {
            Log.e(tag, String.valueOf(e.getMessage()), e);
        } else {
            PgyCrashManager.reportCaughtException(App.me(), e);
        }
    }

}