package com.example.spotify_wrapped;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private UserViewModel model;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        RecyclerView playlistsRecyclerView = view.findViewById(R.id.playlists_recycler_view);
        List<Playlist> playlistsList = new ArrayList<>();
        playlistsList.add(new Playlist("Playlist 1", "Image URL 1", null, new ArrayList<>()));
        PlaylistsAdapter playlistsAdapter = new PlaylistsAdapter(getContext(), playlistsList);
        playlistsRecyclerView.setAdapter(playlistsAdapter);

        RecyclerView followingRecyclerView = view.findViewById(R.id.following_recycler_view);
        List<Following> followingList = new ArrayList<>();
        followingList.add(new Following("Artist 1", "Image URL 1"));
        FollowingAdapter followingAdapter = new FollowingAdapter(getContext(), followingList);
        followingRecyclerView.setAdapter(followingAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.VISIBLE);

        View settingsButton = view.findViewById(R.id.settings_button);
        View wrapButton = view.findViewById(R.id.wrap_button);
        View pastWrapsButton = view.findViewById(R.id.past_wraps_button);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SettingsFragment());
            }
        });

        wrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ChooseTimeWrapFragment());
            }
        });

        pastWrapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PastWrapsFragment());
            }
        });

    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}