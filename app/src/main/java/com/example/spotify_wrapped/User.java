package com.example.spotify_wrapped;

public class User {
    private String name;
    private String email;
    private String id;
    private String image;
    private String password;

    private String username;

    private String accessToken;

    public User (String name, String email, String id, String image, String password, String username, String accessToken) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.image = image;
        this.password = password;
        this.username = username;
        this.accessToken = accessToken;
    }


}