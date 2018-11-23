

package com.example.administrator.renhua.kit;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;


import com.example.administrator.renhua.R;
import com.example.administrator.renhua.ui.adapter.Holder;
import com.example.administrator.renhua.ui.adapter.HolderBuilder;
import com.example.administrator.renhua.ui.adapter.HolderListAdapter;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.OnClick;

public class SheetKit extends HolderBuilder<String> {

    AlertDialog dialog;
    Context context;
    HolderListAdapter<String> adapter;
    DialogInterface.OnClickListener onClickListener;
    DialogInterface.OnCancelListener onCancelListener;

    public SheetKit(Context context) {
        context(context).adapter(new HolderListAdapter<>(this));
        Log.d("reg", "SheetKit.adapter");
    }

    SheetKit context(Context context) {
        this.context = context;
        return this;
    }

    SheetKit adapter(HolderListAdapter<String> adapter) {
        this.adapter = adapter;
        return this;
    }

    public AlertDialog dialog() {
        if (dialog == null && context != null) {
            synchronized (this) {
                if (dialog == null && context != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppSheet);
                    builder.setAdapter(adapter, null);
                    builder.setOnCancelListener(onCancelListener);
                    builder.setCancelable(onCancelListener != null);
                    dialog = builder.create();

                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);

                    ListView listView = dialog.getListView();
                    listView.setPadding(0, 0, 0, 0);
                    listView.setDivider(new ColorDrawable(0xffdddddd));
                    listView.setDividerHeight(1);
                    listView.requestLayout();
                }
            }
        }
        return dialog;
    }

    public SheetKit show() {
        AlertDialog dialog = dialog();
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
        return this;
    }

    public SheetKit dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        return this;
    }

    public SheetKit items(String... items) {
        if (items != null && items.length > 0) {
            adapter.refresh(Arrays.asList(items));
        }
        return this;
    }

    public SheetKit listener(DialogInterface.OnClickListener listener) {
        onClickListener = listener;
        return this;
    }

    public SheetKit listener(DialogInterface.OnCancelListener listener) {
        onCancelListener = listener;
        return this;
    }

    @Override
    public Holder<String> createHolder(int position, String item) {
        return new SheetItemHolder();
    }

    class SheetItemHolder extends Holder<String> {

        @Bind(R.id.sheetItem)
        TextView sheetItem;

        public SheetItemHolder() {
            super(context, R.layout.layout_sheet_item);
        }

        @Override
        public Holder onBind(String item, int position) {
            if (StringKit.isEmpty(item)) {
                sheetItem.setVisibility(View.GONE);
            } else {
                sheetItem.setVisibility(View.VISIBLE);
            }
            sheetItem.setText(item);
            return super.onBind(item, position);
        }

        @OnClick(R.id.sheetItem)
        void onSheetItemClick() {
            dismiss();
            if (onClickListener != null) {
                onClickListener.onClick(dialog, getPosition());
            }
        }

    }

}
