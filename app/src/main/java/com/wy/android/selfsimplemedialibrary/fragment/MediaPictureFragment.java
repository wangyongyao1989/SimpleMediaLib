package com.wy.android.selfsimplemedialibrary.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Gravity;
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
import com.mmrobot.mediafacer.mediaHolders.PictureContent;
import com.mmrobot.mediafacer.mediaHolders.PictureFolderContent;
import com.wy.android.selfsimplemedialibrary.DeleteMediaDialog;
import com.wy.android.selfsimplemedialibrary.MediaViewModel;
import com.wy.android.selfsimplemedialibrary.R;
import com.wy.android.selfsimplemedialibrary.adapters.PictureRecycleAdapter;
import com.wy.android.selfsimplemedialibrary.databinding.MediaFragmentPictureLayoutBinding;


import java.util.LinkedList;
import java.util.Objects;

public class MediaPictureFragment extends Fragment {

    private static final String TAG = MediaPictureFragment.class.getSimpleName();
    private MediaFragmentPictureLayoutBinding mBinding;
    private static final String MARK_STRING = "artist";

    private RecyclerView mPicRecycler;
    private PictureRecycleAdapter mPicAdapter;
    private LinkedList<PictureContent> allPictures;
    private LinkedList<PictureContent> markPictures;
    private LinkedList<PictureContent> selectedPictures;

    private View mPictureSelectAll;
    private LinearLayout mLlPictureSelectAll;
    private View mPictureSelectMark;
    private LinearLayout mLlPictureSelectMark;
    private RelativeLayout mRlPictureBulkOperation;
    private RelativeLayout mRlBulkOperation;
    private RelativeLayout mRlBulkOperationMark;
    private RelativeLayout mRlBulkOperationDelete;
    private RelativeLayout mRlBulkOperationAll;
    private ImageView mIvBulkOperationMark;
    private TextView mTvBulkOperationMark;

    private MARK_PICTURE MARKPICTURE = MARK_PICTURE.ALL;
    private BULK_OPERATION BULKOPERATION = BULK_OPERATION.BULK_UNSELECT;
    private BULK_MARK_OPERATION MARKOPERATION = BULK_MARK_OPERATION.BULK_MARK;
    private BULK_SELECT_OPERATION SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
    private RelativeLayout mRlBulkOperationTop;
    private FrameLayout mFlBack;
    private ImageView mIvTopBack;
    private TextView mTvTopTitle;
    private ImageView mIvBulkOperationAll;
    private TextView mTvBulkOperationAll;
    private String deleteString = "是否删除2项⽂件？";
    private DeleteMediaDialog mDeleteMediaDialog;
    private MediaViewModel mMediaViewModel;

    private enum MARK_PICTURE {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.media_fragment_picture_layout, container, false);
        initView();
        initObserver();
        initListener();
        initData();
        return mBinding.getRoot();
    }

    private void initData() {

    }

    private void initListener() {
        mDeleteMediaDialog = new DeleteMediaDialog(MediaPictureFragment.this.getActivity()
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
                    mPicAdapter.setBulkOperation(false);
                    getPictures();
                    mPictureSelectAll.setVisibility(View.VISIBLE);
                    mPictureSelectMark.setVisibility(View.INVISIBLE);
                    MARKPICTURE = MARK_PICTURE.ALL;
                    mPicAdapter.setPictureListDataSetChanged(allPictures);
                    mIvTopBack.setSelected(BULKOPERATION == BULK_OPERATION.BULK_SELECT);
                    selectVideosCancel();
                    changeSelectedAllUI(false);
                    changeMarkUI(false);
                } else {
                    mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_RETURN_ALL_MEDIA_FRAGMENT);

                }
            }
        });

        mLlPictureSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MARKPICTURE == MARK_PICTURE.ALL) return;
                mPictureSelectAll.setVisibility(View.VISIBLE);
                mPictureSelectMark.setVisibility(View.INVISIBLE);
                MARKPICTURE = MARK_PICTURE.ALL;
                mPicAdapter.setPictureListDataSetChanged(allPictures);
            }
        });

        mLlPictureSelectMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MARKPICTURE == MARK_PICTURE.MARK) return;
                mPictureSelectAll.setVisibility(View.INVISIBLE);
                mPictureSelectMark.setVisibility(View.VISIBLE);
                MARKPICTURE = MARK_PICTURE.MARK;
                mPicAdapter.setPictureListDataSetChanged(markPictures);
            }
        });

        mRlPictureBulkOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMarkUI(MARKPICTURE == MARK_PICTURE.MARK);
                MARKOPERATION = MARKPICTURE == MARK_PICTURE.MARK
                        ? BULK_MARK_OPERATION.BULK_MARK_CANCEL
                        : BULK_MARK_OPERATION.BULK_MARK;
                mRlBulkOperation.setVisibility(View.VISIBLE);
                mRlBulkOperationTop.setVisibility(View.GONE);
                mIvTopBack.setSelected(true);
                BULKOPERATION = BULK_OPERATION.BULK_SELECT;
                mTvTopTitle.setText("已选择0项");
                mPicAdapter.setBulkOperation(true);
            }
        });

        mRlBulkOperationMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPictures.size() < 1) {
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
                if (selectedPictures.size() < 1) {
                    Toast.makeText(getActivity()
                            , R.string.controller_medialib_selectpage_bulk_select_file_toast
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                String text = String.format("是否删除%s项文件？", selectedPictures.size() + "");
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
        mLlPictureSelectAll = mBinding.llPicSelectAll;
        mPictureSelectAll = mBinding.viewPicSelectAll;
        mLlPictureSelectMark = mBinding.llVideoSelectMark;
        mPictureSelectMark = mBinding.viewPicSelectMark;
        mPictureSelectMark.setVisibility(View.INVISIBLE);

        mRlPictureBulkOperation = mBinding.rlPicBulkOperation;
        mPicRecycler = mBinding.imageRecycler;
        mPicRecycler.hasFixedSize();
        mPicRecycler.setHasFixedSize(true);
        mPicRecycler.setItemViewCacheSize(20);
        mPicRecycler.setDrawingCacheEnabled(true);
        mPicRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mPicRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

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

        allPictures = new LinkedList<>();
        markPictures = new LinkedList<>();
        selectedPictures = new LinkedList<>();
        getPictures();
        setVideoActionsListener();
    }

    private void setVideoActionsListener() {
        PictureRecycleAdapter.pictureActionListrener actionListener = new PictureRecycleAdapter.pictureActionListrener() {
            @Override
            public void onPictureItemClicked(int position) {
                //show picture information
                if (BULKOPERATION == BULK_OPERATION.BULK_UNSELECT) {
                    //play video
                    displayPictureInFragment(allPictures, position);
                } else {
                    selectVideoAction(position);
                }

            }
        };
        mPicAdapter = new PictureRecycleAdapter(getActivity(), allPictures, actionListener);
        mPicRecycler.setAdapter(mPicAdapter);
    }

    private void getPictures() {
        final LinkedList<PictureFolderContent> pictureFolder
                = new LinkedList<>(MediaFacer.withPictureContex(getActivity())
                .getAllPictureFolders());
        allPictures.clear();
        markPictures.clear();
        for (int i = 0; i < pictureFolder.size(); i++) {
            allPictures = pictureFolder.get(i).getPhotos();
//            if (pictureFolder.get(i).getFolderName().equals("SmartCar")) {
//                allPictures = pictureFolder.get(i).getPhotos();
//            }
        }

        for (int i = 0; i < allPictures.size(); i++) {
            if (allPictures.get(i).getArtist() != null
                    && allPictures.get(i).getArtist().contains(MARK_STRING)) {
                markPictures.add(allPictures.get(i));
            }
        }
    }

    private void selectVideosCancel() {
        if (selectedPictures == null || selectedPictures.size() < 1) return;
        for (int i = 0; i < selectedPictures.size(); i++) {
            selectedPictures.get(i).setVideoSelect(false);
        }
        selectedPictures.clear();
    }

    private void changeSelectedAllUI(boolean isAll) {
        mIvBulkOperationAll.setSelected(isAll);
        mTvBulkOperationAll.setText(isAll
                ? R.string.controller_medialib_selectpage_bulk_operation_all_cancel
                : R.string.controller_medialib_selectpage_bulk_operation_all);
    }

    //判断勾选的是否无标记项
    private boolean judgeMarkType() {
        if (selectedPictures.size() < 1) return true;
        for (int i = 0; i < selectedPictures.size(); i++) {
            boolean isMark = selectedPictures.get(i).getArtist() != null
                    && selectedPictures.get(i).getArtist().equals(MARK_STRING);
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
        selectedPictures.clear();
        if (MARKPICTURE == MARK_PICTURE.ALL) {
            for (int i = 0; i < allPictures.size(); i++) {
                allPictures.get(i).setVideoSelect(false);
            }
        } else {
            for (int i = 0; i < markPictures.size(); i++) {
                markPictures.get(i).setVideoSelect(false);
            }
        }
        mTvTopTitle.setText("已选择0项");
        mPicAdapter.notifyDataSetChanged();
    }

    private void allVideosSelect() {
        String selectText;
        selectedPictures.clear();
        if (MARKPICTURE == MARK_PICTURE.ALL) {
            for (int i = 0; i < allPictures.size(); i++) {
                allPictures.get(i).setVideoSelect(true);
            }
            selectedPictures.addAll(allPictures);
            selectText = String.format("已选择%s项", allPictures.size() + "");
        } else {
            for (int i = 0; i < markPictures.size(); i++) {
                markPictures.get(i).setVideoSelect(true);
            }
            selectedPictures.addAll(markPictures);
            selectText = String.format("已选择%s项", markPictures.size() + "");
        }
        mTvTopTitle.setText(selectText);
        mPicAdapter.notifyDataSetChanged();
    }

    private void selectVideoAction(int position) {
        mPicAdapter.setPictureSelectNotify(position);
        if (MARKPICTURE == MARK_PICTURE.ALL) {
            PictureContent positionVideo = allPictures.get(position);
            if (positionVideo.isVideoSelect()) {
                selectedPictures.add(positionVideo);
            } else {
                selectedPictures.remove(positionVideo);
            }
            if (allPictures.size() == selectedPictures.size()) {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
            } else {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
            }
            changeSelectedAllUI(allPictures.size() == selectedPictures.size());
            judgeChangeMarkUI();
        } else {
            PictureContent positionVideo = markPictures.get(position);
            if (positionVideo.isVideoSelect()) {
                selectedPictures.add(positionVideo);
            } else {
                selectedPictures.remove(positionVideo);
            }
            if (markPictures.size() == selectedPictures.size()) {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
            } else {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
            }
            changeSelectedAllUI(markPictures.size() == selectedPictures.size());
        }
        String selectText = String.format("已选择%s项", selectedPictures.size() + "");
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
        if (selectedPictures == null || selectedPictures.size() < 1) return;
        for (int i = 0; i < selectedPictures.size(); i++) {
            selectedPictures.get(i).setArtist(MARK_STRING);
            MediaFacer.withPictureContex(getContext())
                    .favoritePicture(Uri.parse(selectedPictures.get(i).getPhotoUri()), "artist");
        }
        allVideosSelectCancel();
    }

    private void markSelectedVideosCancel() {
        if (selectedPictures == null || selectedPictures.size() < 1) return;
        for (int i = 0; i < selectedPictures.size(); i++) {
            selectedPictures.get(i).setArtist("");
            MediaFacer.withPictureContex(getContext())
                    .favoritePicture(Uri.parse(selectedPictures.get(i).getPhotoUri()), "");
        }
        allVideosSelectCancel();
    }

    private void deleteVideos() {
        if (selectedPictures == null || selectedPictures.size() < 1) return;
        for (int i = 0; i < selectedPictures.size(); i++) {
            MediaFacer.withPictureContex(getContext())
                    .deletePicture(Uri.parse(selectedPictures.get(i).getPhotoUri()));
        }

        if (MARKPICTURE == MARK_PICTURE.ALL) {
            for (int i = 0; i < selectedPictures.size(); i++) {
                allPictures.remove(selectedPictures.get(i));
            }
        } else {
            for (int i = 0; i < selectedPictures.size(); i++) {
                allPictures.remove(selectedPictures.get(i));
            }
        }
        selectedPictures.clear();
        mPicAdapter.notifyDataSetChanged();
    }

    private void displayPictureInFragment(LinkedList<PictureContent> pictureList, int position) {
        MediaPictureDisplayFragment picturePage = new MediaPictureDisplayFragment();
        picturePage.setAllpics(pictureList, position);

        Transition transition = TransitionInflater
                .from(getActivity())
                .inflateTransition(android.R.transition.explode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            picturePage.setEnterTransition(new Slide(Gravity.TOP));
            picturePage.setExitTransition(new Slide(Gravity.BOTTOM));
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.play_holder, picturePage)
                .addToBackStack(null)
                .commit();
    }


}
