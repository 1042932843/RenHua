package com.example.administrator.renhua;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.administrator.renhua.common.ConfigKit;
import com.example.administrator.renhua.entity.LoginResponse;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.pgyersdk.Pgy;
import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.crash.PgyCrashManager;

import java.util.List;

public class App extends Application implements ConfigKit {

    static App me;

    public static App me() {
        return me;
    }

    Toast toast;

    public SharedPreferences config(){
        return getSharedPreferences(CONFIG, MODE_PRIVATE);
    }

    public void toast(CharSequence text) {
        try {
            if (TextUtils.isEmpty(text)) return;
            if (toast == null) {
                toast = Toast.makeText(me, text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
            }
            toast.setText(text);
            toast.show();
        } catch (Exception e) {
            PgyCrashManager.reportCaughtException(this, e);
        }
    }

    InputMethodManager input;

    public InputMethodManager input() {
        if (input == null) {
            input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        }
        return input;
    }

    public void hideInput(Window window) {
        View view = window.getCurrentFocus();
        hideInput(view);
    }

    public void hideInput(View view) {
        if (view != null) {
            view.clearFocus();
            IBinder binder = view.getWindowToken();
            input().hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    LiteOrm orm;

    public LiteOrm orm() {
        if (orm == null) {
            orm = LiteOrm.newSingleInstance(this, "LocalStorage.db");
            orm.setDebugged(BuildConfig.DEBUG);
        }
        return orm;
    }

    @Nullable
    LoginResponse login;

    public LoginResponse login() {
        if (login == null) {
            List<LoginResponse> list = orm().query(QueryBuilder.get(LoginResponse.class).limit(0, 1));
            if (list.size() > 0) {
                login = list.get(0);
                Log.d("login", "LoginResponse:" + login.phone+login.username+login.idcard+login.uuid);
            }
        }
        return login;
    }
    public void login(@NonNull LoginResponse login) {
        orm().deleteAll(LoginResponse.class);
        orm().save(this.login = login);
    }

    public void logout() {
        orm().deleteAll(LoginResponse.class);
        this.login = null;
    }

    Handler handler;
    public Handler handler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Pgy.setDebug(BuildConfig.DEBUG);
        PgyCrashManager.register(me = this);
        FeedbackActivity.setBarImmersive(true);
    }

}
