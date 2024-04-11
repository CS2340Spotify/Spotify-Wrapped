package com.example.spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity {

    View updateProfileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button updateAccountBtn = findViewById(R.id.update_profile);
        ImageButton backButton = findViewById(R.id.back_to_home);
        Button deleteAccountBtn = findViewById(R.id.delete_account);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_settings, new ProfileFragment()).commit();
            }
        });
        updateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, UpdateAccountActivity.class);
                startActivity(i);
            }
        });
        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}