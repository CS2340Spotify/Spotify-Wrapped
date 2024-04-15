package com.example.spotify_wrapped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistsViewHolder> {

    private List<Playlist> playlistsList;
    private Context context;

    public PlaylistsAdapter(Context context, List<Playlist> playlistsList) {
        this.context = context;
        this.playlistsList = playlistsList;
    }

    @NonNull
    @Override
    public PlaylistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistsViewHolder holder, int position) {
        Playlist playlist = playlistsList.get(position);
        holder.playlistName.setText(playlist.getName());
        // Use a library like Picasso or Glide to load the image from the URL
        // For example, if you're using Picasso:
         Picasso.get().load(playlist.getImageUrl()).into(holder.playlistImage);
    }

    @Override
    public int getItemCount() {
        return playlistsList.size();
    }

    public static class PlaylistsViewHolder extends RecyclerView.ViewHolder {
        ImageView playlistImage;
        TextView playlistName;

        public PlaylistsViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistImage = itemView.findViewById(R.id.playlist_image);
            playlistName = itemView.findViewById(R.id.playlist_name);
        }
    }
}
