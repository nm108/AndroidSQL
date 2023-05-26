package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RemoteDBConfigActivity extends AppCompatActivity {

    private RemoteDBConfig rdbc = new RemoteDBConfig();

    private Button saveConfigButton;

    private Button ResetToDefaultsButton;

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

