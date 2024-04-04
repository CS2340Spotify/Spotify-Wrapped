package com.example.spotify_wrapped;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {

    private SettingsFragmentViewModel mViewModel;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }
    View updateProfileBtn;
    View darkLightBtn;
    private SharedPreferences sharedPreferences;
    private boolean isDarkModeEnabled;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateProfileBtn = view.findViewById(R.id.update_profile);
        darkLightBtn = view.findViewById(R.id.dark_light_mode);
        sharedPreferences = this.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isDarkModeEnabled = sharedPreferences.getBoolean("isDarkModeEnabled", false);

        if(isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // on click listener for the button
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Fragment updateAccountFragment = new UpdateAccountFragment();
//                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.activity_main, updateAccountFragment);
//                transaction.commit();
//                Intent intent = new Intent(SettingsFragment.this, UpdateAccountFragment);
//                startActivity(intent);
//                finish();
            }
        });

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);
//        // TODO: Use the ViewModel
//    }

}