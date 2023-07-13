package com.mmrobot.mediafacer.mediaHolders;

import java.util.ArrayList;

public class AudioArtistContent {

    private String artistName;
    private ArrayList<AudioAlbumContent> albums = new ArrayList<>();

    public AudioArtistContent() { }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public ArrayList<AudioAlbumContent> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<AudioAlbumContent> albums) {
        this.albums = albums;
    }

    public int getNumOfSongs() {
        int numOfSongs = 0;
        for(int i = 0; i < albums.size();i++){
            numOfSongs = numOfSongs + albums.get(i).getNumberOfSongs();
        }
        return numOfSongs;
    }

}
