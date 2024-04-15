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
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.spotify_wrapped.databinding.FragmentPastWrapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PastWrapsFragment extends Fragment {

    private FragmentPastWrapsBinding binding;

    private UserViewModel model;

    private PastWrapsAdapter pastWrapsAdapter;

    private ArrayList<Wrap> wrapsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPastWrapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);

        model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        GridView gridView = binding.gridView;

        User currentUser = model.getCurrentUser();

        wrapsList = null;

        if (currentUser.getUserWraps() != null) {
            HashMap<String, Wrap> currentUserWraps = currentUser.getUserWraps();
            wrapsList = new ArrayList<>(currentUserWraps.values());
            pastWrapsAdapter = new PastWrapsAdapter(getActivity(), wrapsList);
            gridView.setAdapter(pastWrapsAdapter);
            pastWrapsAdapter.notifyDataSetChanged();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Wrap clickedWrap = wrapsList.get(position);

                    replaceFragment(new WrapFragment(clickedWrap));

                }
            });
        }

        View pastWrapsBackButton = view.findViewById(R.id.past_wraps_back_button);
        pastWrapsBackButton.setOnClickListener(new View.OnClickListener() {
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
}