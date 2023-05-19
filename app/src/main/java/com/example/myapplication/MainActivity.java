package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Entry point for application.
 */
public class MainActivity extends AppCompatActivity {
    private Button doQueryButton;
    private EditText userNameEditText;

    private Button switchActivitiesButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doQueryButton = findViewById(R.id.DoQueryButton);
        userNameEditText = findViewById(R.id.UserNameEditText);
        switchActivitiesButton = findViewById(R.id.SwitchActivitiesButton);

        doQueryButton.setOnClickListener(
            (final View v) -> {
                final String userName = userNameEditText.getText().toString().trim();
                new QueryForUserDataAsyncTask(MainActivity.this, userName).execute();
            }
        );

        switchActivitiesButton.setOnClickListener(
                (final View v) -> {
                    switchActivities();
                }

        );


    }


    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, SecondaryActivity.class);
        startActivity(switchActivityIntent);
    }
}
