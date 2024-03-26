package com.example.spotify_wrapped;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class UpdateAccountFragment extends Fragment {

    private UpdateAccountViewModel mViewModel;

    public static UpdateAccountFragment newInstance() {
        return new UpdateAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_account, container, false);
        CardView cardViewUpdateInfo = view.findViewById(R.id.cardview_update_info);
        Button backToSettingsButton = view.findViewById(R.id.back_to_settings);
        backToSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to SettingsFragment
                Fragment settingsFragment = new SettingsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_main, settingsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateAccountViewModel.class);
        // TODO: Use the ViewModel
        User user = new User("name", "email", "id", "image", "password", "username", "accessToken");
        mViewModel.updateAccount(user);

        mViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), new Observer<UpdateStatus>() {
            @Override
            public void onChanged(UpdateStatus updateStatus) {
                if (updateStatus == UpdateStatus.SUCCESS) {
                    Toast.makeText(getActivity(), "Account updated", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity(), "Account update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}