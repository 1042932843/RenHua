package com.example.administrator.renhua.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.renhua.App;
import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.entity.UploadFiles;
import com.example.administrator.renhua.ui.activity.UploadFilesActivity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class UploadFilesAdapter extends BaseAdapter implements View.OnClickListener{

    private Context context;
    private ArrayList<UploadFiles> items;
    private LayoutInflater layoutInflater;
    private MyCallBack myCallBack;
    public interface MyCallBack{
        public void click(View v);
    }

    public UploadFilesAdapter(Context context, MyCallBack mCallBack) {
        this.context = context;
        this.items = new ArrayList<UploadFiles>();
        layoutInflater = LayoutInflater.from(context);
        myCallBack = mCallBack;
    }

    public void setItems(ArrayList<UploadFiles> items) {
        this.items = items;
        if (items == null) {
            Log.d("reg", "list为空");
            this.items = new ArrayList<>();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final UploadFiles uploadFiles = items.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_upload_files_item, null);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mIsNeed = (TextView) convertView.findViewById(R.id.is_need);
            holder.mDocName = (TextView) convertView.findViewById(R.id.doc_name);
            holder.mUpload = (ImageView) convertView.findViewById(R.id.upload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(position + 1 + "：" + uploadFiles.getName());
        if (uploadFiles.getIsNeed().equals("0")) {
            holder.mIsNeed.setText("否");
        } else {
            holder.mIsNeed.setText("是");
        }
        holder.mUpload.setOnClickListener(this);
        holder.mUpload.setTag(position);
        holder.mDocName.setTag(position);
        if (items.get(position).getFileName() == null){
        }else {
            holder.mDocName.setText(items.get(position).getFileName());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            ((UploadFilesActivity) context).startActivityForResult(Intent.createChooser(intent, "请选择要上传的文件"), Constant.FILE_SELECT_CODE);
        } catch (ActivityNotFoundException e) {
            App.me().toast("请安装文件管理器");
        }
    }

    @Override
    public UploadFiles getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView mName, mIsNeed, mDocName;
        ImageView mUpload;
    }

    public void clear() {
        items.clear();
    }

    public boolean addAll(Collection<? extends UploadFiles> collection) {
        boolean pa = items.addAll(collection);
        return pa;
    }

    @Override
    public void onClick(View v) {
        myCallBack.click(v);
        showFileChooser();
    }

    public boolean add(UploadFiles object) {
        return items.add(object);
    }

}
