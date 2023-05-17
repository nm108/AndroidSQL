package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button doQueryButton;
    EditText userNameEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doQueryButton = findViewById(R.id.Btn);
        userNameEditText = findViewById(R.id.Eb);

        doQueryButton.setOnClickListener(
            v -> {
                final String userName = userNameEditText.getText().toString().trim();
                new QueryForUserDataAsyncTask(MainActivity.this, userName).execute();
            }
        );
    }
}
