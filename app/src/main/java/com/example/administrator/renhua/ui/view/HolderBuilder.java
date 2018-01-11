/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.view;

import android.content.Context;
import android.view.View;

public interface HolderBuilder {

    View inflateView(Context context, int position);

    Holder createHolder(View view, int position);

}
