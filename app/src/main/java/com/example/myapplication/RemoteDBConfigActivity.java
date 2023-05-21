package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RemoteDBConfigActivity extends AppCompatActivity {
    private Button saveConfigButton;

    private Button ResetToDefaultsButton;

    private Button returnButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_db_config);
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

