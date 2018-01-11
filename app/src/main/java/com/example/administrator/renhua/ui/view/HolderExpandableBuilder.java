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

public interface HolderExpandableBuilder {

    View inflateGroupView(Context context, int groupPosition);

    HolderExpandable createGroupHolder(View view, int groupPosition);

    View inflateChildView(Context context, int groupPosition, int childPosition);

    HolderExpandable createChildHolder(View view, int groupPosition, int childPosition);

}