package com.example.a2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GameSetup extends AppCompatActivity {

    private int sizes[] = {3,3,4,4,5,5};
    private int scores[] = {512,1024,2048,4096};
    Spinner sizeSpinner;
    Spinner scoreSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twenty_setup);
        sizeSpinner = findViewById(R.id.spinner_size2048);
        scoreSpinner = findViewById(R.id.spinner_target2048);
        ArrayList <String> sizeOptions = new ArrayList<>();
        ArrayList <String> scoreOptions = new ArrayList<>();
        for(int i = 0; i < sizes.length; i+=2){
            sizeOptions.add(sizes[i] + "x" + sizes[i+1]);
        }
        for (int i = 0; i < scores.length; i++) {
            scoreOptions.add(String.valueOf(scores[i]));
        }
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text,sizeOptions);
        sizeSpinner.setAdapter(sizeAdapter); // this will set list of values to spinner
        sizeSpinner.setSelection(sizeOptions.indexOf("4x4"));//set selected value in spinner
        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text,scoreOptions);
        scoreSpinner.setAdapter(scoreAdapter); // this will set list of values to spinner
        scoreSpinner.setSelection(scoreOptions.indexOf("2048"));//set selected value in spinner
    }

    public void continueButtonPress(View v){
        Intent intent = new Intent();
        int size;
        String selectedItem = (String) sizeSpinner.getSelectedItem();
        if ("3x3".equals(selectedItem)) {
            size = 3;
        } else if ("4x4".equals(selectedItem)) {
            size = 4;
        } else if ("5x5".equals(selectedItem)) {
            size = 5;
        } else {
            size = 4;
        }
        intent.putExtra("Size",size);
        intent.putExtra("Score",Integer.valueOf((String) scoreSpinner.getSelectedItem()));
        intent.setClass(getApplicationContext(), SplashTFE.class);
        startActivity(intent);
    }
}