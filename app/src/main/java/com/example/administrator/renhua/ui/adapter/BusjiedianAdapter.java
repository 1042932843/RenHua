package com.example.administrator.renhua.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.entity.BusItem;
import com.example.administrator.renhua.entity.business;

import java.util.List;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/4/17
 * @DESCRIPTION:
 */
public class BusjiedianAdapter extends RecyclerView.Adapter<BusjiedianAdapter.ViewHolder>{

    private List<business> home;
    private Context context;


    public BusjiedianAdapter(List<business> home, Context context) {
        this.home = home;
        this.context=context;
    }

    public void updateData(List<business> home) {
        this.home.addAll(home);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_item, parent, false);
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

        holder.SBXMMC.setText(home.get(position).getTime());

        holder.SBLSH.setText(home.get(position).getHuanjie());

        holder.BJJGMS.setText(home.get(position).getState());


        //单独对应类型的设置事件
        if( onItemClickListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
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

        TextView SBXMMC,SBLSH;
        TextView BJJGMS;

        public ViewHolder(View itemView) {
            super(itemView);
            SBXMMC  = (TextView) itemView.findViewById(R.id.name);
            SBLSH  = (TextView) itemView.findViewById(R.id.sblash);
            BJJGMS= (TextView) itemView.findViewById(R.id.result);

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

