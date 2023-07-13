package com.mmrobot.mediafacer.mediaHolders;

import android.net.Uri;

public class AudioContent {

    private String name;
    private String title;
    private String filePath;
    private String artist;
    private String album;
    private String genre;
    private String composer;
    private Uri art_uri;
    private long musicSize;
    private long duration;
    private long musicId;
    private String musicUri;
    private String modifiedData;
    private boolean isAudioSelect;


    public AudioContent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public Uri getArt_uri() {
        return art_uri;
    }

    public void setArt_uri(Uri art_uri) {
        this.art_uri = art_uri;
    }

    public long getMusicSize() {
        return musicSize;
    }

    public void setMusicSize(long musicSize) {
        this.musicSize = musicSize;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getMusicId() {
        return musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    public String getModifiedData() {
        return modifiedData;
    }

    public void setModifiedData(String modifiedData) {
        this.modifiedData = modifiedData;
    }



    public boolean isAudioSelect() {
        return isAudioSelect;
    }

    public void setAudioSelect(boolean audioSelect) {
        isAudioSelect = audioSelect;
    }


    @Override
    public String toString() {
        return "AudioContent{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", filePath='" + filePath + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", composer='" + composer + '\'' +
                ", art_uri=" + art_uri +
                ", musicSize=" + musicSize +
                ", duration=" + duration +
                ", musicId=" + musicId +
                ", musicUri='" + musicUri + '\'' +
                ", modifiedData='" + modifiedData + '\'' +
                '}';
    }
}
