package com.wy.android.selfsimplemedialibrary.fragment;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mmrobot.mediafacer.MediaDataCalculator;
import com.mmrobot.mediafacer.mediaHolders.VideoContent;
import com.wy.android.selfsimplemedialibrary.Constants;
import com.wy.android.selfsimplemedialibrary.DeleteMediaDialog;
import com.wy.android.selfsimplemedialibrary.MMDateUtil;
import com.wy.android.selfsimplemedialibrary.MediaViewModel;
import com.wy.android.selfsimplemedialibrary.R;
import com.wy.android.selfsimplemedialibrary.databinding.MeidaVideoPlayLayoutBinding;


import java.util.LinkedList;
import java.util.Objects;

public class MediaVideoPlayerFragment extends Fragment {

    private static final String TAG = MediaVideoPlayerFragment.class.getSimpleName();
    private VideoContent mVideoContent;
    private Handler mHandler;
    private Runnable mSeekbarPositionUpdateTask;
    private MeidaVideoPlayLayoutBinding mBinding;
    private FrameLayout mFlBack;
    private TextView mTvVideoTitle;
    private ImageView mIvVideoMark;
    private ImageView mIvVideoDelete;
    private ImageView mIvVideoInfo;
    private VideoView mVidZone;
    private SeekBar mSeekerPlay;
    private ImageView mIvVideoPlayStatus;
    private TextView mTvVideoPlayTime;
    private ImageView mIvVideoVolStatus;
    private View mPlayCover;
    private RelativeLayout mRlVideoTop;
    private RelativeLayout mRlPlayBottom;
    private STATUS_DISPLAY mStatusDisplay = STATUS_DISPLAY.SHOW_STATUS;
    private TextView mTvVideoTotalTime;
    private MediaViewModel mMediaViewModel;
    private DeleteMediaDialog mDeleteMediaDialog;


    public MediaVideoPlayerFragment(MediaViewModel mediaViewModel) {
        mMediaViewModel = mediaViewModel;
    }

    enum STATUS_DISPLAY {
        SHOW_STATUS,
        HIDE_STATUS,
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.meida_video_play_layout, container, false);
        initView();
        initObserver();
        initListener();
        initData();
        return mBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        String videoContentPath = mVideoContent.getPath();
        String fileLastModifiedTime = MMDateUtil.getFileLastModifiedTime(videoContentPath);
        mTvVideoTitle.setText(fileLastModifiedTime);
        mTvVideoTotalTime.setText(MediaDataCalculator.convertDuration(mVideoContent.getVideoDuration()) + "/");
        mVidZone.setVideoURI(Uri.parse(mVideoContent.getVideoUri()));
        mVidZone.requestFocus();
        mSeekerPlay.setMax((int) mVideoContent.getVideoDuration());
        mIvVideoMark.setSelected(mVideoContent.getArtist().equals(Constants.MARK_STRING));
    }

    private void initListener() {

        mFlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaViewModel != null) {
                    mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_RETURN_VIDEO_FRAGMENT);
                }
            }
        });

        mIvVideoMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = mIvVideoMark.isSelected();
                mIvVideoMark.setSelected(!selected);
                mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_VIDEO_PLAY_MARK);
            }
        });

        mIvVideoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteMediaDialog == null) {
                    String deleteString = "是否删除该项⽂件？";
                    mDeleteMediaDialog = new DeleteMediaDialog(MediaVideoPlayerFragment.this.getActivity()
                            , deleteString
                            , null
                            , new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_VIDEO_PLAY_DELETE);
                            mDeleteMediaDialog.dismiss();
                        }
                    });
                }
                mDeleteMediaDialog.setCanotBackPress();
                mDeleteMediaDialog.setCanceledOnTouchOutside(true);
                mDeleteMediaDialog.show();
            }
        });

        mIvVideoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mPlayCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatusDisplay == STATUS_DISPLAY.SHOW_STATUS) {
                    mRlVideoTop.setVisibility(View.GONE);
                    mRlPlayBottom.setVisibility(View.GONE);
                    mStatusDisplay = STATUS_DISPLAY.HIDE_STATUS;
                } else {
                    mRlVideoTop.setVisibility(View.VISIBLE);
                    mRlPlayBottom.setVisibility(View.VISIBLE);
                    mStatusDisplay = STATUS_DISPLAY.SHOW_STATUS;
                }
            }
        });

        mIvVideoPlayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVidZone.isPlaying()) {
                    mVidZone.pause();
                    mIvVideoPlayStatus.setSelected(true);
                } else {
                    mVidZone.start();
                    startUpdatingCallbackWithPosition();
                    mIvVideoPlayStatus.setSelected(false);
                }
            }
        });

        mVidZone.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVidZone.stopPlayback();
                stopUpdatingCallbackWithPosition();
            }
        });

        mSeekerPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userSelectedPosition = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    userSelectedPosition = progress;
                }
                mTvVideoPlayTime.setText(MediaDataCalculator.milliSecondsToTimer(progress));
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVidZone.seekTo(userSelectedPosition);
            }
        });

        mIvVideoVolStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initObserver() {
        mMediaViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(MediaViewModel.class);
    }

    private void initView() {
        mRlVideoTop = mBinding.rlVideoTop;
        mFlBack = mBinding.flBack;
        mTvVideoTitle = mBinding.tvVideoTitle;
        mIvVideoMark = mBinding.ivVideoMark;
        mIvVideoDelete = mBinding.ivVideoDelete;
        mIvVideoInfo = mBinding.ivVideoInfo;

        mVidZone = mBinding.vidZone;
        mPlayCover = mBinding.playCover;

        mRlPlayBottom = mBinding.rlPlayBottom;
        mIvVideoPlayStatus = mBinding.ivVideoPlayStatus;
        mSeekerPlay = mBinding.seekerPlay;
        mTvVideoPlayTime = mBinding.tvVideoPlayTime;
        mTvVideoTotalTime = mBinding.tvVideoTotalTime;
        mIvVideoVolStatus = mBinding.ivVideoVolStatus;
        mIvVideoPlayStatus.setSelected(true);
    }


    public void setVideosData(VideoContent videos) {
        this.mVideoContent = videos;
    }

    private void startUpdatingCallbackWithPosition() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updateProgressCallbackTask();
                    mHandler.postDelayed(this, 1000);
                }
            };
            mHandler.post(mSeekbarPositionUpdateTask);
        }
    }

    private void stopUpdatingCallbackWithPosition() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mSeekbarPositionUpdateTask);
            mHandler = null;
            mSeekbarPositionUpdateTask = null;
            mSeekerPlay.setProgress(0);
            mIvVideoPlayStatus.setSelected(false);
        }
    }

    private void updateProgressCallbackTask() {
        if (mVidZone != null) {
            int currentPosition = mVidZone.getCurrentPosition();
            mSeekerPlay.setProgress(currentPosition);
        }
    }

}
