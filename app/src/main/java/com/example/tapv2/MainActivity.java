package com.example.tapv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startConnect(View view) {
        Intent conIntent = new Intent(this, Beam.class);
        startActivity(conIntent);
    }


    public void connHelp(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.conn_help, null);
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
