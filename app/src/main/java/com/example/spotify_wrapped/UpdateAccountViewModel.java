package com.example.spotify_wrapped;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;

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

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + user.getAccessToken())
                .put(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateStatus.postValue(UpdateStatus.FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    updateStatus.postValue(UpdateStatus.SUCCESS);
                } else {
                    updateStatus.postValue(UpdateStatus.FAILURE);
                }
            }
        });
    }
}

enum UpdateStatus {
    SUCCESS,
    FAILURE
}
