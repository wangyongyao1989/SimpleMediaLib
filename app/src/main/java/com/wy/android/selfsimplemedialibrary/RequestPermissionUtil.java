package com.wy.android.selfsimplemedialibrary;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.File;

/**
 * Created by Jean on 2016/7/21.
 */
public class RequestPermissionUtil {

    public int WRITE_EXTERNAL_STORAGE = 100;
    public int ACCESS_COARSE_LOCATION = 200;

    public void requestWritePermission(Activity _activity) {
        ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
    }

    public void requestWifiPermission(Activity _activity) {
        ActivityCompat.requestPermissions(_activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION);
    }

    /**
     * CreateSDCardDir
     */
    public void createSDCardDir(String path) {
        File sdcardDir = Environment.getExternalStorageDirectory();
        String pathcat = sdcardDir.getPath() + path;
        File path1 = new File(pathcat);
        if (!path1.exists()) {
            path1.mkdirs();
        }

    }
}
