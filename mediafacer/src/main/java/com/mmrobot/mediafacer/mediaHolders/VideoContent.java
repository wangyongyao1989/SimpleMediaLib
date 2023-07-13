package com.mmrobot.mediafacer.mediaHolders;

public class VideoContent {

    private long  videoId;
    private String videoName;
    private String path;
    private long videoDuration;
    private long videoSize;
    private String videoUri;
    private String album;
    private String artist;
    private String modifiedData;
    private boolean isVideoSelect;


    public VideoContent(){ }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getModifiedData() {
        return modifiedData;
    }

    public void setModifiedData(String modifiedData) {
        this.modifiedData = modifiedData;
    }

    public boolean isVideoSelect() {
        return isVideoSelect;
    }

    public void setVideoSelect(boolean videoSelect) {
        isVideoSelect = videoSelect;
    }


    @Override
    public String toString() {
        return "VideoContent{" +
                "videoId=" + videoId +
                ", videoName='" + videoName + '\'' +
                ", path='" + path + '\'' +
                ", videoDuration=" + videoDuration +
                ", videoSize=" + videoSize +
                ", videoUri='" + videoUri + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", modifiedData='" + modifiedData + '\'' +
                '}';
    }
}
