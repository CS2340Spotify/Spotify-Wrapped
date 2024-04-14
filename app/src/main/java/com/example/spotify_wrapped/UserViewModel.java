package com.example.spotify_wrapped;

import static com.example.spotify_wrapped.User.setCurrentUser;
import static com.example.spotify_wrapped.UserItemTimeFrame.LONG;
import static com.example.spotify_wrapped.UserItemTimeFrame.MEDIUM;
import static com.example.spotify_wrapped.UserItemTimeFrame.SHORT;

import  com.example.spotify_wrapped.UserItemTimeFrame;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserViewModel extends ViewModel {
    private User currentUser;
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private Call call;

    private final DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");

    private final DatabaseReference followingData = FirebaseDatabase.getInstance().getReference("Following");
    DatabaseReference playlistsRef = FirebaseDatabase.getInstance().getReference("playlists");

    private final DatabaseReference trackData = FirebaseDatabase.getInstance().getReference("tracks");

    public void getUserInformation(String id, String token, Activity context) {
        if (id == null) {
            Log.wtf("what the fuck", "shit is null");
            return;
        }
        user.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.wtf("Firebase", "id not in database");
                } else {
                    try {
                        HashMap<String, Object> vals = (HashMap<String, Object>) task.getResult().getValue();
                        String password = (String) vals.get("password");
                        String name = (String) vals.get("name");
                        String email = (String) vals.get("email");
                        String image = (String) vals.get("image");
                        String username = (String) vals.get("username");
                        String spotId = (String) vals.get("spotId");
                        String accessToken = token;
                        currentUser = new User(name, email, id, image, password, username, accessToken, spotId);
                        if (vals.get("wraps") != null) {
                            ArrayList<HashMap<String,Object>> wraps = (ArrayList<HashMap<String, Object>>) vals.get("wraps");
                            for (int i = 0; i < wraps.size(); i++) {
                                HashMap<String, Object> wrap = (HashMap<String, Object>) wraps.get(i);
                                Wrap newWrap = new Wrap(wrap);
                                currentUser.setWrap(String.valueOf(i), newWrap);
                            }
                        }
                        System.out.println("hi");
//                        if (vals.get("topArtists") == null) {
//                            getArtistsFromSpotify();
//                        } else {
//                            ArrayList<HashMap<String,Object>> artists = (ArrayList<HashMap<String, Object>>) vals.get("topArtists");
//                            for (int i = 1; i <= 10; i++) {
//                                HashMap<String, Object> artist = (HashMap<String, Object>) artists.get(i);
//                                String artistImage = (String) artist.get("image");
//                                String artistName = (String) artist.get("name");
//                                String artistId = (String) artist.get("id");
//                                Artist newArtist = new Artist(artistName, artistImage, artistId);
//                                ArrayList<String> genres = (ArrayList<String>) artist.get("genres");
//                                for (int j = 1; j < genres.size(); j++) {
//                                    newArtist.setGenres(String.valueOf(j), genres.get(j));
//                                }
//                                currentUser.setArtist(String.valueOf(i), newArtist);
//                            }
//                        }
//                        if (vals.get("topTracks") == null) {
//                            getTracksFromSpotify();;
//                        } else {
//                            ArrayList<HashMap<String,Object>> tracks = (ArrayList<HashMap<String, Object>>) vals.get("topTracks");
//                            for (int i = 1; i <= 20; i++) {
//                                HashMap<String, Object> track = (HashMap<String, Object>) tracks.get(i);
//                                String albumImage = (String) track.get("albumImage");
//                                String albumName = (String) track.get("albumName");
//                                String id = (String) track.get("id");
//                                String trackName = (String) track.get("trackName");
//                                Track newTrack = new Track(trackName, albumName, albumImage, id);
//                                ArrayList<String> artists = (ArrayList<String>) track.get("artists");
//                                for (int j = 1; j < artists.size(); j++) {
//                                    newTrack.setArtists(String.valueOf(j), artists.get(j));
//                                }
//                                currentUser.setTrack(String.valueOf(i), newTrack);
//                            }
//                        }
                    } catch (Exception e) {
                        Log.wtf("JSON", "JSON Error");
                    }
                }
            }
        });
    }

    /**
     * Returns a new wrapped with one of the set time frames given by the spotify API. Another
     * method will generate a wrapped for a custom time frame. The new wrapped is added to the
     * current user's list of wraps and saved in Firebase.
     */
    public Wrap makeNewWrapped(UserItemTimeFrame time, Activity context) {
        Long dateNow = System.currentTimeMillis() / 1000L;
        Long dateFrom;
        if (time == LONG) {
            dateFrom = dateNow - 31556926L;
        } else if (time == MEDIUM) {
            dateFrom = dateNow - 15778458L;
        } else {
            dateFrom = dateNow - 2629743L;
        }
        Wrap newWrap = new Wrap(dateNow, dateFrom);
        getTracksFromSpotify(time, newWrap, context);
        synchronized(newWrap.getTopTracks()) {
            try {
                newWrap.getTopTracks().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getArtistsFromSpotify(time, newWrap, context);
        synchronized(newWrap.getTopArtists()) {
            try {
                newWrap.getTopArtists().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        currentUser.setWrap(String.valueOf(currentUser.getUserWraps().size()), newWrap);
        user.child(currentUser.getId()).child("wraps").setValue(currentUser.getUserWraps());
        return newWrap;
    }


    private void getTracksFromSpotify(UserItemTimeFrame time, Wrap wrap, Activity context) {
        Request request;
        if (time == MEDIUM) {
            request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/tracks?offset=0&limit=5")
                    .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                    .build();
        } else  if (time == LONG) {
            request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/tracks?time_range=long_term&offset=0&limit=5")
                    .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                    .build();

        } else {
            request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/tracks?time_range=short_term&offset=0&limit=5")
                    .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                    .build();
        }
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray  tracks = (JSONArray) jsonObject.get("items");
                    for (int i = 0; i < tracks.length(); i++) {
                        JSONObject track = (JSONObject) tracks.get(i);
                        String id = (String) track.get("id");
                        String name = (String) track.get("name");

                        String imageUrl;
                        JSONObject album = (JSONObject) track.get("album");
                        String albumName = (String) album.get("name");
                        JSONArray images = (JSONArray) album.get("images");
                        if (images.isNull(0)) {
                            imageUrl = null;
                        } else {
                            JSONObject imageOb = (JSONObject) images.get(1);
                            imageUrl = (String) imageOb.get("url");
                        }
                        Track newTrack = new Track(name, albumName, imageUrl, id);

                        JSONArray artists = (JSONArray) track.get("artists");
                        for (int j = 0; j < artists.length(); j++) {
                            JSONObject artist = (JSONObject) artists.get(j);
                            newTrack.setArtists(String.valueOf(j), (String) artist.get("name"));
                        }
//                        Artist newArtist = new Artist(name, imageUrl, id);
//
                        wrap.setTrack(String.valueOf(i + 1), newTrack);

                    }
                    synchronized (wrap.getTopTracks()) {
                        wrap.getTopTracks().notify();

                    }
//                    user.child(currentUser.getId()).child("topTracks").setValue(currentUser.getTop20Tracks());
                } catch (Exception e) {
                    Log.wtf("Http", e.getMessage());
                }
            }
        });
    }

    private void getFollowingFromSpotify() {
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/following?type=artist")
                .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray artists = (JSONArray) jsonObject.get("items");
                    for (int i = 0; i < artists.length(); i++) {
                        JSONObject artist = (JSONObject) artists.get(i);
                        String id = (String) artist.get("id");
                        String name = (String) artist.get("name");

                        String imageUrl;
                        JSONArray images = (JSONArray) artist.get("images");
                        if (images.isNull(0)) {
                            imageUrl = null;
                        } else {
                            JSONObject imageOb = (JSONObject) images.get(1);
                            imageUrl = (String) imageOb.get("url");
                        }
                        Artist newArtist = new Artist(name, imageUrl, id);
                        currentUser.setArtist(String.valueOf(i + 1), newArtist);
                        followingData.child(newArtist.getId()).setValue(newArtist);
                    }
                } catch (JSONException e) {
                    Log.wtf("Http", e.getMessage());
                }
            }
        });
    }

    private void getPlaylistsFromSpotify() {
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/smedjan/playlists")
                .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                .build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray playlists = (JSONArray) jsonObject.get("items");
                    for (int i = 0; i < playlists.length(); i++) {
                        JSONObject playlist = (JSONObject) playlists.get(i);
                        String id = (String) playlist.get("id");
                        String name = (String) playlist.get("name");

                        String imageUrl;
                        JSONArray images = (JSONArray) playlist.get("images");
                        if (images.isNull(0)) {
                            imageUrl = null;
                        } else {
                            JSONObject imageOb = (JSONObject) images.get(1);
                            imageUrl = (String) imageOb.get("url");
                        }
                        ArrayList<String> songs = new ArrayList<>();
                        Playlist newPlaylist = new Playlist(name, imageUrl, id, songs);
                        currentUser.setPlaylist(String.valueOf(i + 1), newPlaylist);
                        playlistsRef.child(newPlaylist.getId()).setValue(newPlaylist);
                    }
                } catch (JSONException e) {
                    Log.wtf("Http", e.getMessage());
                }
            }
        });
    }

    private void getArtistsFromSpotify(UserItemTimeFrame time, Wrap wrap, Activity context) {
        Request request;
        if (time == MEDIUM) {
            request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/artists?offset=0&limit=5")
                    .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                    .build();
        } else  if (time == LONG) {
            request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/artists?time_range=long_term&offset=0&limit=5")
                    .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                    .build();

        } else {
            request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/artists?time_range=short_term&offset=0&limit=5")
                    .addHeader("Authorization", "Bearer " + currentUser.getAccessToken())
                    .build();
        }
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray  artists = (JSONArray) jsonObject.get("items");
                    for (int i = 0; i < artists.length(); i++) {
                        JSONObject artist = (JSONObject) artists.get(i);
                        String id = (String) artist.get("id");
                        String name = (String) artist.get("name");

                        String imageUrl;
                        JSONArray images = (JSONArray) artist.get("images");
                        if (images.isNull(0)) {
                            imageUrl = null;
                        } else {
                            JSONObject imageOb = (JSONObject) images.get(1);
                            imageUrl = (String) imageOb.get("url");
                        }
                        Artist newArtist = new Artist(name, imageUrl, id);
                        JSONArray genres = (JSONArray) artist.get("genres");
                        int k = 0;
                        for (int j = 0; j < genres.length(); j++) {
                            if (!wrap.getTopGenres().containsValue((String) genres.get(j))) {
                                wrap.setGenre(String.valueOf(k + 1), (String) genres.get(j));
                                k++;
                            }
                        }

                        wrap.setArtist(String.valueOf(i + 1), newArtist);
//                        artistData.child(id).setValue(newArtist);

//                        user.child(currentUser.getId()).child("topArtists").push().setValue(newArtist);
                    }
                    synchronized (wrap.getTopArtists()) {
                        wrap.getTopArtists().notify();
                    }
//                    user.child(currentUser.getId()).child("topArtists").setValue(currentUser.getTop10Artists());

                } catch (Exception e) {
                    Log.wtf("Http", e.getMessage());
                }
            }

        });

    }
    public void updateUserInformation(Context context, String newUsername, String newEmail, String accessToken, String Spotid) {
        String userId = user.getId();

        HashMap<String, Object> updates = new HashMap<>();
        if (!TextUtils.isEmpty(updatedUser.getName()) && !updatedUser.getName().equals(currentUser.getName())) {
            updates.put("name", updatedUser.getName());
        }
        if (!TextUtils.isEmpty(updatedUser.getUsername()) && !updatedUser.getUsername().equals(currentUser.getUsername())) {
            updates.put("username", updatedUser.getUsername());
        }
        if (!TextUtils.isEmpty(updatedUser.getPassword()) && !updatedUser.getPassword().equals(currentUser.getPassword())) {
            updates.put("password", updatedUser.getPassword());
        }

        if (!updates.isEmpty()) {
            user.child(userId).updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Account updated successfully", Toast.LENGTH_SHORT).show();
                            currentUser = updatedUser;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error updating user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "No changes made", Toast.LENGTH_SHORT).show();
        }
    }

    public User getCurrentUser() {return currentUser;}


}
