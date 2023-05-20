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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseConnectionConfigurationButton = findViewById(R.id.DatabaseConnectionConfigurationButton);

        databaseConnectionConfigurationButton.setOnClickListener(
                (final View v) -> {
                   goToDBConfButton();
                }
        );

    }


    private void goToDBConfButton() {
        Intent switchActivityIntent = new Intent(MainActivity.this, DatabaseQueryActivity.class);
        startActivity(switchActivityIntent);
    }
}
