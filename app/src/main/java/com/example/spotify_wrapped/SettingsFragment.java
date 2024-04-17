package com.example.spotify_wrapped;

import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsFragment extends Fragment {

    private UserViewModel model;
    //private User currentUser;


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    View updateProfileBtn;
    private User currentUser;

    View settingsBackBtn;
    View darkLightBtn;
    private SharedPreferences sharedPreferences;
    private boolean isDarkModeEnabled;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        currentUser = model.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();

        currentUser = model.getCurrentUser();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);

        updateProfileBtn = view.findViewById(R.id.update_profile);
        settingsBackBtn = view.findViewById(R.id.settings_back_button);
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
                Intent intent = new Intent(getActivity(), UpdateAccountActivity.class);
                intent.putExtra("userId", currentUser.getId());
                intent.putExtra("accessToken", currentUser.getAccessToken());
                startActivity(intent);
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        settingsBackBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
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

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);
//        // TODO: Use the ViewModel
//    }

}