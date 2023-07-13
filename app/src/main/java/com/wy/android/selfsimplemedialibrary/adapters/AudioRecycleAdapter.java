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
import com.mmrobot.mediafacer.mediaHolders.AudioContent;
import com.wy.android.selfsimplemedialibrary.MMDateUtil;
import com.wy.android.selfsimplemedialibrary.R;


import java.util.LinkedList;

public class AudioRecycleAdapter extends RecyclerView.Adapter<AudioRecycleAdapter.MusicViewHolder> {
    private Context musicActivity;
    private LinkedList<AudioContent> musiclist;
    private musicActionListerner actionListerner;
    private boolean isBulkOperation;


    public AudioRecycleAdapter(Context context, LinkedList<AudioContent> musiclist, musicActionListerner actionListerner) {
        this.musicActivity = context;
        this.musiclist = musiclist;
        this.actionListerner = actionListerner;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(musicActivity);
        View itemView = inflater.inflate(R.layout.media_audio_item, null, false);
        return new MusicViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.notifyMessage(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return musiclist.size();
    }

    public interface musicActionListerner {
        void onMusicItemClicked(int position);
    }

    class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //define views
        private ImageView mIvAudioSelect;
        private ImageView mIvAudioMark;
        private TextView mTvAudioCreateTime;
        private TextView mTvAudioTotalTime;
        private TextView mTvAudioSize;
        private ImageView preview;
        private int position;

        MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            //instantiate views
            preview = itemView.findViewById(R.id.audio_preview);
            mIvAudioSelect = itemView.findViewById(R.id.iv_audio_select);
            mIvAudioMark = itemView.findViewById(R.id.iv_audio_mark);
            mTvAudioCreateTime = itemView.findViewById(R.id.tv_audio_create_time);
            mTvAudioTotalTime = itemView.findViewById(R.id.tv_audio_total_time);
            mTvAudioSize = itemView.findViewById(R.id.tv_audio_size);
        }

        @SuppressLint("SetTextI18n")
        private void notifyMessage(int position) {
            this.position = position;
            AudioContent AudioContent = musiclist.get(position);
            mIvAudioMark.setVisibility(AudioContent.getArtist().equals("artist") ? View.VISIBLE : View.GONE);
            String audioContentPath = AudioContent.getFilePath();
            String fileLastModifiedTime = MMDateUtil.getFileLastModifiedTime(audioContentPath);
            mTvAudioCreateTime.setText(fileLastModifiedTime);
            mTvAudioTotalTime.setText(MMDateUtil.GetTimeFromLong(AudioContent.getDuration()));
            mTvAudioSize.setText("-" + MMDateUtil.bytes2kb(AudioContent.getMusicSize()));
            mIvAudioSelect.setVisibility(isBulkOperation ? View.VISIBLE : View.INVISIBLE);
            mIvAudioSelect.setSelected(AudioContent.isAudioSelect());
        }

        void bind() {

            Glide.with(musicActivity)
                    .load(musiclist.get(position).getArt_uri())
                    .apply(new RequestOptions().placeholder(R.drawable.controller_media_mark).centerCrop())
                    .circleCrop()
                    .into(preview);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            actionListerner.onMusicItemClicked(position);
        }

    }

    public void setAudioListDataSetChanged(LinkedList<AudioContent> audioList) {
        this.musiclist = audioList;
        notifyDataSetChanged();
    }

    public void setBulkOperation(boolean bulkOperation) {
        isBulkOperation = bulkOperation;
        notifyDataSetChanged();
    }

    public void setAudioSelectNotify(int position) {
        AudioContent audioContent = musiclist.get(position);
        boolean audioSelect = audioContent.isAudioSelect();
        audioContent.setAudioSelect(!audioSelect);
        notifyItemChanged(position);
    }

}
