package com.example.nikhil.solo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = null;

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

        callLoginApi(userName, password, postBody);
    }

    void callLoginApi(final String userName, final String password, String postBody){
        client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON,postBody);
        Request request = new Request.Builder()
                .url("http://192.168.1.109:3000/login")
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
                response.body().close();
                openActivity(data, userName, password);
            }
        });
    }


    public void openActivity(final String data, String userName, String password){
        if(data == null) return;
        if(!data.equals("failed")) {
            Intent intent = new Intent(this, Canvas.class);
            intent.putExtra("data", data);
            intent.putExtra("userName", userName);
            intent.putExtra("password", password);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(client != null){
            client.dispatcher().cancelAll();
            client.connectionPool().evictAll();
            try {
                client.cache().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client = null;
        }
    }
}
