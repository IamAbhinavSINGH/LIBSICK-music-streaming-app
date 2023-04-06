package com.example.libsick;

public class SongFiles {
    private  String title;
    private String artist_Name;
    private String duration;
    private String path;
    private String album;

    public SongFiles() {
    }

    public SongFiles(String title, String artist_Name, String duration, String path, String album) {
        this.title = title;
        this.artist_Name = artist_Name;
        this.duration = duration;
        this.path = path;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist_Name() {
        return artist_Name;
    }

    public void setArtist_Name(String artist_Name) {
        this.artist_Name = artist_Name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
