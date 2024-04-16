package com.example.spotify_wrapped;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Response;
import okhttp3.ResponseBody;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link InsightsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class InsightsFragment extends Fragment {

    UserViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insights_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User currentUser = model.getCurrentUser();

        Wrap wrap = model.makeNewWrapped(UserItemTimeFrame.SHORT, getActivity());
        List<Artist> topArtistsList = null;
        List<Track> topTracksList = null;
        List<String> topGenresList = null;

        if (wrap.getTopArtists() != null) {
            HashMap<String, Artist> currentUserTopArtists = wrap.getTopArtists();
            topArtistsList = new ArrayList<>(currentUserTopArtists.values());
        }

        if (wrap.getTopTracks() != null) {
            HashMap<String, Track> currentUserTopTracks = wrap.getTopTracks();
            topTracksList = new ArrayList<>(currentUserTopTracks.values());
        }

        if (wrap.getTopGenres() != null) {
            HashMap<String, String> currentUserTopGenres = wrap.getTopGenres();
            topGenresList = new ArrayList<>(currentUserTopGenres.values());
        }

        StringBuilder prompt = new StringBuilder();
        if (topArtistsList != null) {
            prompt.append("Here are the users top artists: ");
            for (Artist a : topArtistsList) {
                prompt.append(a.getName());
                prompt.append(",");
            }
        }

        if (topGenresList != null) {
            prompt.append("Here are the users top genres: ");
            for (String a : topGenresList) {
                prompt.append(a);
                prompt.append(", ");
            }
        }

        if (topTracksList != null) {
            prompt.append("Here are the users top tracks: ");
            for (Track a : topTracksList) {
                prompt.append(a.getTrackName());
                prompt.append(", ");
            }
        }

        LLMQueryManager manager = new LLMQueryManager();
        ArrayList<String> preferences = new ArrayList<>();
        final boolean[] flag = {false};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Log.d("Prompt", prompt.toString());
                    Response reply = manager.queryPrompt(prompt.toString());
                    ResponseBody replyBody = reply.body();
                    JSONObject replyJson = new JSONObject(replyBody.string());
                    String output = replyJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    JSONObject jsonOutput = new JSONObject(output);
                    JSONArray out = jsonOutput.getJSONArray("preferences");
                    for (int i = 0; i < out.length(); i++) {
                        preferences.add(out.getString(i));
                    }
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<Insight> insights = new ArrayList<>();
                            for (int i = 0; i < Math.min(preferences.size(), 5); i++) {
                                String title = "Preference #" + (i + 1);
                                String description = preferences.get(i);
                                Log.d("title", title);
                                Log.d("desc", description);
                                insights.add(new Insight(title, description));
                            }

                            RecyclerView recyclerView = view.findViewById(R.id.insights_recycler_view);
                            InsightsAdapter adapter = new InsightsAdapter(insights);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}