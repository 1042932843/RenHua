package com.example.administrator.renhua.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.HtmlStr;
import com.example.administrator.renhua.ui.listener.OnScrollLastItemListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by lenovo on 2017/2/24.
 */
public class ConditionfFragment extends BaseFragment implements PtrHandler, OnScrollLastItemListener, AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String KEY_TITLE = "title";
    private ArrayList<String> items;
    private Button btn_submit;
    public RadioGroup rg;
    public CheckBox radioButton;
    public LinearLayout lin_layout;
    private TextView text;
    private String conditions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_handle, container, false);
        Bundle args = getArguments();
        if (args != null) {
            conditions = args.getString("conditions");
            Log.d("reg_ConditionFragment", "conditions:"+conditions);
        }
        assignViews(view);
        initViews();
        setConditions(conditions);

        //给CheckBox设置事件监听
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Log.d("reg", "选中");
                    EventBus.getDefault().post(1, "change_user");
                } else {
                    EventBus.getDefault().post(2, "change_user");
                }
            }
        });

        return view;


    }

    private void assignViews(View view) {
        rg = (RadioGroup) view.findViewById(R.id.rg);
        radioButton = (CheckBox) view.findViewById(R.id.perfect);
        lin_layout = (LinearLayout) view.findViewById(R.id.lin_layout);
        text = (TextView) view.findViewById(R.id.text);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
//        btn_submit.setVisibility(View.GONE);

    }

    private void initViews() {
        View[] views = {};
        for (View view : views) {
            view.setOnClickListener(this);
        }
//        pull.setPtrHandler(this);
//        pull.post(new Runnable() {
//            @Override
//            public void run() {
//                pull.autoRefresh();
//            }
//        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
        }
    }

    private void submit() {


    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onScrollLastItem(AbsListView view) {

    }

    private void setConditions(String conditions){
        if (items == null) {
            items = new ArrayList<String>();
        } else {
            items.clear();
        }
        if (null != conditions) {
            lin_layout.setVisibility(View.VISIBLE);
            radioButton.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
            String str = HtmlStr.delHTMLTag(conditions);
            if (str.equals("") || str == null) {
                rg.setVisibility(View.GONE);
                lin_layout.setVisibility(View.GONE);
                radioButton.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
            } else {
                rg.setVisibility(View.VISIBLE);
                lin_layout.setVisibility(View.VISIBLE);
                radioButton.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                text.setText(str);
                btn_submit.setVisibility(View.VISIBLE);
            }
        } else {

            lin_layout.setVisibility(View.GONE);
            radioButton.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);


        }
    }

}


