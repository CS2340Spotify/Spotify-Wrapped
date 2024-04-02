package com.example.spotify_wrapped;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ArrayAdapter;

import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private String mAccessToken;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton settingsButton = view.findViewById(R.id.settings_button);

        // on click listener for the Settings button on profile page
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment settingsFragment = new SettingsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, settingsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        fetchFollowedArtists();
        fetchUserPlaylists();
    }

    public void fetchFollowedArtists() {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/following?type=artist")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to fetch data from Spotify API", Toast.LENGTH_SHORT).show());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    final JSONArray items = jsonObject.getJSONObject("artists").getJSONArray("items");
                    List<String> artists = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String artistName = item.getString("name");
                        String imageUrl = item.getJSONArray("images").getJSONObject(0).getString("url"); // get the URL of the first image
                        artists.add(new String(artistName, imageUrl));
                    }
                    updateArtistsRecyclerView(artists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to fetch data from Spotify API", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    public void fetchUserPlaylists() {
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/playlists")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to fetch data from Spotify API", Toast.LENGTH_SHORT).show());
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    final JSONArray items = jsonObject.getJSONArray("items");
                    List<String> playlists = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String playlistName = item.getString("name");
                        String imageUrl = item.getJSONObject("images").getJSONObject(0).getString("url"); // get the URL of the first image
                        playlists.add(new String(playlistName, imageUrl));
                    }
                    updatePlaylistsRecyclerView(playlists);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Failed to fetch data from Spotify API", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void updateArtistsRecyclerView(List<String> artistNames) {
        RecyclerView recyclerView = getView().findViewById(R.id.following_recycler_view);
        RecyclerView.Adapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, artistNames);
        recyclerView.setAdapter(adapter);
    }

    private void updatePlaylistsRecyclerView(List<String> playlistNames) {
        RecyclerView recyclerView = getView().findViewById(R.id.playlists_recycler_view);
        RecyclerView.Adapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, playlistNames);
        recyclerView.setAdapter(adapter);
    }
}


