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
public class MaterialFragment extends BaseFragment implements PtrHandler, OnScrollLastItemListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private String material;
    //    private ListViewForScrollView listView;
    private ArrayList<String> items;
    //    private MaterialAdapter adapter;
//    private HashSet<String> ItemMaps;
    private Button btn_submit;
    private String titl;
    public RadioGroup rg;
    public CheckBox radioButton;
    public LinearLayout lin_layout;
    private TextView text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conditionf, container, false);

        Bundle args = getArguments();
        if (args != null) {
            material = args.getString("material");
        }
        assignViews(view);
        initViews();
        setMaterial(material);
        return view;
    }

    private void assignViews(View view) {
        rg = (RadioGroup) view.findViewById(R.id.rg);
        radioButton = (CheckBox) view.findViewById(R.id.perfect);
        lin_layout = (LinearLayout) view.findViewById(R.id.lin_layout);
        text = (TextView) view.findViewById(R.id.text);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
    }

    private void initViews() {
        View[] views = {btn_submit};
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
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void setMaterial(String material) {
        if (items == null) {
            items = new ArrayList<String>();
        } else {
            items.clear();
        }
        if (null != material) {
            lin_layout.setVisibility(View.VISIBLE);
            radioButton.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
            String str = HtmlStr.delHTMLTag(material);
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

    private void submit() {
        if (radioButton.isChecked()){
            Log.d("reg", "radioButton.isChecked()--->true");
            EventBus.getDefault().post(3, "change_user");
        }else {
            Log.d("reg", "radioButton.isChecked()--->false");
            EventBus.getDefault().post(4, "change_user");
        }

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

}


