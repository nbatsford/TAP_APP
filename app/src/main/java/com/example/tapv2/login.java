package com.example.tapv2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Set;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }
    public void logIn(View view){
        EditText userInput = findViewById(R.id.editUser);
        EditText passInput = findViewById(R.id.editPass);
        TextView error = (TextView)findViewById(R.id.txtError);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor saveDetails = preferences.edit();

        Intent loggedIntent = new Intent(this, MainActivity.class);

        String user = userInput.getText().toString();
        String pass = passInput.getText().toString();
        saveDetails.putString("USERNAME", user);
        saveDetails.putString("PASSWORD", pass);
        saveDetails.apply();
        loggedIntent.putExtra("USERNAME", user);
        loggedIntent.putExtra("PASSWORD", pass);
        if (TextUtils.isEmpty(userInput.getText()) && TextUtils.isEmpty(passInput.getText())) {
            error.setText("Please Enter a username and password");
            error.setVisibility(View.VISIBLE);
            error.setTextColor(Color.rgb(200,0,0));

        } else if (TextUtils.isEmpty(passInput.getText())) {
            error.setText("Please Enter a Password");
            error.setVisibility(View.VISIBLE);
            error.setTextColor(Color.rgb(200,0,0));
        } else {
                startActivity(loggedIntent);
        }
    }
    public void loginHelp(View view){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.login_help, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int hight = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focus = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, hight, focus);
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }
}