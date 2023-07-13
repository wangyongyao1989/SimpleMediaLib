package com.mmrobot.mediafacer.mediaHolders;

import java.util.LinkedList;

public class AudioFolderContent {

    private LinkedList<AudioContent> audioFiles;
    private String folderName;
    private String folderPath;
    private int bucket_id;

    public AudioFolderContent() {
        audioFiles = new LinkedList<>();
    }

    public LinkedList<AudioContent> getMusicFiles() {
        return audioFiles;
    }

    public void setMusicFiles(LinkedList<AudioContent> audioFiles) {
        this.audioFiles = audioFiles;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getNumberOfSongs() {
        return audioFiles.size();
    }

    public int getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(int bucket_id) {
        this.bucket_id = bucket_id;
    }

    @Override
    public String toString() {
        return "AudioFolderContent{" +
                "audioFiles=" + audioFiles +
                ", folderName='" + folderName + '\'' +
                ", folderPath='" + folderPath + '\'' +
                ", bucket_id=" + bucket_id +
                '}';
    }

}
