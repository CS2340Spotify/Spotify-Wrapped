package com.example.spotify_wrapped;

public class Friend {
    private String name;
    private String username;
    private String image;

    private String id;

    private String accessToken;

    public Friend(String name, String username, String image, String id, String accessToken) {
        this.name = name;
        this.username = username;
        this.image = image;
        this.id = id;
        this.accessToken = accessToken;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
