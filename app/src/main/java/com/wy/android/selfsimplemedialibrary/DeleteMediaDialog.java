package com.wy.android.selfsimplemedialibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

public class DeleteMediaDialog extends Dialog {
    private String title;
    private String buttonConfirm;
    private String buttonCancel;
    private View.OnClickListener confirmClickListener;
    private View.OnClickListener cancelClickListener;
    private TextView mDialog_title;

    public DeleteMediaDialog(Context context, String title,
                             String buttonConfirm,
                             View.OnClickListener confirmClickListener) {
        super(context, R.style.Dialog);
        this.title = title;
        this.buttonConfirm = buttonConfirm;
        this.confirmClickListener = confirmClickListener;
    }

    public DeleteMediaDialog(Context context,
                             String title,
                             String buttonConfirm,
                             View.OnClickListener confirmClickListener,
                             String buttonCancel) {
        super(context, R.style.Dialog);
        this.title = title;
        this.buttonConfirm = buttonConfirm;
        this.buttonCancel = buttonCancel;
        this.confirmClickListener = confirmClickListener;
    }

    public DeleteMediaDialog(Context context,
                             String title,
                             View.OnClickListener confirmClickListener,
                             String buttonConfirm,
                             String buttonCancel) {
        super(context, R.style.Dialog);
        this.title = title;
        this.buttonConfirm = buttonConfirm;
        this.buttonCancel = buttonCancel;
        this.confirmClickListener = confirmClickListener;
    }

    public DeleteMediaDialog(Context context,
                             String title,
                             View.OnClickListener confirmClickListener,
                             View.OnClickListener cancelClickListener,
                             String buttonConfirm,
                             String buttonCancel) {
        super(context, R.style.Dialog);
        this.title = title;
        this.buttonConfirm = buttonConfirm;
        this.buttonCancel = buttonCancel;
        this.confirmClickListener = confirmClickListener;
        this.cancelClickListener = cancelClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delete_media_dialog);
        mDialog_title = findViewById(R.id.tv_delete_dec);
        TextView dialog_confirm = findViewById(R.id.tv_delete_confirm);
        TextView dialog_cancel = findViewById(R.id.tv_delete_cancel);
        if (!TextUtils.isEmpty(title))
            mDialog_title.setText(title);
        if (!TextUtils.isEmpty(buttonConfirm))
            dialog_confirm.setText(buttonConfirm);
        if (!TextUtils.isEmpty(buttonCancel))
            dialog_cancel.setText(buttonCancel);

        if (null != confirmClickListener) {
            dialog_confirm.setOnClickListener(confirmClickListener);
        }
        if (null != cancelClickListener) {
            dialog_cancel.setOnClickListener(cancelClickListener);
        } else {
            dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteMediaDialog.this.dismiss();
                }
            });
        }


    }


    public void setTitle(String title) {
        this.title = title;
        mDialog_title.setText(title);
    }


    public void setCanotBackPress() {
        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP;
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void show() {
        Objects.requireNonNull(this.getWindow()).setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                , WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        fullScreenImmersive(getWindow().getDecorView());
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

    }
}
