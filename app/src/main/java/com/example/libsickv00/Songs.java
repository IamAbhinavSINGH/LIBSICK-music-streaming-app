package com.example.libsickv00;


import android.media.MediaMetadataRetriever;

public class Songs {
    private String id;
    private String title;
    private String artistName;
    private String path;
    private long duration = 0;
    private String album;
    private String artUri;

    public Songs(String id, String title, String artistName, String path, long duration, String album, String artUri){
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.path = path;
        this.duration = duration;
        this.album = album;
        this.artUri = artUri;
    }
    public Songs(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtUri() {
        return artUri;
    }

    public void setArtUri(String artUri) {
        this.artUri = artUri;
    }

    public static byte[] getAlbumImage(String path){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        return retriever.getEmbeddedPicture();
    }

}
