package com.example.spotify_wrapped;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
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
        ImageView profileImage = view.findViewById(R.id.profile_image);
        TextView profileUsername = view.findViewById(R.id.profile_username);
        TextView profileEmail = view.findViewById(R.id.profile_email);

        User currentUser = model.getCurrentUser();

        String currentUserUsername = currentUser.getUsername();
        String currentUserEmail = currentUser.getEmail();
        String currentUserImage = currentUser.getImage();

        profileUsername.setText(currentUserUsername);
        profileEmail.setText(currentUserEmail);

        WrapFragment.DownloadImageFromInternet downloadImageFromInternet = (WrapFragment.DownloadImageFromInternet) new WrapFragment.DownloadImageFromInternet(profileImage, getActivity()).execute(currentUserImage);

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