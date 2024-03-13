package com.example.spotify_wrapped;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class UpdateAccountViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<UpdateStatus> updateStatus = new MutableLiveData<>();
    public LiveData<UpdateStatus> getUpdateStatus() {
        return updateStatus;
    }
    public void updateAccount(User user) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("name", user.getName())
                .add("username", user.getUsername())
                .add("password", user.getPassword())
                .add("email", user.getEmail())
                .build();
    }
}

enum UpdateStatus {
    SUCCESS,
    FAILURE
}
