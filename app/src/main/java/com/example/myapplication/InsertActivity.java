package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InsertActivity extends AppCompatActivity{
    private Button doInsertQueryButton;

    private Button returnButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        doInsertQueryButton = findViewById(R.id.DoInsertQueryButton);
        returnButton = findViewById(R.id.ReturnButton);

        returnButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );


    }


    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }
}

