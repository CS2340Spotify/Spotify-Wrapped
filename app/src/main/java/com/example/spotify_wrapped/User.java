package com.example.spotify_wrapped;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class User {
    private String name;
    private String email;
    private String id;
    private String image;
    private String password;
    private MutableLiveData<UpdateStatus> updateStatus = new MutableLiveData<>();
    private String username;

    private String accessToken;
    private ArrayList<String> friends;
    private LinkedHashMap<String, Artist> topTenArtists = new LinkedHashMap<>(20);
    private LinkedHashMap<String, Track> topTwentyArtists = new LinkedHashMap<>(30);

    public User (String name, String email, String id, String image, String password, String username, String accessToken) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.image = image;
        this.password = password;
        this.username = username;
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void addFriend(String FriendId) {
        this.friends.add(id);
    }
    public String getFriend(int index) {
        return this.friends.get(index);
    }

    public void setArtist(String key, Artist artist) {
        topTenArtists.put(key, artist);
    }
    public Artist getArtist(String key) {
        return topTenArtists.get(key);
    }
    public void setTrack(String key, Track track) {
        topTwentyArtists.put(key, track);
    }
    public LinkedHashMap<String, Track> getTop20Tracks(){return this.topTwentyArtists;}
    public LinkedHashMap<String, Artist> getTop10Artists() { return this.topTenArtists;}
    public LiveData<UpdateStatus> getUpdateStatus() { return updateStatus;}
}
