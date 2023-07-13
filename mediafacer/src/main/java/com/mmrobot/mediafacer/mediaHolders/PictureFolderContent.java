package com.mmrobot.mediafacer.mediaHolders;

import java.util.LinkedList;

public class PictureFolderContent {

    private String folderPath;
    private String folderName;
    private LinkedList<PictureContent> photos;
    private int bucket_id;

    public PictureFolderContent() {
        photos = new LinkedList<>();
    }

    public PictureFolderContent(String path, String folderName) {
        this.folderPath = path;
        this.folderName = folderName;
        photos = new LinkedList<>();
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String path) {
        this.folderPath = path;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public LinkedList<PictureContent> getPhotos() {
        return photos;
    }

    public void setPhotos(LinkedList<PictureContent> photos) {
        this.photos = photos;
    }

    public int getBucket_id() {
        return bucket_id;
    }

    public void setBucket_id(int bucket_id) {
        this.bucket_id = bucket_id;
    }
}
