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
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HolderExpandableListAdapter<G extends Expandable<C>, C> extends BaseExpandableListAdapter {

    final Context context;
    final HolderExpandableBuilder builder;
    final List<G> list;

    public HolderExpandableListAdapter(Context context, @NonNull HolderExpandableBuilder builder) {
        this(context, builder, new ArrayList<G>());
    }

    public HolderExpandableListAdapter(Context context, @NonNull HolderExpandableBuilder builder, @NonNull List<G> list) {
        this.context = context;
        this.builder = builder;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getChildrenCount();
    }

    @Override
    public G getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public C getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getChild(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        try {
            if (view == null) {
                view = builder.inflateGroupView(context, groupPosition);
            }
            HolderExpandable holder = null;
            Object tag = view.getTag();
            if (tag != null) {
                try {
                    holder = (HolderExpandable) tag;
                } catch (ClassCastException e) {
                }
            }
            if (holder == null) {
                holder = builder.createGroupHolder(view, groupPosition);
                view.setTag(holder);
            }
            if (holder != null) {
                holder.onBind(getGroup(groupPosition), isExpanded);
            }
        } catch (Exception e) {
            Log.e("HolderExpandableListAdapter", e.getMessage(), e);
        } finally {
            return view;
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        try {
            if (view == null) {
                view = builder.inflateChildView(context, groupPosition, childPosition);
            }
            HolderExpandable holder = null;
            Object tag = view.getTag();
            if (tag != null) {
                try {
                    holder = (HolderExpandable) tag;
                } catch (ClassCastException e) {
                }
            }
            if (holder == null) {
                holder = builder.createChildHolder(view, groupPosition, childPosition);
                view.setTag(holder);
            }
            if (holder != null) {
                holder.onBind(getChild(groupPosition, childPosition), isLastChild);
            }
        } catch (Exception e) {
            Log.e("HolderExpandableListAdapter", e.getMessage(), e);
        } finally {
            return view;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean add(G object) {
        return list.add(object);
    }

    public boolean addAll(Collection<? extends G> collection) {
        return list.addAll(collection);
    }

    public void addAll(G... items) {
        Collections.addAll(list, items);
    }

    public void add(int location, G object) {
        list.add(location, object);
    }

    public G remove(int location) {
        return list.remove(location);
    }

    public void sort(Comparator<? super G> comparator) {
        Collections.sort(list, comparator);
    }

    public void clear() {
        list.clear();
    }

    public boolean remove(Object object) {
        return list.remove(object);
    }

    public boolean removeAll(Collection<?> collection) {
        return list.removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return list.retainAll(collection);
    }

}
