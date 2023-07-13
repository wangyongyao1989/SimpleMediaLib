package com.wy.android.selfsimplemedialibrary.fragment;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.mmrobot.mediafacer.AudioGet;
import com.mmrobot.mediafacer.MediaFacer;
import com.mmrobot.mediafacer.mediaHolders.AudioContent;
import com.mmrobot.mediafacer.mediaHolders.AudioFolderContent;
import com.wy.android.selfsimplemedialibrary.DeleteMediaDialog;
import com.wy.android.selfsimplemedialibrary.MediaViewModel;
import com.wy.android.selfsimplemedialibrary.R;
import com.wy.android.selfsimplemedialibrary.adapters.AudioRecycleAdapter;
import com.wy.android.selfsimplemedialibrary.databinding.MediaFragmentAudioLayoutBinding;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class MediaAudioFragment extends Fragment {

    private static final String TAG = MediaAudioFragment.class.getSimpleName();
    private MediaFragmentAudioLayoutBinding mBinding;
    private ArrayList<AudioContent> allAudio;
    private MediaPlayer player;

    private static final String MARK_STRING = "artist";

    private RecyclerView mAudioRecycler;
    private AudioRecycleAdapter mAudioAdapter;
    private LinkedList<AudioContent> allAudios;
    private LinkedList<AudioContent> markAudios;
    private LinkedList<AudioContent> selectedAudios;

    private View mAudioSelectAll;
    private LinearLayout mLlAudioSelectAll;
    private View mAudioSelectMark;
    private LinearLayout mLlAudioSelectMark;
    private RelativeLayout mRlAudioBulkOperation;
    private RelativeLayout mRlBulkOperation;
    private RelativeLayout mRlBulkOperationMark;
    private RelativeLayout mRlBulkOperationDelete;
    private RelativeLayout mRlBulkOperationAll;
    private ImageView mIvBulkOperationMark;
    private TextView mTvBulkOperationMark;

    private MARK_AUDIO MARKAUDIO = MARK_AUDIO.ALL;
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


    private enum MARK_AUDIO {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.media_fragment_audio_layout, container, false);
        initView();
        initObserver();
        initListener();
        initData();
        return mBinding.getRoot();
    }

    private void initData() {

    }

    private void initListener() {
        mDeleteMediaDialog = new DeleteMediaDialog(MediaAudioFragment.this.getActivity()
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
                    mAudioAdapter.setBulkOperation(false);
                    getAudios();
                    mAudioSelectAll.setVisibility(View.VISIBLE);
                    mAudioSelectMark.setVisibility(View.INVISIBLE);
                    MARKAUDIO = MARK_AUDIO.ALL;
                    mAudioAdapter.setAudioListDataSetChanged(allAudios);
                    mIvTopBack.setSelected(BULKOPERATION == BULK_OPERATION.BULK_SELECT);
                    selectVideosCancel();
                    changeSelectedAllUI(false);
                    changeMarkUI(false);
                } else {
                    mMediaViewModel.onCilckEvent.postValue(MediaViewModel.ONCLICK.ONCLICK_RETURN_ALL_MEDIA_FRAGMENT);

                }
            }
        });

        mLlAudioSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MARKAUDIO == MARK_AUDIO.ALL) return;
                mAudioSelectAll.setVisibility(View.VISIBLE);
                mAudioSelectMark.setVisibility(View.INVISIBLE);
                MARKAUDIO = MARK_AUDIO.ALL;
                mAudioAdapter.setAudioListDataSetChanged(allAudios);
            }
        });

        mLlAudioSelectMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MARKAUDIO == MARK_AUDIO.MARK) return;
                mAudioSelectAll.setVisibility(View.INVISIBLE);
                mAudioSelectMark.setVisibility(View.VISIBLE);
                MARKAUDIO = MARK_AUDIO.MARK;
                mAudioAdapter.setAudioListDataSetChanged(markAudios);
            }
        });

        mRlAudioBulkOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMarkUI(MARKAUDIO == MARK_AUDIO.MARK);
                MARKOPERATION = MARKAUDIO == MARK_AUDIO.MARK
                        ? BULK_MARK_OPERATION.BULK_MARK_CANCEL
                        : BULK_MARK_OPERATION.BULK_MARK;
                mRlBulkOperation.setVisibility(View.VISIBLE);
                mRlBulkOperationTop.setVisibility(View.GONE);
                mIvTopBack.setSelected(true);
                BULKOPERATION = BULK_OPERATION.BULK_SELECT;
                mTvTopTitle.setText("已选择0项");
                mAudioAdapter.setBulkOperation(true);
            }
        });

        mRlBulkOperationMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAudios.size() < 1) {
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
                if (selectedAudios.size() < 1) {
                    Toast.makeText(getActivity()
                            , R.string.controller_medialib_selectpage_bulk_select_file_toast
                            , Toast.LENGTH_LONG).show();
                    return;
                }
                String text = String.format("是否删除%s项文件？", selectedAudios.size() + "");
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
        mLlAudioSelectAll = mBinding.llAudioSelectAll;
        mAudioSelectAll = mBinding.viewAudioSelectAll;
        mLlAudioSelectMark = mBinding.llAudioSelectMark;
        mAudioSelectMark = mBinding.viewAudioSelectMark;
        mAudioSelectMark.setVisibility(View.INVISIBLE);

        mRlAudioBulkOperation = mBinding.rlAudioBulkOperation;
        mAudioRecycler = mBinding.audioRecycler;
        mAudioRecycler.hasFixedSize();
        mAudioRecycler.setHasFixedSize(true);
        mAudioRecycler.setItemViewCacheSize(20);
        mAudioRecycler.setDrawingCacheEnabled(true);
        mAudioRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mAudioRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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

        allAudios = new LinkedList<>();
        markAudios = new LinkedList<>();
        selectedAudios = new LinkedList<>();
        getAudios();
        setVideoActionsListener();
    }

    private void setVideoActionsListener() {
        AudioRecycleAdapter.musicActionListerner actionListerner = new AudioRecycleAdapter.musicActionListerner() {
            @Override
            public void onMusicItemClicked(int position) {
                if (BULKOPERATION == BULK_OPERATION.BULK_UNSELECT) {
                    //play video
//                    displayAudioInFragment(allAudios, position);
//                    playAudio(audio);

                } else {
                    selectVideoAction(position);
                }
            }
        };

        mAudioAdapter = new AudioRecycleAdapter(getActivity(), allAudios, actionListerner);
        mAudioRecycler.setAdapter(mAudioAdapter);
    }

    private void getAudios() {
        final LinkedList<AudioFolderContent> AudioFolder
                = new LinkedList<>(MediaFacer.withAudioContex(getActivity())
                .getAllAudioFolder(AudioGet.externalContentUri));
        allAudios.clear();
        markAudios.clear();
        for (int i = 0; i < AudioFolder.size(); i++) {
            allAudios = AudioFolder.get(i).getMusicFiles();
//            if (AudioFolder.get(i).getFolderName().equals("SmartCar")) {
//                allAudios = AudioFolder.get(i).getMusicFiles();
//            }
        }

        for (int i = 0; i < allAudios.size(); i++) {
            if (allAudios.get(i).getArtist().contains(MARK_STRING)) {
                markAudios.add(allAudios.get(i));
            }
        }
    }

    private void deleteVideos() {
        if (selectedAudios == null || selectedAudios.size() < 1) return;
        for (int i = 0; i < selectedAudios.size(); i++) {
            MediaFacer.withAudioContex(getContext())
                    .deleteAudio(Uri.parse(selectedAudios.get(i).getMusicUri()));
        }

        if (MARKAUDIO == MARK_AUDIO.ALL) {
            for (int i = 0; i < selectedAudios.size(); i++) {
                allAudios.remove(selectedAudios.get(i));
            }
        } else {
            for (int i = 0; i < selectedAudios.size(); i++) {
                allAudios.remove(selectedAudios.get(i));
            }
        }
        selectedAudios.clear();
        mAudioAdapter.notifyDataSetChanged();
    }

    private void selectVideosCancel() {
        if (selectedAudios == null || selectedAudios.size() < 1) return;
        for (int i = 0; i < selectedAudios.size(); i++) {
            selectedAudios.get(i).setAudioSelect(false);
        }
        selectedAudios.clear();
    }

    private void changeSelectedAllUI(boolean isAll) {
        mIvBulkOperationAll.setSelected(isAll);
        mTvBulkOperationAll.setText(isAll
                ? R.string.controller_medialib_selectpage_bulk_operation_all_cancel
                : R.string.controller_medialib_selectpage_bulk_operation_all);
    }

    //判断勾选的是否无标记项
    private boolean judgeMarkType() {
        if (selectedAudios.size() < 1) return true;
        for (int i = 0; i < selectedAudios.size(); i++) {
            boolean isMark = selectedAudios.get(i).getArtist().equals(MARK_STRING);
            if (!isMark) {
                return true;
            }
        }
        return false;
    }


    private void selectVideoAction(int position) {
        mAudioAdapter.setAudioSelectNotify(position);
        if (MARKAUDIO == MARK_AUDIO.ALL) {
            AudioContent positionVideo = allAudios.get(position);
            if (positionVideo.isAudioSelect()) {
                selectedAudios.add(positionVideo);
            } else {
                selectedAudios.remove(positionVideo);
            }
            if (allAudios.size() == selectedAudios.size()) {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
            } else {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
            }
            changeSelectedAllUI(allAudios.size() == selectedAudios.size());
            judgeChangeMarkUI();
        } else {
            AudioContent positionVideo = markAudios.get(position);
            if (positionVideo.isAudioSelect()) {
                selectedAudios.add(positionVideo);
            } else {
                selectedAudios.remove(positionVideo);
            }
            if (markAudios.size() == selectedAudios.size()) {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_ALL;
            } else {
                SELECTOPERATION = BULK_SELECT_OPERATION.BULK_SELECT_CANCEL;
            }
            changeSelectedAllUI(markAudios.size() == selectedAudios.size());
        }
        String selectText = String.format("已选择%s项", selectedAudios.size() + "");
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


    private void changeMarkUI(boolean isMark) {
        mIvBulkOperationMark.setSelected(isMark);
        mTvBulkOperationMark.setText(isMark
                ? R.string.controller_medialib_selectpage_bulk_operation_mark_cancel
                : R.string.controller_medialib_selectpage_mark);
    }


    private void markSelectedVideos() {
        if (selectedAudios == null || selectedAudios.size() < 1) return;
        for (int i = 0; i < selectedAudios.size(); i++) {
            selectedAudios.get(i).setArtist(MARK_STRING);
            MediaFacer.withAudioContex(getContext())
                    .favoriteAudio(Uri.parse(selectedAudios.get(i).getMusicUri()), "artist");
        }
        allVideosSelectCancel();
    }

    private void markSelectedVideosCancel() {
        if (selectedAudios == null || selectedAudios.size() < 1) return;
        for (int i = 0; i < selectedAudios.size(); i++) {
            selectedAudios.get(i).setArtist("");
            MediaFacer.withAudioContex(getContext())
                    .favoriteAudio(Uri.parse(selectedAudios.get(i).getMusicUri()), "");
        }
        allVideosSelectCancel();
    }

    private void allVideosSelectCancel() {
        selectedAudios.clear();
        if (MARKAUDIO == MARK_AUDIO.ALL) {
            for (int i = 0; i < allAudios.size(); i++) {
                allAudios.get(i).setAudioSelect(false);
            }
        } else {
            for (int i = 0; i < markAudios.size(); i++) {
                markAudios.get(i).setAudioSelect(false);
            }
        }
        mTvTopTitle.setText("已选择0项");
        mAudioAdapter.notifyDataSetChanged();
    }

    private void allVideosSelect() {
        String selectText;
        selectedAudios.clear();
        if (MARKAUDIO == MARK_AUDIO.ALL) {
            for (int i = 0; i < allAudios.size(); i++) {
                allAudios.get(i).setAudioSelect(true);
            }
            selectedAudios.addAll(allAudios);
            selectText = String.format("已选择%s项", allAudios.size() + "");
        } else {
            for (int i = 0; i < markAudios.size(); i++) {
                markAudios.get(i).setAudioSelect(true);
            }
            selectedAudios.addAll(markAudios);
            selectText = String.format("已选择%s项", markAudios.size() + "");
        }
        mTvTopTitle.setText(selectText);
        mAudioAdapter.notifyDataSetChanged();
    }


    private void playAudio(AudioContent audio) {
        Uri content = Uri.parse(audio.getMusicUri());
        if (player == null) {
            player = new MediaPlayer();
            try {
                AssetFileDescriptor file = getActivity().getContentResolver().openAssetFileDescriptor(content, "r");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    player.setDataSource(file);
                } else {
                    player.setDataSource(audio.getFilePath());
                }
                player.setLooping(false);
                player.prepare();
                player.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            player.release();
            player = new MediaPlayer();
            try {
                AssetFileDescriptor file = getActivity().getContentResolver().openAssetFileDescriptor(content, "r");
                assert file != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    player.setDataSource(file);
                } else {
                    player.setDataSource(audio.getFilePath());
                }
                player.setLooping(false);
                player.prepare();
                player.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
