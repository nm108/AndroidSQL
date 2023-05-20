package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Entry point for application.
 */
public class DatabaseQueryActivity extends AppCompatActivity {
    private Button doQueryButton;
    private EditText userNameEditText;

    private Button returnButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_query);
        doQueryButton = findViewById(R.id.DoQueryButton);
        userNameEditText = findViewById(R.id.UserNameEditText);
        returnButton = findViewById(R.id.SwitchActivitiesButton);

        doQueryButton.setOnClickListener(
            (final View v) -> {
                final String userName = userNameEditText.getText().toString().trim();
                new QueryForUserDataAsyncTask(DatabaseQueryActivity.this, userName).execute();
            }
        );

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
