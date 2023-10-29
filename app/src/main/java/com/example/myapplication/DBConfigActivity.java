package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DBConfigActivity extends AppCompatActivity {

   private DBConfigSQLiteHelper sqlh =
           new DBConfigSQLiteHelper(DBConfigActivity.this);
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

        ipAddressEditText.setText(sqlh.getIpAddress());
        portEditText.setText(sqlh.getPort());
        dbNameEditText.setText(sqlh.getDBName());
        dbInstanceEditText.setText(sqlh.getDBInstance());

        usernameTextView = findViewById(R.id.UsernameTextView);
        userNameEditText = findViewById(R.id.UsernameEditText);

        passwordTextView = findViewById(R.id.PasswordTextView);
        passwordEditText = findViewById(R.id.PasswordEditText);

        userNameEditText.setText(sqlh.getUserName());
        passwordEditText.setText(sqlh.getPassword());

        saveConfigButton = findViewById(R.id.SaveConfigButton);
        saveConfigButton.setOnClickListener(
                (final View v) -> {
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.IPAddressEditText), DBConfigEntry.IP_ADDRESS);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.PortEditText), DBConfigEntry.PORT);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.DBNameEditText), DBConfigEntry.DB_NAME);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.DBInstanceEditText), DBConfigEntry.DB_INSTANCE);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.UsernameEditText), DBConfigEntry.USER_NAME);
                    sqlh.setDbConfigValue(DBConfigActivity.this, findViewById(R.id.PasswordEditText), DBConfigEntry.PASSWORD);
                    switchActivities();
                });



        resetToDefaultsButton = findViewById(R.id.ResetToDefaultsButton);
        resetToDefaultsButton.setOnClickListener(
                (final View v) -> {
                    sqlh.setDbConfigToDefault(DBConfigActivity.this);

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

