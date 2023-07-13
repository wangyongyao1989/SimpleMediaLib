package com.wy.android.selfsimplemedialibrary.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mmrobot.mediafacer.MediaFacer;
import com.mmrobot.mediafacer.VideoGet;
import com.mmrobot.mediafacer.mediaHolders.VideoContent;
import com.mmrobot.mediafacer.mediaHolders.VideoFolderContent;
import com.wy.android.selfsimplemedialibrary.DeleteMediaDialog;
import com.wy.android.selfsimplemedialibrary.MediaViewModel;
import com.wy.android.selfsimplemedialibrary.R;
import com.wy.android.selfsimplemedialibrary.adapters.VideoRecycleAdapter;
import com.wy.android.selfsimplemedialibrary.databinding.MediaFragmentVideoLayoutBinding;


import java.util.LinkedList;
import java.util.Objects;

public class MediaVideoFragment extends Fragment {

    private static final String TAG = MediaVideoFragment.class.getSimpleName();
    private static final String MARK_STRING = "artist";

    private MediaFragmentVideoLayoutBinding mBinding;
    private RecyclerView mVideoRecycler;
    private LinkedList<VideoContent> allVideos;
    private LinkedList<VideoContent> markVideos;
    private LinkedList<VideoContent> selectedVideos;

    private View mVideoSelectAll;
    private LinearLayout mLlVideoSelectAll;
    private View mVideoSelectMark;
    private LinearLayout mLlVideoSelectMark;
    private RelativeLayout mRlVideoBulkOperation;
    private RelativeLayout mRlBulkOperation;
    private RelativeLayout mRlBulkOperationMark;
    private RelativeLayout mRlBulkOperationDelete;
    private RelativeLayout mRlBulkOperationAll;
    private ImageView mIvBulkOperationMark;
    private TextView mTvBulkOperationMark;

    private MARK_VIDEO MARKVIDEO = MARK_VIDEO.ALL;
    private BULK_OPERATION BULKOPERATION = BULK_OPERATION.BULK_UNSELECT;
    private BULK_MARK_OPERATION MARKOPERATION = BULK_MARK_OPERATION.BULK_MARK;
    private BULK_SELECT_OPERATION SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
    private RelativeLayout mRlBulkOperationTop;
    private FrameLayout mFlBack;
    private ImageView mIvTopBack;
    private TextView mTvTopTitle;
    private VideoRecycleAdapter mVideoAdapter;
    private ImageView mIvBulkOperationAll;
    private TextView mTvBulkOperationAll;
    private String deleteString = "是否删除2项⽂件？";
    private DeleteMediaDialog mDeleteMediaDialog;
    private MediaViewModel mMediaViewModel;


    private enum MARK_VIDEO {
        ALL,
        MARK
    }

    private enum BULK_OPERATION {
        BULK_SELECT,
        BULK_UNSELECT
    }

    private enum BULK_MARK_OPERATION {
        BULK_MARK,
        BULK_MARK_CANCEL
    }

    private enum BULK_SELECT_OPERATION {
        BULK_SELECT_ALL,
        BULK_SELECT_CANCEL
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.media_fragment_video_layout, container, false);
        initView();
        initObserver();
        initListener();
        initData();
        return mBinding.getRoot();
    }

    private void initData() {

    }

    private void initListener() {
        mDeleteMediaDialog = new DeleteMediaDialog(MediaVideoFragment.this.getActivity()
                , deleteString
                , null
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideos();
                mDeleteMediaDialog.dismiss();
            }
        });
        mDeleteMediaDialog.setCanotBackPress();
        mDeleteMediaDialog.setCanceledOnTouchOutside(true);

        mFlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BULKOPERATION == BULK_OPERATION.BULK_SELECT) {
                    BULKOPERATION = BULK_OPERATION.BULK_UNSELECT;
                    mRlBulkOperation.setVisibility(View.GONE);
                    mRlBulkOperationTop.setVisibility(View.VISIBLE);
                    mTvTopTitle.setText(getString(R.string.controller_medialib_selectpage_video));
                    mVideoAdapter.setBulkOperation(false);
                    getVideos();
                    mVideoSelectAll.setVisibility(View.VISIBLE);
                    mVideoSelectMark.setVisibility(View.INVISIBLE);
                    MARKVIDEO = MARK_VIDEO.ALL;
                    mVideoAdapter.setVideoListDataSetChanged(allVideos);
                    mIvTopBack.setSelected(BULKOPERATION == BULK_OPERATION.BULK_SELECT);
                    selectVideosCancel();
                    changeSelectedAllUI(false);
                    changeMarkUI(false);
                } else {
                    mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_RETURN_ALL_MEDIA_FRAGMENT);

                }
            }
        });

        mLlVideoSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MARKVIDEO == MARK_VIDEO.ALL) return;
                mVideoSelectAll.setVisibility(View.VISIBLE);
                mVideoSelectMark.setVisibility(View.INVISIBLE);
                MARKVIDEO = MARK_VIDEO.ALL;
                mVideoAdapter.setVideoListDataSetChanged(allVideos);
            }
        });

        mLlVideoSelectMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MARKVIDEO == MARK_VIDEO.MARK) return;
                mVideoSelectAll.setVisibility(View.INVISIBLE);
                mVideoSelectMark.setVisibility(View.VISIBLE);
                MARKVIDEO = MARK_VIDEO.MARK;
                mVideoAdapter.setVideoListDataSetChanged(markVideos);
            }
        });

        mRlVideoBulkOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMarkUI(MARKVIDEO == MARK_VIDEO.MARK);
                MARKOPERATION = MARKVIDEO == MARK_VIDEO.MARK
                        ? BULK_MARK_OPERATION.BULK_MARK_CANCEL
                        : BULK_MARK_OPERATION.BULK_MARK;
                mRlBulkOperation.setVisibility(View.VISIBLE);
                mRlBulkOperationTop.setVisibility(View.GONE);
                mIvTopBack.setSelected(true);
                BULKOPERATION = BULK_OPERATION.BULK_SELECT;
                mTvTopTitle.setText("已选择0项");
                mVideoAdapter.setBulkOperation(true);
            }
        });

        mRlBulkOperationMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedVideos.size() < 1) {
                    Toast.makeText(getActivity()
                            , R.string.controller_medialib_selectpage_bulk_select_file_toast
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                if (MARKOPERATION == BULK_MARK_OPERATION.BULK_MARK) {
                    markSelectedVideos();
                } else {
                    markSelectedVideosCancel();
                }
            }
        });

        mRlBulkOperationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedVideos.size() < 1) {
                    Toast.makeText(getActivity()
                            , R.string.controller_medialib_selectpage_bulk_select_file_toast
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                String text = String.format("是否删除%s项文件？",selectedVideos.size()+"");
                mDeleteMediaDialog.show();
                mDeleteMediaDialog.setTitle(text);
            }
        });

        mRlBulkOperationAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELECTOPERATION == BULK_SELECT_OPERATION.BULK_SELECT_CANCEL) {
                    allVideosSelect();
                    changeSelectedAllUI(true);
                    SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
                } else {
                    allVideosSelectCancel();
                    changeSelectedAllUI(false);
                    SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
                }
                judgeChangeMarkUI();
            }
        });


    }

    private void initObserver() {
        mMediaViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(MediaViewModel.class);

    }

    private void initView() {

        mFlBack = mBinding.flBack;
        mIvTopBack = mBinding.ivTopBack;
        mTvTopTitle = mBinding.tvTopTitle;

        mRlBulkOperationTop = mBinding.rlBulkOperationTop;
        mLlVideoSelectAll = mBinding.llVideoSelectAll;
        mVideoSelectAll = mBinding.viewVideoSelectAll;
        mLlVideoSelectMark = mBinding.llVideoSelectMark;
        mVideoSelectMark = mBinding.viewVideoSelectMark;
        mVideoSelectMark.setVisibility(View.INVISIBLE);

        mRlVideoBulkOperation = mBinding.rlVideoBulkOperation;
        mVideoRecycler = mBinding.videoRecycler;
        mVideoRecycler.hasFixedSize();
        mVideoRecycler.setHasFixedSize(true);
        mVideoRecycler.setItemViewCacheSize(20);
        mVideoRecycler.setDrawingCacheEnabled(true);
        mVideoRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mVideoRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        //底部控件
        mRlBulkOperation = mBinding.rlBulkOperation;
        mRlBulkOperation.setVisibility(View.GONE);
        mRlBulkOperationMark = mBinding.rlBulkOperationMark;
        mIvBulkOperationMark = mBinding.ivBulkOperationMark;
        mTvBulkOperationMark = mBinding.tvBulkOperationMark;
        mRlBulkOperationDelete = mBinding.rlBulkOperationDelete;

        mRlBulkOperationAll = mBinding.rlBulkOperationAll;
        mIvBulkOperationAll = mBinding.ivBulkOperationAll;
        mTvBulkOperationAll = mBinding.tvBulkOperationAll;

        allVideos = new LinkedList<>();
        markVideos = new LinkedList<>();
        selectedVideos = new LinkedList<>();
        getVideos();
        setVideoActionsListener();
    }

    private void getVideos() {
        final LinkedList<VideoFolderContent> videoFolders
                = new LinkedList<>(MediaFacer.withVideoContex(getActivity())
                .getAllVideoFolders(VideoGet.externalContentUri));
        allVideos.clear();
        markVideos.clear();
        for (int i = 0; i < videoFolders.size(); i++) {
            allVideos = videoFolders.get(i).getVideoFiles();
//            if (videoFolders.get(i).getFolderName().equals("SmartCar")) {
//                allVideos = videoFolders.get(i).getVideoFiles();
//            }
        }

        for (int i = 0; i < allVideos.size(); i++) {
            if (allVideos.get(i).getArtist().contains(MARK_STRING)) {
                markVideos.add(allVideos.get(i));
            }
        }
    }

    private void setVideoActionsListener() {
        VideoRecycleAdapter.videoActionListener actionListener = new VideoRecycleAdapter.videoActionListener() {
            @Override
            public void onVideoItemClicked(int position) {
                if (BULKOPERATION == BULK_OPERATION.BULK_UNSELECT) {
                    //play video
                    playVideo(position);
                } else {
                    selectVideoAction(position);
                }
            }
        };
        mVideoAdapter = new VideoRecycleAdapter(getActivity(), allVideos, actionListener);
        mVideoRecycler.setAdapter(mVideoAdapter);
    }


    private void selectVideosCancel() {
        if (selectedVideos == null || selectedVideos.size() < 1) return;
        for (int i = 0; i < selectedVideos.size(); i++) {
            selectedVideos.get(i).setVideoSelect(false);
        }
        selectedVideos.clear();
    }

    private void changeSelectedAllUI(boolean isAll) {
        mIvBulkOperationAll.setSelected(isAll);
        mTvBulkOperationAll.setText(isAll
                ? R.string.controller_medialib_selectpage_bulk_operation_all_cancel
                : R.string.controller_medialib_selectpage_bulk_operation_all);
    }

    //判断勾选的是否无标记项
    private boolean judgeMarkType() {
        if (selectedVideos.size() < 1) return true;
        for (int i = 0; i < selectedVideos.size(); i++) {
            boolean isMark = selectedVideos.get(i).getArtist().equals(MARK_STRING);
            if (!isMark) {
                return true;
            }
        }
        return false;
    }

    private void changeMarkUI(boolean isMark) {
        mIvBulkOperationMark.setSelected(isMark);
        mTvBulkOperationMark.setText(isMark
                ? R.string.controller_medialib_selectpage_bulk_operation_mark_cancel
                : R.string.controller_medialib_selectpage_mark);
    }


    private void allVideosSelectCancel() {
        selectedVideos.clear();
        if (MARKVIDEO == MARK_VIDEO.ALL) {
            for (int i = 0; i < allVideos.size(); i++) {
                allVideos.get(i).setVideoSelect(false);
            }
        } else {
            for (int i = 0; i < markVideos.size(); i++) {
                markVideos.get(i).setVideoSelect(false);
            }
        }
        mTvTopTitle.setText("已选择0项");
        mVideoAdapter.notifyDataSetChanged();
    }

    private void allVideosSelect() {
        String selectText;
        selectedVideos.clear();
        if (MARKVIDEO == MARK_VIDEO.ALL) {
            for (int i = 0; i < allVideos.size(); i++) {
                allVideos.get(i).setVideoSelect(true);
            }
            selectedVideos.addAll(allVideos);
            selectText = String.format("已选择%s项", allVideos.size() + "");
        } else {
            for (int i = 0; i < markVideos.size(); i++) {
                markVideos.get(i).setVideoSelect(true);
            }
            selectedVideos.addAll(markVideos);
            selectText = String.format("已选择%s项", markVideos.size() + "");
        }
        mTvTopTitle.setText(selectText);
        mVideoAdapter.notifyDataSetChanged();
    }

    private void selectVideoAction(int position) {
        mVideoAdapter.setVideoSelectNotify(position);
        if (MARKVIDEO == MARK_VIDEO.ALL) {
            VideoContent positionVideo = allVideos.get(position);
            if (positionVideo.isVideoSelect()) {
                selectedVideos.add(positionVideo);
            } else {
                selectedVideos.remove(positionVideo);
            }
            if (allVideos.size() == selectedVideos.size()) {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
            } else {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
            }
            changeSelectedAllUI(allVideos.size() == selectedVideos.size());
            judgeChangeMarkUI();
        } else {
            VideoContent positionVideo = markVideos.get(position);
            if (positionVideo.isVideoSelect()) {
                selectedVideos.add(positionVideo);
            } else {
                selectedVideos.remove(positionVideo);
            }
            if (markVideos.size() == selectedVideos.size()) {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
            } else {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
            }
            changeSelectedAllUI(markVideos.size() == selectedVideos.size());
        }
        String selectText = String.format("已选择%s项", selectedVideos.size() + "");
        mTvTopTitle.setText(selectText);
    }

    private void judgeChangeMarkUI() {
        boolean judgeMarkType = judgeMarkType();
        if (judgeMarkType) {
            changeMarkUI(false);
            MARKOPERATION = BULK_MARK_OPERATION.BULK_MARK;
        } else {
            changeMarkUI(true);
            MARKOPERATION = BULK_MARK_OPERATION.BULK_MARK_CANCEL;
        }
    }

    private void markSelectedVideos() {
        if (selectedVideos == null || selectedVideos.size() < 1) return;
        for (int i = 0; i < selectedVideos.size(); i++) {
            selectedVideos.get(i).setArtist(MARK_STRING);
            MediaFacer.withVideoContex(getContext())
                    .favoriteVideo(Uri.parse(selectedVideos.get(i).getVideoUri()), "artist");
        }
        allVideosSelectCancel();
    }

    private void markSelectedVideosCancel() {
        if (selectedVideos == null || selectedVideos.size() < 1) return;
        for (int i = 0; i < selectedVideos.size(); i++) {
            selectedVideos.get(i).setArtist("");
            MediaFacer.withVideoContex(getContext())
                    .favoriteVideo(Uri.parse(selectedVideos.get(i).getVideoUri()), "");
        }
        allVideosSelectCancel();
    }

    private void deleteVideos() {
        if (selectedVideos == null || selectedVideos.size() < 1) return;
        for (int i = 0; i < selectedVideos.size(); i++) {
            MediaFacer.withVideoContex(getContext())
                    .deleteVideo(Uri.parse(selectedVideos.get(i).getVideoUri()));
        }

        if (MARKVIDEO == MARK_VIDEO.ALL) {
            for (int i = 0; i < selectedVideos.size(); i++) {
                allVideos.remove(selectedVideos.get(i));
            }
        } else {
            for (int i = 0; i < selectedVideos.size(); i++) {
                allVideos.remove(selectedVideos.get(i));
            }
        }
        selectedVideos.clear();
        mVideoAdapter.notifyDataSetChanged();
    }

    private void playVideo(int position) {
        MediaVideoPlayerFragment playerFragment = new MediaVideoPlayerFragment();
        playerFragment.setVideosData(allVideos, position);

        Transition transition = TransitionInflater.from(getActivity()).
                inflateTransition(android.R.transition.explode);

        playerFragment.setEnterTransition(transition);
        playerFragment.setExitTransition(transition);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.play_holder, playerFragment)
                .addToBackStack(null)
                .commit();
    }


}
