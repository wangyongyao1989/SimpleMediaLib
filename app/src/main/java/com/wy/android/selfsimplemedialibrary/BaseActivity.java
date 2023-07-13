package com.wy.android.selfsimplemedialibrary;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import xcrash.XCrash;


public class BaseActivity extends AppCompatActivity {
    public RequestPermissionUtil requestPermissionUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        XCrash.init(this
                , new XCrash.InitParameters()
                        .setLogDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CarshLog")
                        .enableJavaCrashHandler()
                        .enableAnrCrashHandler()
                        .enableNativeCrashHandler());
//        CrashApphandler.getInstance().init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
