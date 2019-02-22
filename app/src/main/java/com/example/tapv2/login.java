package com.example.tapv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }
    public void logIn(View view){
        EditText userInput = findViewById(R.id.editUser);
        EditText passInput = findViewById(R.id.editPass);
        TextView error = (TextView)findViewById(R.id.textError);
        Intent loggedIntent = new Intent(this, MainActivity.class);

        String user = userInput.getText().toString();
        String pass = passInput.getText().toString();

        loggedIntent.putExtra("USERNAME", user);
        loggedIntent.putExtra("PASSWORD", pass);
        if (TextUtils.isEmpty(userInput.getText()) && TextUtils.isEmpty(passInput.getText())) {
            error.setText("Please Enter a username and password");
            error.setTextColor();

        } else if (TextUtils.isEmpty(passInput.getText())) {
            error.setText("Please Enter a Password");
        } else {
                startActivity(loggedIntent);
                }
                }
                }