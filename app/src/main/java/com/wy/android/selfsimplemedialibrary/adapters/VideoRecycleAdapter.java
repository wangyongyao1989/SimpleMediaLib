package com.wy.android.selfsimplemedialibrary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mmrobot.mediafacer.mediaHolders.VideoContent;
import com.wy.android.selfsimplemedialibrary.MMDateUtil;
import com.wy.android.selfsimplemedialibrary.R;

import java.util.LinkedList;


public class VideoRecycleAdapter extends RecyclerView.Adapter<VideoRecycleAdapter.videoViewHolder> {

    private static final String TAG = VideoRecycleAdapter.class.getSimpleName();
    private Context videoActivity;
    private LinkedList<VideoContent> videoList;
    private videoActionListener actionListener;
    private boolean isBulkOperation;


    public VideoRecycleAdapter(Context context, LinkedList<VideoContent> videoList, videoActionListener actionListener) {
        this.videoActivity = context;
        this.videoList = videoList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(videoActivity);
        View itemView = inflater.inflate(R.layout.media_video_item, null, false);
        return new videoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {
        holder.notifyMessage(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface videoActionListener {
        void onVideoItemClicked(int position);
    }

    class videoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIvVideoSelect;
        private ImageView mIvVideoMark;
        private TextView mTvVideoCreateTime;
        private TextView mTvVideoTotalTime;
        private TextView mTvVideoSize;
        private ImageView preview;
        private int position;

        videoViewHolder(@NonNull View itemView) {
            super(itemView);
            //instantiate views
            preview = itemView.findViewById(R.id.video_preview);
            mIvVideoSelect = itemView.findViewById(R.id.iv_video_select);
            mIvVideoMark = itemView.findViewById(R.id.iv_video_mark);
            mTvVideoCreateTime = itemView.findViewById(R.id.tv_video_create_time);
            mTvVideoTotalTime = itemView.findViewById(R.id.tv_video_total_time);
            mTvVideoSize = itemView.findViewById(R.id.tv_video_size);
        }

        @SuppressLint("SetTextI18n")
        private void notifyMessage(int position) {
            this.position = position;
            VideoContent videoContent = videoList.get(position);
            mIvVideoMark.setVisibility(videoContent.getArtist().equals("artist") ? View.VISIBLE : View.GONE);
            String videoContentPath = videoContent.getPath();
            String fileLastModifiedTime = MMDateUtil.getFileLastModifiedTime(videoContentPath);
            mTvVideoCreateTime.setText(fileLastModifiedTime);
            mTvVideoTotalTime.setText(MMDateUtil.GetTimeFromLong(videoContent.getVideoDuration()));
            mTvVideoSize.setText("-" + MMDateUtil.bytes2kb(videoContent.getVideoSize()));
            mIvVideoSelect.setVisibility(isBulkOperation ? View.VISIBLE : View.INVISIBLE);
            mIvVideoSelect.setSelected(videoContent.isVideoSelect());
        }

        void bind() {
            VideoContent video = videoList.get(position);
            Glide.with(videoActivity)
                    .load(video.getPath())
                    .apply(new RequestOptions().centerCrop())
                    .into(preview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            actionListener.onVideoItemClicked(position);
        }

    }

    public void setVideoListDataSetChanged(LinkedList<VideoContent> videoList) {
        this.videoList = videoList;
        notifyDataSetChanged();
    }

    public void setBulkOperation(boolean bulkOperation) {
        isBulkOperation = bulkOperation;
        notifyDataSetChanged();
    }

    public void setVideoSelectNotify(int position) {
        VideoContent videoContent = videoList.get(position);
        boolean videoSelect = videoContent.isVideoSelect();
        videoContent.setVideoSelect(!videoSelect);
        notifyItemChanged(position);
    }

}


