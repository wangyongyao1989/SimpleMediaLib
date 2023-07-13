package com.wy.android.selfsimplemedialibrary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mmrobot.mediafacer.mediaHolders.PictureContent;
import com.wy.android.selfsimplemedialibrary.R;


import java.util.LinkedList;


public class PictureRecycleAdapter extends RecyclerView.Adapter<PictureRecycleAdapter.pictureViewHolder> {

    private static final String TAG = PictureRecycleAdapter.class.getSimpleName();
    private Context pictureActivity;
    private LinkedList<PictureContent> pictureList;
    private pictureActionListrener actionListener;
    private boolean isBulkOperation;


    public PictureRecycleAdapter(Context context, LinkedList<PictureContent> pictureList, pictureActionListrener actionListener) {
        this.pictureActivity = context;
        this.pictureList = pictureList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public pictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(pictureActivity);
        View itemView = inflater.inflate(R.layout.media_picture_item, null, false);
        return new pictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull pictureViewHolder holder, int position) {
        holder.notifyMessage(position);
        holder.Bind();
    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    public interface pictureActionListrener {
        void onPictureItemClicked(int position);
    }

    class pictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView picture;
        private ImageView mIvVideoMark;
        private ImageView mIvVideoSelect;
        private int position;

        pictureViewHolder(@NonNull View itemView) {
            super(itemView);
            //instantiate views
            picture = itemView.findViewById(R.id.picture);
            mIvVideoMark = itemView.findViewById(R.id.iv_picture_mark);
            mIvVideoSelect = itemView.findViewById(R.id.iv_picture_select);
        }

        @SuppressLint("SetTextI18n")
        private void notifyMessage(int position) {
            this.position = position;
            PictureContent videoContent = pictureList.get(position);
            boolean artist = (videoContent.getArtist() != null) && videoContent.getArtist().equals("artist");
            Log.e(TAG, "notifyMessage position" + position+"==="+artist);
            mIvVideoMark.setVisibility(artist ? View.VISIBLE : View.INVISIBLE);
            mIvVideoSelect.setVisibility(isBulkOperation ? View.VISIBLE : View.INVISIBLE);
            mIvVideoSelect.setSelected(videoContent.isVideoSelect());
        }

        void Bind() {
            PictureContent pic = pictureList.get(position);
            Glide.with(pictureActivity)
                    .load(Uri.parse(pic.getPhotoUri()))
                    .apply(new RequestOptions().centerCrop())
                    .into(picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            actionListener.onPictureItemClicked(position);
        }

    }

    public void setPictureListDataSetChanged(LinkedList<PictureContent> pictureList) {
        this.pictureList = pictureList;
        notifyDataSetChanged();
    }

    public void setBulkOperation(boolean bulkOperation) {
        isBulkOperation = bulkOperation;
        notifyDataSetChanged();
    }

    public void setPictureSelectNotify(int position) {
        PictureContent videoContent = pictureList.get(position);
        boolean videoSelect = videoContent.isVideoSelect();
        videoContent.setVideoSelect(!videoSelect);
        notifyItemChanged(position);
    }

}
