package com.example.nikhil.solo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    public String postUrl = "http://192.168.43.173:3000/login";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void validateInput(View view){
        EditText login = findViewById(R.id.editText3);
        EditText pass = findViewById(R.id.editText5);
        String userName = login.getText().toString();
        String password = pass.getText().toString();
        String postBody= "{\n" +
                "    \"userName\": \"" + userName + "\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";

        try {
            postRequest(postUrl,postBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void postRequest(String postUrl,String postBody) throws IOException {

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
                        openActivity("Login Failed");
                    else
                        openActivity(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void openActivity(final String data){
        if(!data.equals("Login Failed")) {
            Intent intent = new Intent(this, Canvas.class);
            intent.putExtra("Data", data);
            startActivity(intent);
        }else {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
