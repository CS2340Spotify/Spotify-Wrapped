package com.example.spotify_wrapped;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.spotify_wrapped.Wrap;

import java.util.ArrayList;

public class PastWrapsAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Wrap> wraps;
    LayoutInflater inflater;


    public PastWrapsAdapter(Context context, ArrayList<Wrap> wraps) {
        this.context = context;
        this.wraps = wraps;
        inflater = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return wraps.size();
    }

    @Override
    public Object getItem(int position) {
        return wraps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardView pastWrap;
        pastWrap = (CardView) inflater.inflate(R.layout.item_past_wrap, parent, false);
        TextView pastWrapName = (TextView) pastWrap.findViewById(R.id.past_wrap_name);
        String date = wraps.get(position).getDateMade();
        Instant instant = Instant.ofEpochSecond(Long.parseLong(date));
        Date updatedDate = Date.from(instant);
        pastWrapName.setText(updatedDate.toString());

        return pastWrap;
    }

}
