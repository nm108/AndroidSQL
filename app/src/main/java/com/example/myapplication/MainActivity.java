package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Entry point for application.
 */
public class MainActivity extends AppCompatActivity {
     Button databaseConnectionConfigurationButton;

     Button selectButton;
    Button insertButton;
     Button updateButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseConnectionConfigurationButton = findViewById(R.id.DatabaseConnectionConfigurationButton);


        selectButton = findViewById(R.id.SelectButton);

        selectButton.setOnClickListener(
                (final View v) -> {
                    switchActivityTo(SelectActivity.class);
                }
        );

        insertButton = findViewById(R.id.InsertButton);
        insertButton.setOnClickListener(
                (final View v) -> {
                    switchActivityTo(InsertActivity.class);
                }
        );

        updateButton = findViewById(R.id.UpdateButton);
        updateButton.setOnClickListener(
                (final View v) -> {
                    switchActivityTo(UpdateActivity.class);
                }
        );
    }

    private void switchActivityTo(Class activityClass) {
        Intent switchActivityIntent = new Intent(MainActivity.this, activityClass);
        startActivity(switchActivityIntent);
    }
}
