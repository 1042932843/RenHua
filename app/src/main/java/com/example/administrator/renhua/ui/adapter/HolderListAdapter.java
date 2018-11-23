/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;


import com.example.administrator.renhua.kit.LogKit;
import com.example.administrator.renhua.ui.view.PinnedSectionListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 */
public class HolderListAdapter<T> extends BaseAdapter implements Filterable, ExpandableListAdapter, PinnedSectionListView.PinnedSectionListAdapter {

    List<T> list;
    List<T> filterList;
    HolderListFilter filter;
    HolderBuilder<T> builder;

    public HolderListAdapter(@NonNull HolderBuilder<T> builder) {
        this(builder, new ArrayList<T>());
    }

    public HolderListAdapter(@NonNull HolderBuilder<T> builder, @NonNull List<T> list) {
        this.builder = builder;
        if (list instanceof ArrayList) {
            this.list = list;
        } else {
            this.list = new ArrayList<>(list);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        try {
            T item = getItem(position);
            Holder<T> holder;
            if (view == null) {
                holder = builder.createHolder(position, item);
            } else {
                holder = (Holder<T>) view.getTag();
            }
            if (holder != null) {
                (view = holder.onBind(item, position).getView()).setTag(holder);
            }
        } catch (Exception e) {
            LogKit.e("HolderListAdapter", e);
        }
        return view;
    }

    @Override
    public int getCount() {
        if (filterList != null) {
            return filterList.size();
        }
        return list.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= 0) {
            if (filterList != null) {
                if (position < filterList.size()) {
                    return filterList.get(position);
                }
            } else if (position < list.size()) {
                return list.get(position);
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getList() {
        return list;
    }

    public List<T> getFilterList() {
        return filterList;
    }

    public void refresh(List<T> items) {
        if (list.size() > 0) {
            list.clear();
        }
        load(items);
    }

    public void load(List<T> items) {
        if (items != null && items.size() > 0) {
            list.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new HolderListFilter();
        }
        return filter;
    }

    @Override
    public int getViewTypeCount() {
        return builder.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return builder.getItemViewType(position, getItem(position));
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return builder.isItemViewTypePinned(viewType);
    }

    class HolderListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<T> values;
            if (constraint == null || constraint.length() == 0) {
                values = new ArrayList<>(list);
            } else {
                values = new ArrayList<>();
                for (T t : list) {
                    String l = String.valueOf(t);
                    String r = String.valueOf(constraint);
                    if (l != null && r != null) {
                        l = l.toLowerCase();
                        r = r.toLowerCase();
                        if (l.contains(r)) {
                            values.add(t);
                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = values;
            results.count = values.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterList = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public int getGroupCount() {
        return getCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return builder.getChildrenCount(groupPosition);
    }

    @Override
    public T getGroup(int groupPosition) {
        return getItem(groupPosition);
    }

    @Override
    public T getChild(int groupPosition, int childPosition) {
        return builder.getChild(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getItemId(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return getView(groupPosition, convertView, parent);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        try {
            T item = builder.getChild(groupPosition, childPosition);
            Holder<T> holder;
            if (view == null) {
                holder = builder.createHolder(childPosition, item);
            } else {
                holder = (Holder<T>) view.getTag();
            }
            if (holder != null) {
                (view = holder.onBind(item, childPosition).getView()).setTag(holder);
            }
        } catch (Exception e) {
            LogKit.e("HolderListAdapter", e);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return builder.isChildSelectable(groupPosition, childPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        builder.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        builder.onGroupCollapsed(groupPosition);
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return childId;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId;
    }

}
