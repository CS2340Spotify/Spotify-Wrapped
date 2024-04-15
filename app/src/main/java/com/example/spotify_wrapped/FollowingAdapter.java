package com.example.spotify_wrapped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    private List<Following> mData;
    private LayoutInflater mInflater;

    public FollowingAdapter(Context context, List<Following> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Following following = mData.get(position);
        holder.artistName.setText(following.getArtistName());
        Picasso.get().load(following.getArtistImageUrl()).into(holder.artistImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView artistImage;
        TextView artistName;

        ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistName = itemView.findViewById(R.id.artist_name);
        }
    }
}