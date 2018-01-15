package com.example.administrator.renhua.ui.activity;

import android.support.v7.app.AppCompatActivity;

public class QRCodeActivity extends AppCompatActivity {
//    private boolean isLightHaveOpen = false;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qrcode);
//        ButterKnife.bind(this);
//        CaptureFragment captureFragment = new CaptureFragment();
//        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_qrcamera);
//
//        captureFragment.setAnalyzeCallback(new CodeUtils.AnalyzeCallback() {
//            @Override
//            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//                Intent resultIntent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//                bundle.putString(CodeUtils.RESULT_STRING, result);
//                resultIntent.putExtras(bundle);
//                QRCodeActivity.this.setResult(RESULT_OK, resultIntent);
//                QRCodeActivity.this.finish();
//            }
//
//            @Override
//            public void onAnalyzeFailed() {
//                Intent resultIntent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//                bundle.putString(CodeUtils.RESULT_STRING, "");
//                resultIntent.putExtras(bundle);
//                QRCodeActivity.this.setResult(RESULT_OK, resultIntent);
//                QRCodeActivity.this.finish();
//            }
//        });
//
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
//    }
//
//    @OnClick(R.id.second_button1)
//    public void openlight() {
//
//
//        if(!isLightHaveOpen){
//            CodeUtils.isLightEnable(true);
//            isLightHaveOpen = true;
//        }else{
//            CodeUtils.isLightEnable(false);
//            isLightHaveOpen = false;
//        }
//
//
//    }
}
