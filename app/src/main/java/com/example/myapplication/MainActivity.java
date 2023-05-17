package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Entry point for application.
 */
public class MainActivity extends AppCompatActivity {
    private Button doQueryButton;
    private EditText userNameEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doQueryButton = findViewById(R.id.Btn);
        userNameEditText = findViewById(R.id.Eb);

        doQueryButton.setOnClickListener(
            (final View v) -> {
                final String userName = userNameEditText.getText().toString().trim();
                new QueryForUserDataAsyncTask(MainActivity.this, userName).execute();
            }
        );
    }
}
