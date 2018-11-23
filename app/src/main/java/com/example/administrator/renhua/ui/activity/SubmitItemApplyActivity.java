package com.example.administrator.renhua.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.renhua.R;
import com.example.administrator.renhua.common.ApiMsg;
import com.example.administrator.renhua.common.Constant;
import com.example.administrator.renhua.utils.AsyncHttpDialog;
import com.lidroid.xutils.http.client.HttpRequest;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class SubmitItemApplyActivity extends BaseActivity {

    @Bind(R.id.result)
    TextView mResult;
    @Bind(R.id.content)
    TextView mContent;

    String itemApplyId, name, callTime, serviceCode, applyUserName;
    private SubmitResult submitResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_item_apply);
        ButterKnife.bind(this);
        itemApplyId = getIntent().getStringExtra("itemApplyId");
        name = getIntent().getStringExtra("name");
        callTime = getIntent().getStringExtra("callTime");
        serviceCode = getIntent().getStringExtra("serviceCode");
        applyUserName = getIntent().getStringExtra("applyUserName");
        submitResult = new SubmitResult(this);
        submitResult.getResult(itemApplyId);
    }

    private class SubmitResult extends AsyncHttpDialog {
        public SubmitResult(Context context) {
            super(context);
        }

        private void getResult(String itemApplyId) {
            post(HttpRequest.HttpMethod.POST, Constant.WT_DOMAIN + "itemApplyServer/submitItemApply",
                    "itemApplyId", itemApplyId);
        }

        @Override
        public void onSuccess(ApiMsg apiMsg) {
            super.onSuccess(apiMsg);
            if (apiMsg.getStatus().equals("1")) {
                mResult.setText("申请成功");
                mContent.setText("尊敬的"+applyUserName+":\n"+"您"+callTime+"在线申办的"+"【"+name+"】"+"已提交处理，业务流水号为"
                        +"【"+serviceCode+"】，"+"业务等待受理。请留意短信提醒或在网上办事大厅查询办事进度。");
            }else {
                mResult.setText("申请失败");
            }
        }
    }
}
