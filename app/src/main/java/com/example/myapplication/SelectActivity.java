package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * Entry point for application.
 */
public class SelectActivity extends AppCompatActivity {
    private Button doSelectQueryButton;
    private EditText userNameEditText;

    private Button returnButton;

    private String userName = "aw108";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        doSelectQueryButton = findViewById(R.id.DoSelectQueryButton);
        userNameEditText = findViewById(R.id.UserNameEditText);
        returnButton = findViewById(R.id.ReturnButton);

        doSelectQueryButton.setOnClickListener(
            (final View v) -> {
                JDBCDatabaseHelper jdbcDatabaseHelper = new JDBCDatabaseHelper();
                List selectList = null;
                try {
                    selectList = jdbcDatabaseHelper.doSelect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(selectList);
                String listString = String.join(", ", selectList);
                userNameEditText.setText(listString);
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
