package com.example.spotify_wrapped;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.spotify_wrapped.SpotifyAuthenticator;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class LoginActivity extends AppCompatActivity {

    private SpotifyAuthenticator authenticator;
    private String accessToken;
    private boolean linkedToSpotify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        authenticator = new SpotifyAuthenticator();

        CardView cardViewSignIn = findViewById(R.id.cardview_sign_in);
        CardView cardViewSignUp = findViewById(R.id.cardview_sign_up);
        TextView signUpTextView = findViewById(R.id.sign_up_selection_clickable);
        TextView signInTextView = findViewById(R.id.sign_in_selection_clickable);
        EditText newUserName = findViewById(R.id.username_sign_up);
        EditText newPassword = findViewById(R.id.password_sign_up);
        Button linkSpotify = findViewById(R.id.linkSpotifyButton);
        Button submitSignUp = findViewById(R.id.signup_button);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewSignIn.setVisibility(View.GONE);
                cardViewSignUp.setVisibility(View.VISIBLE);
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewSignIn.setVisibility(View.VISIBLE);
                cardViewSignUp.setVisibility(View.GONE);
            }
        });

        linkSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticator.getToken(LoginActivity.this);
            }
        });

        submitSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linkedToSpotify) {
                    String username = newUserName.getText().toString();
                    String password = newPassword.getText().toString();
                    try {
                        authenticator.createNewUser(LoginActivity.this, accessToken, username, password);
                        LoginActivity.this.finish();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(LoginActivity.this, "Please fill out all fields",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Please connect to your Spotify account",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        if (requestCode == 0) {
            accessToken = response.getAccessToken();
            linkedToSpotify = true;
        } else if (requestCode == 1) {
            accessToken = response.getAccessToken();

        }
    }

}