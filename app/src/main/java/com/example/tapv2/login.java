package com.example.tapv2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Set;

public class login extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        ImageView imageView = findViewById(R.id.imgLogoLogin);
        imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo));*/
        setContentView(R.layout.login);
        perms(this);
        locationsFile(this);
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
    public void randomDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
        builder.setTitle("Use a Random Username and Password?");
        builder.setMessage("By doing this you will not be able to connect to the Wi-Fi using the same credentials on your laptop or other NON-NFC enabled Android devices.");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        randomDetails();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void randomDetails(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor saveDetails = preferences.edit();
        Intent loggedin = new Intent(this, MainActivity.class);

        String user = randomString();
        String pass = randomString();

        saveDetails.putString("USERNAME", user);
        saveDetails.putString("PASSWORD", pass);
        saveDetails.apply();
        startActivity(loggedin);
    }
    public String randomString(){
        String source = "ABCDEFGHIJKMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        char[] string = new char[10];
        for (int i =0; i < 10; i++) {
            string[i] = source.charAt(random.nextInt(source.length()));
        }

        return new String(string);
    }

    public void locationsFile(Context context) {
        File file = new File(context.getFilesDir(), "/past_locations.txt");
        boolean exists = file.exists();
        if (!exists) {
            try {
                file.createNewFile();
                Toast.makeText(context, "Locations File Created", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                return;
            }
        } else {
            Toast.makeText(context, "Locations File Exists", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public boolean perms(Context context){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NFC}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        return true;
    }
}