package com.example.spotify_wrapped;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class UpdateAccountActivity extends AppCompatActivity {
    private UpdateAccountViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
    }
}