package com.example.spotify_wrapped;

import com.example.spotify_wrapped.User;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyAuthenticator {
    public static final String CLIENT_ID = "5f05cbeda0b94c5fa48f4c6a1b5e56cd";
    public static final String REDIRECT_URI = "spotify-wrapped://auth";
    private Call call;
    final OkHttpClient okHttpClient = new OkHttpClient();
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");;



    public void getToken(Activity context) {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(context, 0, request);
    }

    public void trySpotifyLogin(Activity context) {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(context, 1, request);
    }

    public void createNewUser(Activity context, String accessToken, String username, String password) throws IllegalArgumentException {
        if (username.equals("") || password.equals("")) {
            throw new IllegalArgumentException();
        }
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(context, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());


                    String name = (String) jsonObject.get("display_name");
                    String email = (String) jsonObject.get("email");
                    String id = (String) jsonObject.get("id");
                    JSONArray imageArray = (JSONArray) jsonObject.get("images");
                    String image;
                    if (imageArray.isNull(0)) {
                        image = null;
                    } else {
                        JSONObject imageOb = (JSONObject) imageArray.get(1);
                        image = (String) imageOb.get("url");
                    }
                    User newUser = new User(name, email, id, image, password, username, accessToken);

                    mDatabase.child(id).setValue(newUser);
                } catch (JSONException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context, "Failed to Parse data", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                }
            }
        });

    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

}
