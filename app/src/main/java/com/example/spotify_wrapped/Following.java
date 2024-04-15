package com.example.spotify_wrapped;

public class Following {
    private String artistName;
    private String artistImageUrl;

    public Following(String artistName, String artistImageUrl) {
        this.artistName = artistName;
        this.artistImageUrl = artistImageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }
}
