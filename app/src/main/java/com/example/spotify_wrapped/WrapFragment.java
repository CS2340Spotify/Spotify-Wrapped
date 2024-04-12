package com.example.spotify_wrapped;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.spotify_wrapped.UserItemTimeFrame.LONG;
import static com.example.spotify_wrapped.UserItemTimeFrame.MEDIUM;
import static com.example.spotify_wrapped.UserItemTimeFrame.SHORT;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WrapFragment extends Fragment {

    private UserViewModel model;

    private String timeChoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            timeChoice = bundle.getString("timeChoice");
        }
        return inflater.inflate(R.layout.fragment_wrap, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        User currentUser = model.getCurrentUser();

        Wrap currentWrap;



        if (timeChoice.equals("1")) {
            currentWrap = model.makeNewWrapped(SHORT, getActivity());
            Log.wtf("m", "huh");
        } else if (timeChoice.equals("2")) {
            currentWrap = model.makeNewWrapped(MEDIUM, getActivity());
        } else {
            currentWrap = model.makeNewWrapped(LONG, getActivity());
        }



        List<Artist> topArtistsList = null;
        List<Track> topTracksList = null;
        List<String> topGenresList = null;

        if (currentWrap.getTopArtists() != null) {
            HashMap<String, Artist> currentUserTopArtists = currentWrap.getTopArtists();
            topArtistsList = new ArrayList<>(currentUserTopArtists.values());
        }

        if (currentWrap.getTopTracks() != null) {
            HashMap<String, Track> currentUserTopTracks = currentWrap.getTopTracks();
            topTracksList = new ArrayList<>(currentUserTopTracks.values());
        }


        if (currentWrap.getTopGenres() != null) {
            HashMap<String, String> currentUserTopGenres = currentWrap.getTopGenres();
            topGenresList = new ArrayList<>(currentUserTopGenres.values());
        }

        for (int i = 0; i < Math.min(topArtistsList.size(), 5); i++) {
            Artist artist = topArtistsList.get(i);
            TextView artistTextView = (TextView) getView().findViewById(getResources().getIdentifier("artist_" + (i + 1), "id", getActivity().getPackageName()));
            artistTextView.setText((i + 1) + "    " + artist.getName());
        }

        for (int i = 0; i < Math.min(topTracksList.size(), 5); i++) {
            Track track = topTracksList.get(i);
            TextView trackTextView = (TextView) getView().findViewById(getResources().getIdentifier("song_" + (i + 1), "id", getActivity().getPackageName()));
            trackTextView.setText((i + 1) + "    " + track.getTrackName());
            trackTextView.setSelected(true);
        }

        String topGenre = topGenresList.get(0);
        TextView topGenreTextView = (TextView) getView().findViewById(R.id.top_genre);
        topGenreTextView.setText(topGenre);
        topGenreTextView.setSelected(true);

        new DownloadImageFromInternet((ImageView) getView().findViewById(R.id.top_track_album_image), getActivity()).execute(topTracksList.get(0).getAlbumImage());

        View wrapBackButton = view.findViewById(R.id.wrap_back_button);
        wrapBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public static Drawable loadImageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        Activity context;

        public DownloadImageFromInternet(ImageView imageView, Activity context) {
            this.imageView = imageView;
            this.context = context;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}