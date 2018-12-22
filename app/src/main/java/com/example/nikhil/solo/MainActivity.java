package com.example.nikhil.solo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    NetworkUtils networkUtils;

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

        String data = networkUtils.postRequest(postBody);
        openActivity(data);
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
