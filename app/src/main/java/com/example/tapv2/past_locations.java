package com.example.tapv2;

import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class past_locations extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastlocations);
        populate();

    }

    public void populate(){
        ArrayList<String> location = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_Loc);
        try {

            InputStream inputStream = openFileInput("past_locations.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = in.readLine();
            Log.d("LINE", line);
            while (line !=null){
                location.add(line);
                line = in.readLine();
            }
            Log.d("ARRAY", location.get(0));
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_viewt_text, R.id.textList, location);
            listView.setAdapter(arrayAdapter);
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clearLoc(View view) {
        String file_name = "past_locations.txt";

        try {
            FileOutputStream outputStream = openFileOutput(file_name, MODE_PRIVATE);
            try {
                outputStream.write("".getBytes());
                outputStream.close();
                listView.setAdapter(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
