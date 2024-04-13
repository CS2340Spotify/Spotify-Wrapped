package com.example.spotify_wrapped;

import java.util.ArrayList;

public class Playlist {
    private String name;
    private String imageUrl;
    private String id;
    private ArrayList<String> songs;

    public Playlist(String name, String imageUrl, String id, ArrayList<String> songs) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }
}