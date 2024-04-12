package com.example.spotify_wrapped;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

        TextView insightTitle = view.findViewById(R.id.insight_card_title);
        TextView insightDescription = view.findViewById(R.id.insight_card_description);


    }
}