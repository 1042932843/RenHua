package com.example.administrator.renhua.biz;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2016/8/19.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        Log.d("reg", "add activity:"+ activity.getClass().getSimpleName());
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        Log.d("reg", "activity finish1:"+activity.getLocalClassName());
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                Log.d("reg", "activity finish2:"+activity.getLocalClassName());
                activity.finish();
            }
        }
    }
}
