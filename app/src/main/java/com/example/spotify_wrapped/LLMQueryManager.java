package com.example.spotify_wrapped;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LLMQueryManager {
    public static final String API_KEY = BuildConfig.OPENAI_API_KEY;
    final OkHttpClient okHttpClient = new OkHttpClient();

    private Call call;

    public void queryPrompt(String prompt) throws JSONException {
        String body = "{" +
                "\"model\":\"gpt-3.5-turbo\", " +
                "messages: [{\"role\":\"system\", \"content\": " +
                "\"You are a spotify assistant with the goal of describing users based on their music taste. " +
                "\nYou will be given information about the user and should describe things like how they would act, think, and dress.}," +
                "{\"role\": \"user\", \"content\":" + prompt + "}]";
        RequestBody reqBody = RequestBody.create(body, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(reqBody)
                .build();
        call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response);
            }
        });
    }
}
