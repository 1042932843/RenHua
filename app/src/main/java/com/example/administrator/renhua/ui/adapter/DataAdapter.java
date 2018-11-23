package com.example.administrator.renhua.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.Enclosure;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{

    private List<Enclosure> home;
    private Context context;


    public DataAdapter(List<Enclosure> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<Enclosure> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(home.get(position)==null){
            return;
        }
        holder.setIsRecyclable(false);
        holder.title.setText(home.get(position).getMaterials_name());
        holder.tip.setHint("注意："+home.get(position).getFilling_requirement());
        if(TextUtils.isEmpty(home.get(position).getEmpty_name())){
            holder.download.setVisibility(View.GONE);
        }else{
            String all="下载:"+home.get(position).getEmpty_name();
            SpannableString spannableString = new SpannableString(all);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(context.getResources().getColor(R.color.colorPrimary));  //设置下划线颜色
                    ds.setUnderlineText(true);  // 显示下划线
                }

                @Override
                public void onClick(View view) {     // TextView点击事件
                    Toast.makeText(context,"加入下载队列",Toast.LENGTH_SHORT).show();
                }
        }, 0, home.get(position).getEmpty_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.download.setVisibility(View.GONE);
        holder.download.setText(spannableString);
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"将"+home.get(position).getEmpty_name()+"加入了下载队列",Toast.LENGTH_SHORT).show();
                }
            });
        }
        //单独对应类型的设置事件
        if( onItemClickListener!= null){
            holder.upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
            holder. upload.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return home == null ? 0 : home.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,download;
        TextView tip,upload;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            tip = (TextView) itemView.findViewById(R.id.tip);
            download= (TextView) itemView.findViewById(R.id.download);
            upload=(TextView) itemView.findViewById(R.id.upload);
        }
    }

    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener=onItemClickListener;
    }
}

