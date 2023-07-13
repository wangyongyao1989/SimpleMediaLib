package com.mmrobot.mediafacer.mediaHolders;

public class PictureContent {

    private String pictureName;
    private String picturePath;
    private Long pictureSize;
    private String photoUri;
    private int pictureId;
    private String artist = "unknow";
    private String modifiedData;
    private boolean isVideoSelect;


    public PictureContent() {

    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public Long getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(Long pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getModifiedData() {
        return modifiedData;
    }

    public void setModifiedData(String modifiedData) {
        this.modifiedData = modifiedData;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isVideoSelect() {
        return isVideoSelect;
    }

    public void setVideoSelect(boolean videoSelect) {
        isVideoSelect = videoSelect;
    }

    @Override
    public String toString() {
        return "PictureContent{" +
                "pictureName='" + pictureName + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", pictureSize=" + pictureSize +
                ", photoUri='" + photoUri + '\'' +
                ", artist=" + artist +
                ", pictureId=" + pictureId +
                ", modifiedData=" + modifiedData +
                '}';
    }
}
