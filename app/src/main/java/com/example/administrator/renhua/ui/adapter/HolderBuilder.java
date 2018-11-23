/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.adapter;

public abstract class HolderBuilder<T> {

    public abstract Holder<T> createHolder(int position, T item);

    public int getViewTypeCount() {
        return 1;
    }

    public int getItemViewType(int position, T item) {
        return 0;
    }

    public boolean isItemViewTypePinned(int viewType) {
        return false;
    }

    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    public T getChild(int groupPosition, int childPosition) {
        return null;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void onGroupExpanded(int groupPosition) {
    }


    public void onGroupCollapsed(int groupPosition) {
    }

}