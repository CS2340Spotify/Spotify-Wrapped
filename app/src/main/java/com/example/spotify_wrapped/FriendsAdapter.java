package com.example.spotify_wrapped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FriendsAdapter extends ArrayAdapter<Friend> {

    public FriendsAdapter(Context context, ArrayList<Friend> friendsArrayList) {

        super(context, R.layout.fragment_friends, friendsArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Friend friend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_friends, parent, false);
        }

        ImageView friendPicture = convertView.findViewById(R.id.friend_picture);
        TextView friendName = convertView.findViewById(R.id.friend_name);

        friendPicture.setImageResource(friend.getImage());



    }
}
