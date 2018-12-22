package com.example.nikhil.solo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Canvas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Intent intent = getIntent();
        String data = intent.getStringExtra("Data");
        setData(data);
    }
    public void setData(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            TextView textView = findViewById(R.id.textView);
            textView.setText((String) jsonObject.get("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
