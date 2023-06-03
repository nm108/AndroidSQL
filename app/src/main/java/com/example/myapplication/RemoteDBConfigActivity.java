package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RemoteDBConfigActivity extends AppCompatActivity {

    private RemoteDBConfig rdbc = new RemoteDBConfig(RemoteDBConfigActivity.this);

    private Button saveConfigButton;

    private Button resetToDefaultsButton;

    private Button returnButton;

    private TextView titleTextView;

    private TextView ipAddressTextView;

    private EditText ipAddressEditText;

    private TextView portTextView;

    private EditText portEditText;

    private TextView dbNameTextView;

    private EditText dbNameEditText;

    private TextView dbInstanceTextView;

    private EditText dbInstanceEditText;

    private TextView usernameTextView;

    private EditText userNameEditText;

    private TextView passwordTextView;

    private EditText passwordEditText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_db_config);

        titleTextView = findViewById(R.id.TitleTextView);
        ipAddressTextView = findViewById(R.id.IPAddressTextView);
        ipAddressEditText = findViewById(R.id.IPAddressEditText);
        portTextView = findViewById(R.id.PortTextView);
        portEditText = findViewById(R.id.PortEditText);
        dbNameTextView = findViewById(R.id.DBNameTextView);
        dbNameEditText = findViewById(R.id.DBNameEditText);
        dbInstanceTextView = findViewById(R.id.DBInstanceTextView);
        dbInstanceEditText = findViewById(R.id.DBInstanceEditText);

        ipAddressEditText.setText(rdbc.getIpAddress());
        portEditText.setText(rdbc.getPort());
        dbNameEditText.setText(rdbc.getDefaultDBName());
        dbInstanceEditText.setText(rdbc.getDefaultDBInstance());

        usernameTextView = findViewById(R.id.UsernameTextView);
        userNameEditText = findViewById(R.id.UsernameEditText);

        passwordTextView = findViewById(R.id.PasswordTextView);
        passwordEditText = findViewById(R.id.PasswordEditText);

        userNameEditText.setText(rdbc.getDefaultUserName());
        passwordEditText.setText(rdbc.getDefaultPassword());

        saveConfigButton = findViewById(R.id.SaveConfigButton);
        saveConfigButton.setOnClickListener(
                (final View v) -> {
                    rdbc.setDbConfigValue(RemoteDBConfigActivity.this, findViewById(R.id.IPAddressEditText), FeedEntry.IP_ADDRESS);
                    rdbc.setDbConfigValue(RemoteDBConfigActivity.this, findViewById(R.id.PortEditText), FeedEntry.PORT);
                    rdbc.setDbConfigValue(RemoteDBConfigActivity.this, findViewById(R.id.DBNameEditText), FeedEntry.DB_NAME);
                    rdbc.setDbConfigValue(RemoteDBConfigActivity.this, findViewById(R.id.DBInstanceEditText), FeedEntry.DB_INSTANCE);
                    rdbc.setDbConfigValue(RemoteDBConfigActivity.this, findViewById(R.id.UsernameEditText), FeedEntry.USER_NAME);
                    rdbc.setDbConfigValue(RemoteDBConfigActivity.this, findViewById(R.id.PasswordEditText), FeedEntry.PASSWORD);
                });



        resetToDefaultsButton = findViewById(R.id.ResetToDefaultsButton);
        resetToDefaultsButton.setOnClickListener(
                (final View v) -> {
                    rdbc.setDbConfigToDefault(RemoteDBConfigActivity.this);

                    // restart activity
                    finish();
                    startActivity(getIntent());

                }
        );

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

