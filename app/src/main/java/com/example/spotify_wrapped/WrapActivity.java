package com.example.spotify_wrapped;

import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class WrapActivity extends AppCompatActivity {
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccessToken = getIntent().getStringExtra("AccessToken");
        fetchTopArtists();
        fetchTopSongs();
    }

    public void fetchTopArtists() {
        fetchTopItems("https://api.spotify.com/v1/me/top/artists", "artists");
    }

    public void fetchTopSongs() {
        fetchTopItems("https://api.spotify.com/v1/me/top/tracks", "songs");
    }

    public void fetchTopItems(String url, String type) {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user's top artists
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(WrapActivity.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    final JSONArray items = jsonObject.getJSONArray("items");
                    final List<String> topItems = new ArrayList<>();
                    for (int i = 0; i < Math.min(items.length(), 5); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String itemName = item.getString("name");
                        topItems.add(itemName);
                    }
                    if (type.equals("artists")) {
                        updateUI(topItems, "artist");
                    } else if (type.equals("songs")) {
                        updateUI(topItems, "song");
                    }
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(WrapActivity.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(List<String> items, String type) {
        // Update your UI to display the top items
        runOnUiThread(() -> {
            for (int i = 0; i < items.size(); i++) {
                String itemName = items.get(i);
                int textViewId = getResources().getIdentifier(type + "_" + (i + 1), "id", getPackageName());
                TextView textView = findViewById(textViewId);
                textView.setText(itemName);
            }
        });
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> wrapActivity
