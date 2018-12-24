package com.example.nikhil.solo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Canvas extends AppCompatActivity {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String userName;
    private String password;
    private Boolean editButtonClickable = true;
    private Boolean saveButtonClickable = false;
    EditText savedEditText = null;
    Button editButton = null;
    Button saveButton = null;
    OkHttpClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        userName = intent.getStringExtra("userName");
        password = intent.getStringExtra("password");
        setData(data);
    }
    public void setData(String data){
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);
        setClickableButtons();
        try {
            JSONObject jsonObject = new JSONObject(data);
            savedEditText = findViewById(R.id.savedEditText);
            savedEditText.setText((String) jsonObject.get("data"));
            savedEditText.setFocusable(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onEditClicked(View view) {
            if(savedEditText == null)   return;
        saveButtonClickable = true;
        editButtonClickable = false;
        setClickableButtons();
        savedEditText.setFocusableInTouchMode(true);
    }

    public void onSaveClicked(View view) {
        if(savedEditText == null) return;
        saveButtonClickable = false;
        editButtonClickable = true;
        String data = savedEditText.getText().toString();
        String postBody= "{\n" +
                "    \"userName\": \"" + userName + "\",\n" +
                "    \"password\": \"" + password + "\",\n" +
                "    \"data\": \"" + data + "\"\n" +
                "}";
        callUpdateApi(postBody);
    }

    void callUpdateApi(String postBody){
        client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON,postBody);
        Request request = new Request.Builder()
                .url("http://192.168.1.109:3000/updateData")
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
                afterUpdateApi(data);


            }
        });
    }

    void afterUpdateApi(String updateData){
        if(updateData == null) return;
        final String toastMessage = updateData.contains("success") ? "Data Saved!" : "Can not Save Data! Please try again";
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_SHORT).show();
                savedEditText.setFocusable(false);
                setClickableButtons();
            }
        });
    }

    void setClickableButtons(){
        editButton.setEnabled(editButtonClickable);
        saveButton.setEnabled(saveButtonClickable);
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
