package com.wy.android.selfsimplemedialibrary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wy.android.selfsimplemedialibrary.MediaViewModel;
import com.wy.android.selfsimplemedialibrary.R;
import com.wy.android.selfsimplemedialibrary.databinding.MediaAllShowLayoutBinding;

import java.util.Objects;

public class MediAllShowFragment extends Fragment {

    private MediaAllShowLayoutBinding mBinding;
    private FrameLayout mFlBack;
    private RelativeLayout mAudioBut;
    private RelativeLayout mPictureBut;
    private RelativeLayout mVideoBut;
    private MediaViewModel mMediaViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.media_all_show_layout, container, false);
        initView();
        initObserver();
        initListener();
        initData();
        return mBinding.getRoot();
    }

    private void initData() {

    }

    private void initListener() {
        mFlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_RETURN_MAIN_ACTIVITY);
            }
        });
        mAudioBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_GO_AUDIO_FRAGMENT);
            }
        });

        mPictureBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_GO_PICTURE_FRAGMENT);
            }
        });

        mVideoBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_GO_VIDEO_FRAGMENT);
            }
        });
    }

    private void initObserver() {
        mMediaViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(MediaViewModel.class);
    }

    private void initView() {
        mFlBack = mBinding.flBack;
        mAudioBut = mBinding.audioBut;
        mPictureBut = mBinding.pictureBut;
        mVideoBut = mBinding.videoBut;
    }


}
