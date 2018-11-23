/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.kit;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ViewKit {

    public static CompoundButton[] radio(int id, CompoundButton... cbs) {
        if (cbs != null) {
            for (CompoundButton cb : cbs) {
                if (cb != null) {
                    cb.setChecked(cb.getId() == id);
                }
            }
        }
        return cbs;
    }

    public static CompoundButton[] check(boolean checked, CompoundButton... cbs) {
        if (cbs != null) {
            for (CompoundButton cb : cbs) {
                if (cb != null) {
                    cb.setChecked(checked);
                }
            }
        }
        return cbs;
    }

    public static View[] enable(View... views) {
        if (views != null) {
            for (View view : views) {
                if (view != null) {
                    view.setEnabled(true);
                }
            }
        }
        return views;
    }

    public static View[] disable(View... views) {
        if (views != null) {
            for (View view : views) {
                if (view != null) {
                    view.setEnabled(false);
                }
            }
        }
        return views;
    }

    public static boolean noEmpty(TextView... views) {
        boolean valid = views != null;
        if (valid) {
            for (TextView view : views) {
                if (view != null) {
                    String text = view.getText().toString();
                    if (StringKit.isEmpty(text)) {
                        view.setError(view.getHint());
                        if (valid) {
                            ToastKit.toast(view.getHint().toString());
                        }
                        valid = false;
                    } else {
                        view.setError(null);
                    }
                }
            }
        }
        return valid;
    }

    public static boolean noEmpty(TextView view, String message) {
        boolean valid = view != null;
        if (valid) {
            String text = view.getText().toString();
            if (StringKit.isEmpty(text)) {
                view.setError(message);
                ToastKit.toast(message);
                valid = false;
            } else {
                view.setError(null);
            }
        }
        return valid;
    }

    public static boolean matches(TextView view, String regular, String message) {
        boolean valid = view != null;
        if (valid) {
            String text = view.getText().toString();
            if (valid = StringKit.matches(text, regular)) {
                view.setError(null);
            } else {
                view.setError(message);
                ToastKit.toast(message);
            }
        }
        return valid;
    }

    public static boolean equals(TextView view1, TextView view2, String message) {
        boolean valid = view1 != null && view2 != null;
        if (valid) {
            CharSequence error1 = view1.getError();
            CharSequence error2 = view2.getError();
            if (error1 != null || error2 != null) {
                valid = false;
            } else {
                String text1 = view1.getText().toString();
                String text2 = view2.getText().toString();
                if (valid = text1.equals(text2)) {
                    view1.setError(null);
                    view2.setError(null);
                } else {
                    view1.setError(message);
                    view2.setError(message);
                    ToastKit.toast(message);
                }
            }
        }
        return valid;
    }

}
