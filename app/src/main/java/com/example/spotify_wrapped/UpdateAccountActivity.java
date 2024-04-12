package com.example.spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class UpdateAccountActivity extends AppCompatActivity {
    private UserViewModel mViewModel;
    private EditText editName, editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        ImageButton backToSettingsBtn = findViewById(R.id.back_to_settings);
        editName = findViewById(R.id.edit_name);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.update_password);
        Button saveChangesBtn = findViewById(R.id.save_changes);

        mViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        backToSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editName.getText().toString().trim();
                String newUsername = editUsername.getText().toString().trim();
                String newPassword = editPassword.getText().toString().trim();

                // creating user object with updated information
                User updatedUser = new User(newName, "email", "id", "image", newPassword, newUsername, "accessToken", "spotId");


                mViewModel.updateUserInformation(updatedUser, UpdateAccountActivity.this);
            }
        });


        backToSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateAccountActivity.this, SettingsFragment.class);
                startActivity(i);
            }
        });
    }
}
