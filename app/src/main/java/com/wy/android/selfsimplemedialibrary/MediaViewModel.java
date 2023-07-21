package com.wy.android.selfsimplemedialibrary;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MediaViewModel extends ViewModel {

    public enum ONCLICK {
        ONCLICK_RETURN_MAIN_ACTIVITY,
        ONCLICK_RETURN_ALL_MEDIA_FRAGMENT,
        ONCLICK_RETURN_VIDEO_FRAGMENT,
        ONCLICK_GO_VIDEO_FRAGMENT,
        ONCLICK_GO_AUDIO_FRAGMENT,
        ONCLICK_GO_PICTURE_FRAGMENT,

        ONCLICK_VIDEO_PLAY_MARK,
        ONCLICK_VIDEO_PLAY_DELETE,

    }

    public MutableLiveData<ONCLICK> onCilckEvent = new MutableLiveData<>();



}
