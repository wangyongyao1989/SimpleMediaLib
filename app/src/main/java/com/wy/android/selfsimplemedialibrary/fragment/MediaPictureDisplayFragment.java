package com.wy.android.selfsimplemedialibrary.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mmrobot.mediafacer.mediaHolders.PictureContent;
import com.wy.android.selfsimplemedialibrary.R;
import com.wy.android.selfsimplemedialibrary.ZoomOutPageTransformer;


import java.util.LinkedList;

public class MediaPictureDisplayFragment extends Fragment {

    private LinkedList<PictureContent> allpics;
    private int currentPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.media_picture_display_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager picture_pager = view.findViewById(R.id.picture_pager);
        picture_pager.setOffscreenPageLimit(3);
        picture_pager.setPageTransformer(true, new ZoomOutPageTransformer());
        picturePager adapter = new picturePager();
        picture_pager.setAdapter(adapter);
        picture_pager.setCurrentItem(currentPosition, true);
    }

    public void setAllpics(LinkedList<PictureContent> pics, int currentPosition) {
        allpics = pics;
        this.currentPosition = currentPosition;
    }


    private class picturePager extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View pictureView = inflater.inflate(R.layout.media_picture_pager_item, null);

            ImageView picturezone = pictureView.findViewById(R.id.picture_zone);

            Glide.with(getActivity())
                    .load(Uri.parse(allpics.get(position).getPhotoUri()))
                    .apply(new RequestOptions().fitCenter())
                    .into(picturezone);


            ((ViewPager) container).addView(pictureView);
            return pictureView;
        }

        @Override
        public int getCount() {
            return allpics.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup containerCollection, int position, Object view) {
            ((ViewPager) containerCollection).removeView((View) view);
        }

    }

}
