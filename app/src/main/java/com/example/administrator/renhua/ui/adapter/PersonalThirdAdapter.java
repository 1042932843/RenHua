package com.example.administrator.renhua.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.activity.GovernmentForthListActivity;
import com.example.administrator.renhua.utils.HanziToPinyin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/3 0003.
 */

public class PersonalThirdAdapter extends RecyclerView.Adapter<PersonalThirdAdapter.ViewHolder> {

    LayoutInflater inflate;
    Context context;
    ArrayList<HashMap<String, String>> thirdTitleList;

    public PersonalThirdAdapter(Context context, ArrayList<HashMap<String, String>> thirdTitleList) {
        this.context = context;
        this.thirdTitleList = thirdTitleList;
        this.inflate = LayoutInflater.from(context);
    }

    //RecyclerView显示的子View
    //该方法返回是ViewHolder，当有可复用View时，就不再调用
    @Override
    public PersonalThirdAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflate.inflate(R.layout.layout_government_item, null);
        return new PersonalThirdAdapter.ViewHolder(v);
    }

    //将数据绑定到子View
    @Override
    public void onBindViewHolder(PersonalThirdAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.textView.setText(thirdTitleList.get(i).get("name"));//这里显示中文名

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        viewHolder.imageView.setLayoutParams(lp);
        Log.d("reg", "enName:" + thirdTitleList.get(i).get("name"));

        viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int[] backg =new int[]{R.drawable.circle_shape_purple, R.drawable.circle_shape_orange, R.drawable.circle_shape_green1,
                R.drawable.circle_shape_red, R.drawable.circle_shape_red2, R.drawable.circle_shape_green2,
                R.drawable.circle_shape_blue1, R.drawable.circle_shape_blue2};
        int c=(i+1)%8;
        Log.d("reg", "itemId:" + i);
        Log.d("reg", "c:" + c);



        if(c==0){
            viewHolder.imageView.setBackgroundResource(backg[7]);
        }else {
            viewHolder.imageView.setBackgroundResource(backg[c-1]);
        }
        String str = thirdTitleList.get(i).get("name");
        String str2 =  HanziToPinyin.trans2PinYin(str);
        Log.d("reg","str2:"+str2);

//        Log.d("regg","str2:"+str2);
//           String string =  str2.substring(0,str.length());
       String string =  str2.substring(0,str2.length()-1);
        Log.d("reg","string:"+string);

//        String str2 = PinyinHelper.convertToPinyinString(str, "_", PinyinFormat.WITHOUT_TONE);

        int ID = context.getResources().getIdentifier(string, "drawable", "com.example.administrator.renhua");
        viewHolder.imageView.setImageResource(ID);

        viewHolder.governmentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GovernmentForthListActivity.class);
                intent.putExtra("title", thirdTitleList.get(i).get("name"));
                intent.putExtra("typeCode", thirdTitleList.get(i).get("typeCode"));
                intent.putExtra("flag", "type");
                context.startActivity(intent);
            }
        });
    }

    //RecyclerView显示数据条数
    @Override
    public int getItemCount() {
        Log.d("reg", "personalTitlesList.size():" + thirdTitleList.size());
        return thirdTitleList.size();
    }

    //自定义的ViewHolder,减少findViewById调用次数
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        LinearLayout governmentItem;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_name);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            governmentItem = (LinearLayout) itemView.findViewById(R.id.government_item);
        }
    }
}
