package com.example.tapv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startConnect(View view) {
        Intent conIntent = new Intent(this, beam.class);
        Intent loggedIntent = getIntent();

        String user = loggedIntent.getStringExtra("USERNAME");
        String pass = loggedIntent.getStringExtra("PASSWORD");

        conIntent.putExtra("STARTUSER", user);
        conIntent.putExtra("STARTPASS", pass);

        startActivity(conIntent);

    }
}
