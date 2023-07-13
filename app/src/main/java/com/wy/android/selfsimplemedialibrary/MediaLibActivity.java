package com.wy.android.selfsimplemedialibrary;

import android.Manifest;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.wy.android.selfsimplemedialibrary.databinding.ActivityMedialibLayoutBinding;
import com.wy.android.selfsimplemedialibrary.fragment.MediAllShowFragment;
import com.wy.android.selfsimplemedialibrary.fragment.MediaAudioFragment;
import com.wy.android.selfsimplemedialibrary.fragment.MediaPictureFragment;
import com.wy.android.selfsimplemedialibrary.fragment.MediaVideoFragment;

import me.jessyan.autosize.internal.CustomAdapt;

public class MediaLibActivity extends BaseActivity implements CustomAdapt {

    private static final int MY_PERMISSIONS_REQUEST_STORAGE_PERMISSIONS = 10;
    private ActivityMedialibLayoutBinding mBinding;
    private MediaVideoFragment mVideoFragment;
    private MediaPictureFragment mPictureFragment;
    private MediaAudioFragment mAudioFragment;
    private MediaViewModel mMediaViewModel;
    private MediAllShowFragment mMediAllShowFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_medialib_layout);
        checkoutPermission();
        initView();
        initData();
        initObserver();
        initListener();
        addFragment();
        // 屏幕保持不暗不关闭
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void addFragment() {
        mMediAllShowFragment = new MediAllShowFragment();
        replaceFragment(mMediAllShowFragment);
    }

    private void initListener() {

    }

    private void initObserver() {
        mMediaViewModel = ViewModelProviders.of(this).get(MediaViewModel.class);
        mMediaViewModel.onCilckEvent.observe(this, new Observer<MediaViewModel.ONCLICK>() {
            @Override
            public void onChanged(MediaViewModel.ONCLICK onclick) {
                switch (onclick) {
                    case ONCLICK_RETURN_MAIN_ACTIVITY: {
                        finish();
                    }
                    break;
                    case ONCLICK_RETURN_ALL_MEDIA_FRAGMENT: {
                        replaceFragment(mMediAllShowFragment);
                    }
                    break;
                    case ONCLICK_GO_VIDEO_FRAGMENT: {
                        if (mVideoFragment == null) {
                            mVideoFragment = new MediaVideoFragment();
                        }
                        replaceFragment(mVideoFragment);
                    }
                    break;
                    case ONCLICK_GO_AUDIO_FRAGMENT: {
                        if (mAudioFragment == null) {
                            mAudioFragment = new MediaAudioFragment();
                        }
                        replaceFragment(mAudioFragment);
                    }
                    break;
                    case ONCLICK_GO_PICTURE_FRAGMENT: {
                        if (mPictureFragment == null) {
                            mPictureFragment = new MediaPictureFragment();
                        }
                        replaceFragment(mPictureFragment);
                    }
                    break;
                }
            }
        });
    }

    private void initData() {

    }

    private void initView() {

    }

    //动态切换Fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_media_fragment_layout, fragment);
        transaction.commit();//事物的提交
    }

    private void checkoutPermission() {
        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_STORAGE_PERMISSIONS);
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        return 640;
    }
}
