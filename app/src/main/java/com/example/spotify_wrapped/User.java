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

    private String username;

    private String accessToken;
    private ArrayList<String> friends;

    private LinkedHashMap<String, Wrap> userWraps = new LinkedHashMap<>();

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
    public void setAccessToken(String accessToken){this.accessToken = accessToken;}
    public void addFriend(String FriendId) {
        this.friends.add(id);
    }
    public String getFriend(int index) {
        return this.friends.get(index);
    }

    public void setWrap(String key, Wrap wrap) {
        userWraps.put(key, wrap);
    }

    public LinkedHashMap<String, Wrap> getUserWraps() {
        return userWraps;
    }
}
