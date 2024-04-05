package com.example.spotify_wrapped;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class WrapActivity extends AppCompatActivity {
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private String mAccessToken;

    private UserViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap);
        model = new ViewModelProvider(this).get(UserViewModel.class);

        List<Artist> topArtistsList = null;
        List<Track> topTracksList = null;
        if (model.getCurrentUser().getTop10Artists() != null) {
            LinkedHashMap<String, Artist> currentUserTopArtists = model.getCurrentUser().getTop10Artists();
            topArtistsList = new ArrayList<>(currentUserTopArtists.values());
        }

        if (model.getCurrentUser().getTop20Tracks() != null) {
            LinkedHashMap<String, Track> currentUserTopTracks = model.getCurrentUser().getTop20Tracks();
            topTracksList = new ArrayList<>(currentUserTopTracks.values());
        }

        // only need 5 artists and 5 tracks
        if (topArtistsList != null) {
            topArtistsList = topArtistsList.subList(0, Math.min(5, topArtistsList.size()));
        }
        if (topTracksList != null) {
            topTracksList = topTracksList.subList(0, Math.min(5, topTracksList.size()));
        }

        for (int i = 0; i < topArtistsList.size(); i++) {
            Artist artist = topArtistsList.get(i);
            TextView artistTextView = findViewById(getResources().getIdentifier("artist_" + (i + 1), "id", getPackageName()));
            artistTextView.setText(artist.getName());
        }

        for (int i = 0; i < topTracksList.size(); i++) {
            Track track = topTracksList.get(i);
            TextView trackTextView = findViewById(getResources().getIdentifier("song_" + (i + 1), "id", getPackageName()));
            trackTextView.setText(track.getTrackName());
        }

        ImageButton wrappedBackButton = findViewById(R.id.wrap_back_button);
        wrappedBackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}