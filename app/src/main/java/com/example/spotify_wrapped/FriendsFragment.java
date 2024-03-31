package com.example.spotify_wrapped;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.Toast;

import com.example.spotify_wrapped.databinding.FriendsLayoutBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FriendsFragment extends Fragment {

    private ArrayList<Friend> friends = new ArrayList<>();

    private FriendsLayoutBinding binding;

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FriendsLayoutBinding.inflate(inflater, container, false);
        GridView grid = binding.friendListView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}