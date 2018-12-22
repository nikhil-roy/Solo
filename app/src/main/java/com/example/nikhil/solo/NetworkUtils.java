package com.example.nikhil.solo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class NetworkUtils {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String apiResponse = null;

    String postRequest(String postBody) {
        String postUrl = "http://0.0.0.0:3000/login";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string();
                Log.d("TAG",data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if(jsonObject.has("status"))
//                        Toast.makeText(getApplicationContext(),"Login Failed!",Toast.LENGTH_LONG).show();
                        apiResponse = "Login Failed";
                    else
                        apiResponse = data;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return apiResponse;
    }
}
