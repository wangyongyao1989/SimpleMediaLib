package com.mmrobot.mediafacer.mediaHolders;

import java.util.ArrayList;

public class audioGenreContents {

    private ArrayList<AudioContent> audioFiles;
    private String genreName;
    private String genreId;

    public audioGenreContents(){
        audioFiles = new ArrayList<>();
    }

    public ArrayList<AudioContent> getAudioFiles() {
        return audioFiles;
    }

    public void setAudioFiles(ArrayList<AudioContent> audioFiles) {
        this.audioFiles = audioFiles;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public void addGenreMusic(AudioContent music){
        audioFiles.add(music);
    }

}
