package com.example.spotify_wrapped;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WrapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WrapFragment extends Fragment {

    private UserViewModel model;

    public WrapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrap, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        User currentUser = model.getCurrentUser();

        List<Artist> topArtistsList = null;
        List<Track> topTracksList = null;

        if (currentUser.getTop10Artists() != null) {
            LinkedHashMap<String, Artist> currentUserTopArtists = model.getCurrentUser().getTop10Artists();
            topArtistsList = new ArrayList<>(currentUserTopArtists.values());
        }

        if (currentUser.getTop20Tracks() != null) {
            LinkedHashMap<String, Track> currentUserTopTracks = model.getCurrentUser().getTop20Tracks();
            topTracksList = new ArrayList<>(currentUserTopTracks.values());
        }

        for (int i = 0; i < 5; i++) {
            Artist artist = topArtistsList.get(i);
            TextView artistTextView = (TextView) getView().findViewById(getResources().getIdentifier("artist_" + (i + 1), "id", getActivity().getPackageName()));
            artistTextView.setText((i + 1) + "    " + artist.getName());
        }

        for (int i = 0; i < 5; i++) {
            Track track = topTracksList.get(i);
            TextView trackTextView = (TextView) getView().findViewById(getResources().getIdentifier("song_" + (i + 1), "id", getActivity().getPackageName()));
            trackTextView.setText((i + 1) + "    " + track.getTrackName());
            trackTextView.setSelected(true);
        }

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